#!/usr/bin/env python3
"""
Diablo Immortal Legendary Gem Inventory Scanner CLI.
Extracts gem name, rank, and quantity from inventory screenshots into JSONL format.
"""

import os
import sys
import uuid
import json
import argparse
from pathlib import Path
from typing import List, Tuple, Dict, Optional, Any

import cv2
import numpy as np
import pytesseract


def detect_cells(img: np.ndarray, min_pitch_ratio: float = 0.85) -> List[Tuple[np.ndarray, Tuple[int, int, int, int]]]:
    """
    Detects regular grid cells from a cropped screenshot.
    Automatically filters out cut-off partial cells at the borders.
    Returns a list of (cell_image, (x1, y1, x2, y2)).
    """
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    H, W = gray.shape

    # Profile the 90th percentile brightness along rows and columns
    # In Diablo Immortal inventory, separator grooves between cells are consistently dark.
    row_p90 = np.array([np.percentile(gray[y, :], 90) for y in range(H)])
    col_p90 = np.array([np.percentile(gray[:, x], 90) for x in range(W)])

    def get_grooves(profile: np.ndarray, min_cell_size: int = 50) -> List[int]:
        p_min, p_max = float(np.min(profile)), float(np.max(profile))
        thresh = min(max(p_min + (p_max - p_min) * 0.15, 25.0), 50.0)
        is_groove = profile <= thresh
        grooves = []
        in_groove = False
        start = 0
        for i, val in enumerate(is_groove):
            if val and not in_groove:
                in_groove = True
                start = i
            elif not val and in_groove:
                in_groove = False
                grooves.append((start + i) // 2)
        if in_groove:
            grooves.append((start + len(is_groove)) // 2)

        merged: List[int] = []
        for g in grooves:
            if not merged:
                merged.append(g)
            elif g - merged[-1] < min_cell_size:
                if profile[g] < profile[merged[-1]]:
                    merged[-1] = g
            else:
                merged.append(g)
        return merged

    y_gaps = get_grooves(row_p90)
    x_gaps = get_grooves(col_p90)

    # Estimate expected cell pitch
    y_pitch = float(np.median(np.diff(y_gaps))) if len(y_gaps) > 1 else 115.0
    x_pitch = float(np.median(np.diff(x_gaps))) if len(x_gaps) > 1 else 118.0

    # Determine row intervals
    row_bounds: List[Tuple[int, int]] = []
    if y_gaps and y_gaps[0] >= min_pitch_ratio * y_pitch:
        row_bounds.append((0, y_gaps[0]))
    for i in range(len(y_gaps) - 1):
        if (y_gaps[i + 1] - y_gaps[i]) >= min_pitch_ratio * y_pitch:
            row_bounds.append((y_gaps[i], y_gaps[i + 1]))
    if y_gaps and (H - y_gaps[-1]) >= min_pitch_ratio * y_pitch:
        row_bounds.append((y_gaps[-1], H))

    # Determine col intervals
    col_bounds: List[Tuple[int, int]] = []
    if x_gaps and x_gaps[0] >= min_pitch_ratio * x_pitch:
        col_bounds.append((0, x_gaps[0]))
    for j in range(len(x_gaps) - 1):
        if (x_gaps[j + 1] - x_gaps[j]) >= min_pitch_ratio * x_pitch:
            col_bounds.append((x_gaps[j], x_gaps[j + 1]))
    if x_gaps and (W - x_gaps[-1]) >= min_pitch_ratio * x_pitch:
        col_bounds.append((x_gaps[-1], W))

    cells = []
    for y1, y2 in row_bounds:
        for x1, x2 in col_bounds:
            # Crop cell with a small margin to exclude groove edges
            pad = 2
            cy1, cy2 = min(H, y1 + pad), max(0, y2 - pad)
            cx1, cx2 = min(W, x1 + pad), max(0, x2 - pad)
            if cy2 > cy1 and cx2 > cx1:
                cell_img = img[cy1:cy2, cx1:cx2].copy()
                cells.append((cell_img, (cx1, cy1, cx2, cy2)))

    return cells


def extract_gem_core(cell_img: np.ndarray, target_size: Tuple[int, int] = (64, 64)) -> np.ndarray:
    """
    Extracts the central core of the gem icon.
    This avoids the tradeable scale icon at top-left, the rank at bottom-left,
    and quantity at bottom-right.
    """
    h, w = cell_img.shape[:2]
    core = cell_img[int(h * 0.12):int(h * 0.75), int(w * 0.15):int(w * 0.85)]
    return cv2.resize(core, target_size, interpolation=cv2.INTER_AREA)


def extract_rank(cell_img: np.ndarray) -> str:
    """
    Extracts the gem rank (e.g. 'R1', 'R5') from the bottom-left of the cell.
    Uses localized template correlation with Tesseract OCR fallback.
    """
    h, w = cell_img.shape[:2]
    # Rank area is in bottom-left region
    search_region = cell_img[-35:-5, 0:min(50, w)]
    gray = cv2.cvtColor(search_region, cv2.COLOR_BGR2GRAY)

    # Threshold for the golden rank text
    _, thresh = cv2.threshold(gray, 75, 255, cv2.THRESH_BINARY)
    inv = cv2.bitwise_not(thresh)
    scaled = cv2.resize(inv, (0, 0), fx=6, fy=6, interpolation=cv2.INTER_NEAREST)
    padded = cv2.copyMakeBorder(scaled, 30, 30, 30, 30, cv2.BORDER_CONSTANT, value=255)

    # First try full OCR on the rank area
    for psm in [7, 8, 6]:
        txt = pytesseract.image_to_string(
            padded,
            config=f'--psm {psm} -c tessedit_char_whitelist=R0123456789'
        ).strip().replace(' ', '')
        if txt.startswith('R') and len(txt) >= 2 and txt[1:].isdigit():
            return txt

    # If full OCR did not return R+number, check if '5' or other digit is present on the right
    # Digit area is right half of the rank search region
    d_gray = gray[:, int(gray.shape[1] * 0.45):]
    _, d_thresh = cv2.threshold(d_gray, 75, 255, cv2.THRESH_BINARY)
    d_inv = cv2.bitwise_not(d_thresh)
    d_scaled = cv2.resize(d_inv, (0, 0), fx=8, fy=8, interpolation=cv2.INTER_NEAREST)
    d_padded = cv2.copyMakeBorder(d_scaled, 30, 30, 30, 30, cv2.BORDER_CONSTANT, value=255)

    for psm in [10, 8, 7]:
        d_txt = pytesseract.image_to_string(
            d_padded,
            config=f'--psm {psm} -c tessedit_char_whitelist=0123456789'
        ).strip()
        if d_txt and d_txt.isdigit():
            return f"R{d_txt}"

    # Default baseline rank for Diablo Immortal legendary gems
    return "R1"


def extract_quantity(cell_img: np.ndarray) -> int:
    """
    Extracts the quantity count from the bottom-right of the cell.
    If no quantity number is displayed, quantity is 1.
    """
    h, w = cell_img.shape[:2]
    # Quantity area is in bottom-right corner
    q_strip = cell_img[-28:-8, -32:-6]
    gray = cv2.cvtColor(q_strip, cv2.COLOR_BGR2GRAY)

    # If the region lacks bright pixels (max < 135 or sparse bright pixels), it's empty -> quantity 1
    if np.max(gray) < 135:
        return 1

    _, thresh = cv2.threshold(gray, 135, 255, cv2.THRESH_BINARY)
    if np.count_nonzero(thresh) < 15:
        return 1

    # Invert and upscale for OCR
    inv = cv2.bitwise_not(thresh)
    scaled = cv2.resize(inv, (0, 0), fx=8, fy=8, interpolation=cv2.INTER_NEAREST)
    padded = cv2.copyMakeBorder(scaled, 40, 40, 40, 40, cv2.BORDER_CONSTANT, value=255)

    for psm in [8, 10, 7, 6]:
        txt = pytesseract.image_to_string(
            padded,
            config=f'--psm {psm} -c tessedit_char_whitelist=0123456789'
        ).strip()
        if txt and txt.isdigit():
            return int(txt)

    return 1


class GemDatabase:
    """Manages known gem icon templates and handles matching or saving unknowns."""

    def __init__(self, gems_dir: Path, threshold: float = 0.85):
        self.gems_dir = gems_dir
        self.threshold = threshold
        self.known_gems: Dict[str, np.ndarray] = {}
        self.gems_dir.mkdir(parents=True, exist_ok=True)
        self.load_templates()

    def load_templates(self) -> None:
        """Loads all existing templates from the gems directory."""
        valid_exts = {'.png', '.jpg', '.jpeg', '.webp'}
        for file in sorted(self.gems_dir.iterdir()):
            if file.suffix.lower() in valid_exts:
                tmpl_img = cv2.imread(str(file))
                if tmpl_img is not None:
                    # If full cell was saved, extract core; otherwise use resized template
                    core = extract_gem_core(tmpl_img) if tmpl_img.shape[0] > 70 else cv2.resize(tmpl_img, (64, 64))
                    self.known_gems[file.stem] = core

    def match_gem(self, core: np.ndarray) -> Tuple[Optional[str], float]:
        """Finds the best matching known gem for the given gem core."""
        best_name = None
        best_score = -1.0

        for name, tmpl in self.known_gems.items():
            res = cv2.matchTemplate(core, tmpl, cv2.TM_CCOEFF_NORMED)[0][0]
            if res > best_score:
                best_score = float(res)
                best_name = name

        if best_score >= self.threshold and best_name is not None:
            return best_name, best_score

        return None, best_score

    def register_unknown(self, cell_img: np.ndarray, core: np.ndarray) -> str:
        """Saves an unknown gem image to the gems directory and registers it in-memory."""
        rand_id = uuid.uuid4().hex[:8]
        gem_name = f"unknown-{rand_id}"
        save_path = self.gems_dir / f"{gem_name}.png"
        cv2.imwrite(str(save_path), cell_img)
        # Add to known templates so subsequent identical gems in the same scan match it
        self.known_gems[gem_name] = core
        return gem_name


def scan_inventory(
    image_path: str,
    gems_dir: str,
    threshold: float = 0.85,
    save_unknowns: bool = True,
    debug_path: Optional[str] = None
) -> List[Dict[str, Any]]:
    """
    Scans a Diablo Immortal screenshot and returns list of gem records.
    """
    img = cv2.imread(image_path)
    if img is None:
        raise FileNotFoundError(f"Failed to load image from {image_path}")

    db = GemDatabase(Path(gems_dir), threshold=threshold)
    cells = detect_cells(img)

    results = []
    debug_img = img.copy() if debug_path else None

    for idx, (cell, bbox) in enumerate(cells):
        core = extract_gem_core(cell)
        matched_name, score = db.match_gem(core)

        if matched_name:
            gem_name = matched_name
        elif save_unknowns:
            gem_name = db.register_unknown(cell, core)
        else:
            gem_name = "unknown"

        rank = extract_rank(cell)
        quantity = extract_quantity(cell)

        item = {
            "gem": gem_name,
            "rank": rank,
            "quantity": quantity
        }
        results.append(item)

        if debug_img is not None:
            x1, y1, x2, y2 = bbox
            cv2.rectangle(debug_img, (x1, y1), (x2, y2), (0, 255, 0), 2)
            label = f"{gem_name} {rank}x{quantity}"
            cv2.putText(debug_img, label, (x1 + 4, y1 + 18), cv2.FONT_HERSHEY_SIMPLEX, 0.35, (0, 255, 255), 1)

    if debug_img is not None and debug_path:
        cv2.imwrite(debug_path, debug_img)

    return results


def main():
    parser = argparse.ArgumentParser(
        description="Scan Diablo Immortal inventory screenshot and output gem items in JSONL format."
    )
    parser.add_argument("image", help="Path to inventory screenshot (e.g. sample/a.jpeg)")
    parser.add_argument(
        "-d", "--gems-dir",
        default="known_gems",
        help="Directory containing known gem icon templates (default: known_gems)"
    )
    parser.add_argument(
        "-t", "--threshold",
        type=float,
        default=0.85,
        help="Similarity threshold for gem matching [0.0 - 1.0] (default: 0.85)"
    )
    parser.add_argument(
        "--no-save-unknowns",
        action="store_true",
        help="Do not save newly detected unknown gems into gems-dir"
    )
    parser.add_argument(
        "-o", "--output",
        help="Write output JSONL to specified file instead of stdout"
    )
    parser.add_argument(
        "--debug-img",
        help="Path to save annotated debug image with detected boxes and labels"
    )

    args = parser.parse_args()

    results = scan_inventory(
        image_path=args.image,
        gems_dir=args.gems_dir,
        threshold=args.threshold,
        save_unknowns=not args.no_save_unknowns,
        debug_path=args.debug_img
    )

    out_lines = [json.dumps(item) for item in results]
    output_text = "\n".join(out_lines)

    if args.output:
        Path(args.output).write_text(output_text + "\n", encoding="utf-8")
    else:
        for line in out_lines:
            print(line)


if __name__ == "__main__":
    main()
