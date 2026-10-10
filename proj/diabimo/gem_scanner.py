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
import os
from pathlib import Path
import secrets
import sys
from typing import Any, Dict, List, Optional, Tuple

import cv2
import numpy as np
import pytesseract


def detect_grid_cells(
    img: np.ndarray,
    min_cell_size: int = 60,
    max_cell_size: int = 350,
    min_fill_ratio: float = 0.85,
) -> Tuple[List[Tuple[int, int]], List[Tuple[int, int]], float]:
    """
    Detects full grid cell intervals (rows and cols) in an inventory screenshot.
    Uses Hough line transform and separator band clustering to locate the exact
    grid boundaries, automatically interpolating missing lines and filtering out
    partial cells cut off at the screenshot edges.
    """
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    height, width = gray.shape

    edges = cv2.Canny(gray, 30, 100)
    min_line_len = int(min(height, width) * 0.35)
    lines = cv2.HoughLinesP(
        edges, 1, np.pi / 180, threshold=80, minLineLength=min_line_len, maxLineGap=20
    )

    h_lines: List[Tuple[int, float]] = []
    v_lines: List[Tuple[int, float]] = []

    if lines is not None:
        for x1, y1, x2, y2 in lines.reshape(-1, 4):
            length = float(np.hypot(x2 - x1, y2 - y1))
            if abs(y2 - y1) <= 2:
                h_lines.append(((y1 + y2) // 2, length))
            elif abs(x2 - x1) <= 2:
                v_lines.append(((x1 + x2) // 2, length))

    def extract_separators(
        lines_with_len: List[Tuple[int, float]], total_dim: int
    ) -> Tuple[List[Tuple[int, int]], float]:
        if not lines_with_len:
            return [], float(min_cell_size)

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
            mean_coord = float(np.average(coords, weights=weights))
            bands.append((mean_coord, tot_len))

        max_support = max(b[1] for b in bands) if bands else 0.0
        strong_bands = [b[0] for b in bands if b[1] >= max_support * 0.25]

        diffs = [
            strong_bands[i + 1] - strong_bands[i] for i in range(len(strong_bands) - 1)
        ]
        valid_diffs = [d for d in diffs if min_cell_size <= d <= max_cell_size]
        if not valid_diffs:
            valid_diffs = [d / 2 for d in diffs if min_cell_size * 2 <= d <= max_cell_size * 2]
        step = float(np.median(valid_diffs)) if valid_diffs else float(min_cell_size)

        filtered: List[float] = [strong_bands[0]]
        for coord in strong_bands[1:]:
            d = coord - filtered[-1]
            k = int(round(d / step))
            if k >= 1 and abs(d - k * step) <= 15:
                for _ in range(1, k):
                    filtered.append(filtered[-1] + step)
                filtered.append(coord)

        if filtered[0] >= min_fill_ratio * step:
            filtered.insert(0, 0.0)

        if (total_dim - filtered[-1]) >= min_fill_ratio * step:
            filtered.append(float(total_dim))

        intervals: List[Tuple[int, int]] = []
        for i in range(len(filtered) - 1):
            p1 = max(0, int(round(filtered[i])))
            p2 = min(total_dim, int(round(filtered[i + 1])))
            if (p2 - p1) >= min_fill_ratio * step:
                intervals.append((p1, p2))

        return intervals, step

    rows, h_step = extract_separators(h_lines, height)
    cols, v_step = extract_separators(v_lines, width)
    return rows, cols, (h_step + v_step) / 2


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
        if ch >= 10 and area >= 25:
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
    Manages known gem templates in a directory and registers unknown gems.
    """

    def __init__(self, gems_dir: Path, threshold: float = 0.80):
        self.gems_dir = gems_dir
        self.threshold = threshold
        self.gems_dir.mkdir(parents=True, exist_ok=True)
        self.known_gems: List[Dict[str, Any]] = []
        self._load_existing_gems()

    def _load_existing_gems(self) -> None:
        valid_exts = {".png", ".jpg", ".jpeg", ".webp"}
        for file_path in sorted(self.gems_dir.iterdir()):
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

    def identify_or_register(self, cell: np.ndarray) -> str:
        """
        Matches cell against known gems.
        If matched, returns gem name.
        If unrecognized, saves the cell image as 'unknown-<random_hex>.png' into
        gems_dir, adds it to known gems, and returns the generated name.
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

        # Unrecognized gem: generate unique random name and save
        random_id = secrets.token_hex(4)
        new_name = f"unknown-{random_id}"
        save_path = self.gems_dir / f"{new_name}.png"
        cv2.imwrite(str(save_path), cell)

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
    gems_dir: Path,
    threshold: float = 0.80,
    verbose: bool = False,
) -> List[Dict[str, Any]]:
    """
    Processes the inventory screenshot and returns detected gems with rank and quantity.
    """
    img = cv2.imread(str(image_path))
    if img is None:
        raise FileNotFoundError(f"Could not open or read image file: {image_path}")

    row_intervals, col_intervals, step = detect_grid_cells(img)
    if verbose:
        sys.stderr.write(
            f"Detected grid step: {step:.1f}px, "
            f"{len(row_intervals)} rows x {len(col_intervals)} cols "
            f"({len(row_intervals) * len(col_intervals)} total cells)\n"
        )

    db = GemDatabase(gems_dir=gems_dir, threshold=threshold)

    results: List[Dict[str, Any]] = []
    for r_idx, (y1, y2) in enumerate(row_intervals):
        for c_idx, (x1, x2) in enumerate(col_intervals):
            cell = img[y1:y2, x1:x2]
            gem_name = db.identify_or_register(cell)
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
        "-d",
        "--gems-dir",
        type=Path,
        default=Path("gems"),
        help="Directory containing known gem icon images (default: ./gems).",
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
        help="Print diagnostic progress to stderr.",
    )

    args = parser.parse_args()

    if not args.image.exists():
        sys.stderr.write(f"Error: Image file '{args.image}' not found.\n")
        sys.exit(1)

    try:
        results = scan_inventory(
            image_path=args.image,
            gems_dir=args.gems_dir,
            threshold=args.threshold,
            verbose=args.verbose,
        )
    except Exception as e:
        sys.stderr.write(f"Error processing inventory: {e}\n")
        sys.exit(1)

    if args.format == "jsonl":
        for item in results:
            sys.stdout.write(json.dumps(item) + "\n")
    else:
        sys.stdout.write(json.dumps(results, indent=2) + "\n")


if __name__ == "__main__":
    main()
