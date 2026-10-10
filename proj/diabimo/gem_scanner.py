#!/usr/bin/env python3
"""
Diablo Immortal Legendary Gem Inventory Scanner CLI.

Scans inventory screenshots, identifies gem icons via template matching against
a configurable directory, extracts Rank (bottom-left) and Quantity (bottom-right)
via OCR, and outputs the result in JSON-per-line (JSONL) format.
Automatically saves unrecognized gems as 'unknown-<id>.png' into the gems directory.
"""

import argparse
import json
import logging
import os
from pathlib import Path
import secrets
import sys
from typing import Any, Dict, List, Optional, Tuple

import cv2
import numpy as np
import pytesseract

logger = logging.getLogger("gem_scanner")


def detect_grid_cells(
    img: np.ndarray,
    min_cell_size: int = 60,
    max_cell_size: int = 350,
    min_fill_ratio: float = 0.85,
) -> Tuple[List[Tuple[int, int]], List[Tuple[int, int]], float]:
    """
    Detects full grid cell intervals (rows and cols) in an inventory screenshot.
    Uses dimension-aware Hough line transform and circular lattice phase consensus
    to accurately locate grid boundaries, interpolating missing lines and filtering
    out partial cells cut off at screenshot margins.
    """
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    height, width = gray.shape

    edges = cv2.Canny(gray, 30, 100)
    min_h_len = int(width * 0.35)
    min_v_len = int(height * 0.35)

    lines_h = cv2.HoughLinesP(
        edges, 1, np.pi / 180, threshold=80, minLineLength=min_h_len, maxLineGap=20
    )
    lines_v = cv2.HoughLinesP(
        edges, 1, np.pi / 180, threshold=80, minLineLength=min_v_len, maxLineGap=20
    )

    h_lines: List[Tuple[int, float]] = []
    if lines_h is not None:
        for x1, y1, x2, y2 in lines_h.reshape(-1, 4):
            if abs(y2 - y1) <= 2:
                h_lines.append(((y1 + y2) // 2, float(np.hypot(x2 - x1, y2 - y1))))

    v_lines: List[Tuple[int, float]] = []
    if lines_v is not None:
        for x1, y1, x2, y2 in lines_v.reshape(-1, 4):
            if abs(x2 - x1) <= 2:
                v_lines.append(((x1 + x2) // 2, float(np.hypot(x2 - x1, y2 - y1))))

    def get_bands(lines_with_len: List[Tuple[int, float]]) -> List[Tuple[float, float]]:
        if not lines_with_len:
            return []
        lines_with_len.sort(key=lambda x: x[0])
        clusters: List[List[Tuple[int, float]]] = [[lines_with_len[0]]]
        for item in lines_with_len[1:]:
            if item[0] - clusters[-1][-1][0] <= 12:
                clusters[-1].append(item)
            else:
                clusters.append([item])
        bands: List[Tuple[float, float]] = []
        for cl in clusters:
            tot_len = sum(item[1] for item in cl)
            weights = [item[1] for item in cl]
            coords = [item[0] for item in cl]
            bands.append((float(np.average(coords, weights=weights)), tot_len))
        return bands

    h_bands = get_bands(h_lines)
    v_bands = get_bands(v_lines)

    # In Diablo Immortal inventory grids, cells are square.
    # Collect all differences between consecutive bands in both directions
    diffs: List[float] = []
    for bands in (h_bands, v_bands):
        for i in range(len(bands) - 1):
            d = bands[i + 1][0] - bands[i][0]
            if min_cell_size <= d <= max_cell_size:
                diffs.append(d)

    if not diffs:
        for bands in (h_bands, v_bands):
            for i in range(len(bands) - 1):
                d = bands[i + 1][0] - bands[i][0]
                if min_cell_size * 2 <= d <= max_cell_size * 2:
                    diffs.append(d / 2)

    step = float(np.median(diffs)) if diffs else 116.0

    def solve_lattice(
        bands: List[Tuple[float, float]], total_dim: int, grid_step: float
    ) -> List[Tuple[int, int]]:
        if not bands:
            return []

        coords = [b[0] for b in bands]
        weights = [b[1] for b in bands]

        angles = [c * 2 * np.pi / grid_step for c in coords]
        sin_sum = sum(w * np.sin(a) for w, a in zip(weights, angles))
        cos_sum = sum(w * np.cos(a) for w, a in zip(weights, angles))
        mean_angle = np.arctan2(sin_sum, cos_sum)
        if mean_angle < 0:
            mean_angle += 2 * np.pi
        best_phase = (mean_angle * grid_step / (2 * np.pi)) % grid_step

        valid_coords: List[float] = []
        for c in coords:
            diff = abs((c - best_phase + grid_step / 2) % grid_step - grid_step / 2)
            if diff <= 15:
                valid_coords.append(c)

        if not valid_coords:
            valid_coords = [best_phase]

        ref = valid_coords[0]
        start_k = int(np.floor(-ref / grid_step))
        end_k = int(np.ceil((total_dim - ref) / grid_step))
        lattice = sorted([ref + k * grid_step for k in range(start_k, end_k + 1)])

        adjusted: List[float] = []
        for l in lattice:
            close_c = [c for c in valid_coords if abs(c - l) <= 12]
            if close_c:
                adjusted.append(float(np.mean(close_c)))
            else:
                adjusted.append(l)

        intervals: List[Tuple[int, int]] = []
        for i in range(len(adjusted) - 1):
            p1 = adjusted[i]
            p2 = adjusted[i + 1]
            overlap = min(total_dim, p2) - max(0, p1)
            if (
                overlap >= min_fill_ratio * grid_step
                and p1 >= -0.15 * grid_step
                and p2 <= total_dim + 0.15 * grid_step
            ):
                intervals.append(
                    (max(0, int(round(p1))), min(total_dim, int(round(p2))))
                )
        return intervals

    rows = solve_lattice(h_bands, height, step)
    cols = solve_lattice(v_bands, width, step)
    return rows, cols, step


def extract_rank(cell: np.ndarray) -> str:
    """
    Extracts the rank string (e.g. 'R1', 'R5', 'R10') from the bottom-left of the cell.
    Uses color masking to isolate the characteristic gold/tan font color.
    """
    h, w, _ = cell.shape
    crop = cell[int(h * 0.68) : int(h * 0.96), int(w * 0.05) : int(w * 0.50)]
    b, g, r = cv2.split(crop)

    # Gold/tan rank text: warm color with low blue
    mask = (
        (r.astype(int) - b.astype(int) > 20)
        & (r.astype(int) > 80)
        & (g.astype(int) > 65)
        & (r.astype(int) >= g.astype(int) - 10)
    ).astype(np.uint8) * 255

    num_labels, labels, stats, _ = cv2.connectedComponentsWithStats(mask)
    clean_mask = np.zeros_like(mask)
    valid_comps = []
    for i in range(1, num_labels):
        cx, cy, cw, ch, area = stats[i]
        # Ignore components touching the top boundary (bleeding from gem artwork)
        if cy > 1 and ch >= 8 and area >= 20:
            clean_mask[labels == i] = 255
            valid_comps.append((cx, cy, cw, ch, area))

    if np.sum(clean_mask) == 0 or not valid_comps:
        return "R1"

    big = cv2.resize(clean_mask, (0, 0), fx=4, fy=4, interpolation=cv2.INTER_NEAREST)
    inv = 255 - big
    pad = cv2.copyMakeBorder(inv, 20, 20, 20, 20, cv2.BORDER_CONSTANT, value=255)

    txt = pytesseract.image_to_string(
        pad, config="--psm 8 -c tessedit_char_whitelist=R0123456789"
    ).strip()
    if not txt:
        txt = pytesseract.image_to_string(
            pad, config="--psm 7 -c tessedit_char_whitelist=R0123456789"
        ).strip()

    txt = txt.replace(" ", "")
    # Parse rank number
    digits = "".join([c for c in txt if c.isdigit()])
    if not digits:
        digits = "1"
    return f"R{digits}"


def extract_quantity(cell: np.ndarray) -> int:
    """
    Extracts the quantity integer from the bottom-right of the cell.
    In Diablo Immortal, if quantity is 1, no number is displayed.
    Uses brightness and low-saturation filtering to isolate the bright white/cream digits.
    """
    h, w, _ = cell.shape
    crop = cell[int(h * 0.68) : int(h * 0.96), int(w * 0.55) : int(w * 0.95)]
    b, g, r = cv2.split(crop)

    # Quantity digits are bright white/cream
    mask = (
        (r.astype(int) > 175) & (g.astype(int) > 170) & (b.astype(int) > 160)
    ).astype(np.uint8) * 255

    num_labels, labels, stats, _ = cv2.connectedComponentsWithStats(mask)
    clean_mask = np.zeros_like(mask)
    for i in range(1, num_labels):
        _, _, _, ch, area = stats[i]
        if ch >= 8 and area >= 20:
            clean_mask[labels == i] = 255

    if np.sum(clean_mask) == 0:
        return 1

    big = cv2.resize(clean_mask, (0, 0), fx=4, fy=4, interpolation=cv2.INTER_NEAREST)
    inv = 255 - big
    pad = cv2.copyMakeBorder(inv, 20, 20, 20, 20, cv2.BORDER_CONSTANT, value=255)

    txt = pytesseract.image_to_string(
        pad, config="--psm 10 -c tessedit_char_whitelist=0123456789"
    ).strip()
    if not txt.isdigit():
        txt = pytesseract.image_to_string(
            pad, config="--psm 8 -c tessedit_char_whitelist=0123456789"
        ).strip()
    if not txt.isdigit():
        txt = pytesseract.image_to_string(
            pad, config="--psm 7 -c tessedit_char_whitelist=0123456789"
        ).strip()

    if txt.isdigit():
        return int(txt)
    return 1


def get_gem_core(cell: np.ndarray, target_size: Tuple[int, int] = (64, 64)) -> np.ndarray:
    """
    Extracts the central gem region of a cell, avoiding text and tradeable icons.
    Resizes to a standardized target size for template correlation.
    """
    h, w, _ = cell.shape
    core = cell[int(h * 0.12) : int(h * 0.72), int(w * 0.15) : int(w * 0.85)]
    return cv2.resize(core, target_size, interpolation=cv2.INTER_AREA)


class GemDatabase:
    """
    Manages known gem templates in baseimage/ directory and registers unknown gems.
    """

    def __init__(
        self,
        baseimage_dir: Path,
        threshold: float = 0.80,
        generate_unknown_dir: Optional[Path] = None,
    ):
        self.baseimage_dir = baseimage_dir
        self.threshold = threshold
        self.generate_unknown_dir = generate_unknown_dir

        if self.generate_unknown_dir is not None:
            self.generate_unknown_dir.mkdir(parents=True, exist_ok=True)

        self.known_gems: List[Dict[str, Any]] = []
        self._load_existing_gems()

    def _load_existing_gems(self) -> None:
        valid_exts = {".png", ".jpg", ".jpeg", ".webp"}
        if not self.baseimage_dir.exists():
            return

        for file_path in sorted(self.baseimage_dir.iterdir()):
            if file_path.suffix.lower() in valid_exts:
                img = cv2.imread(str(file_path))
                if img is not None:
                    core = get_gem_core(img)
                    self.known_gems.append(
                        {
                            "name": file_path.stem,
                            "path": file_path,
                            "core": core,
                        }
                    )

    def identify_or_register(
        self, cell: np.ndarray, position: Optional[Tuple[int, int]] = None
    ) -> str:
        """
        Matches cell against known gems.
        If matched, returns gem name (e.g. 'blood-soaked-jade' or 'ignored-tourmaline').
        If unrecognized:
          - If generate_unknown_dir is provided, saves 'unknown-<random_hex>.png'.
          - Caches in-memory so identical gems in the same scan reuse the same ID.
          - Logs unknown icon and its position (row, col) via standard logging.
        """
        cell_core = get_gem_core(cell)

        best_score = -1.0
        best_name: Optional[str] = None

        for item in self.known_gems:
            score = float(
                cv2.matchTemplate(cell_core, item["core"], cv2.TM_CCOEFF_NORMED)[0][0]
            )
            if score > best_score:
                best_score = score
                best_name = item["name"]

        if best_score >= self.threshold and best_name is not None:
            return best_name

        # Unrecognized gem: generate unique random name
        random_id = secrets.token_hex(4)
        new_name = f"unknown-{random_id}"

        pos_str = (
            f" at row {position[0] + 1}, col {position[1] + 1}"
            if position is not None
            else ""
        )

        # Save to disk if generate_unknown_dir is configured, otherwise log a warning
        save_path = None
        if self.generate_unknown_dir is not None:
            save_path = self.generate_unknown_dir / f"{new_name}.png"
            cv2.imwrite(str(save_path), cell)
            logger.info("Saved unknown icon %s%s -> %s", new_name, pos_str, save_path)
        else:
            logger.warning("Found unknown icon %s%s", new_name, pos_str)

        # Register in memory so duplicates in the same scan reuse the same unknown ID
        self.known_gems.append(
            {
                "name": new_name,
                "path": save_path,
                "core": cell_core,
            }
        )
        return new_name


def scan_inventory(
    image_path: Path,
    baseimage_dir: Path,
    threshold: float = 0.80,
    generate_unknown_dir: Optional[Path] = None,
    verbose: bool = False,
) -> List[Dict[str, Any]]:
    """
    Processes the inventory screenshot and returns detected legendary gems.
    Any icon matching an 'ignored-*' template is filtered out.
    """
    img = cv2.imread(str(image_path))
    if img is None:
        raise FileNotFoundError(f"Could not open or read image file: {image_path}")

    row_intervals, col_intervals, step = detect_grid_cells(img)
    logger.info(
        "Detected grid step: %.1fpx, %d rows x %d cols (%d total cells)",
        step,
        len(row_intervals),
        len(col_intervals),
        len(row_intervals) * len(col_intervals),
    )

    db = GemDatabase(
        baseimage_dir=baseimage_dir,
        threshold=threshold,
        generate_unknown_dir=generate_unknown_dir,
    )

    results: List[Dict[str, Any]] = []
    for r_idx, (y1, y2) in enumerate(row_intervals):
        for c_idx, (x1, x2) in enumerate(col_intervals):
            cell = img[y1:y2, x1:x2]
            gem_name = db.identify_or_register(cell, position=(r_idx, c_idx))

            # Icons with prefix 'ignored-' are not legendary gems and must be ignored
            if gem_name.startswith("ignored-"):
                logger.info(
                    "Ignoring non-legendary icon: %s at row %d, col %d",
                    gem_name,
                    r_idx + 1,
                    c_idx + 1,
                )
                continue

            rank = extract_rank(cell)
            quantity = extract_quantity(cell)

            results.append(
                {
                    "gem": gem_name,
                    "rank": rank,
                    "quantity": quantity,
                }
            )

    return results


def main() -> None:
    parser = argparse.ArgumentParser(
        description="Scan Diablo Immortal inventory screenshots for legendary gems."
    )
    parser.add_argument(
        "image",
        type=Path,
        help="Path to the inventory screenshot image.",
    )
    parser.add_argument(
        "-b",
        "--baseimage",
        "--baseimage-dir",
        dest="baseimage_dir",
        type=Path,
        default=Path("baseimage"),
        help="Directory containing configured gem icon images (default: ./baseimage).",
    )
    parser.add_argument(
        "--generate-unknown",
        dest="generate_unknown_dir",
        type=Path,
        default=None,
        metavar="DIRECTORY",
        help="Directory where newly discovered unknown gem images should be saved.",
    )
    parser.add_argument(
        "-t",
        "--threshold",
        type=float,
        default=0.80,
        help="Similarity threshold for gem matching (0.0 to 1.0, default: 0.80).",
    )
    parser.add_argument(
        "-f",
        "--format",
        choices=["jsonl", "json"],
        default="jsonl",
        help="Output format: 'jsonl' (default, JSON per line) or 'json' (JSON array).",
    )
    parser.add_argument(
        "-v",
        "--verbose",
        action="store_true",
        help="Enable verbose diagnostic logging (DEBUG level).",
    )

    args = parser.parse_args()

    log_level = logging.DEBUG if args.verbose else logging.INFO
    logging.basicConfig(
        level=log_level,
        format="%(levelname)s: %(message)s",
        stream=sys.stderr,
    )

    if not args.image.exists():
        logger.error("Image file '%s' not found.", args.image)
        sys.exit(1)

    try:
        results = scan_inventory(
            image_path=args.image,
            baseimage_dir=args.baseimage_dir,
            threshold=args.threshold,
            generate_unknown_dir=args.generate_unknown_dir,
            verbose=args.verbose,
        )
    except Exception as e:
        logger.error("Error processing inventory: %s", e)
        sys.exit(1)

    if args.format == "jsonl":
        for item in results:
            sys.stdout.write(json.dumps(item) + "\n")
    else:
        sys.stdout.write(json.dumps(results, indent=2) + "\n")


if __name__ == "__main__":
    main()
