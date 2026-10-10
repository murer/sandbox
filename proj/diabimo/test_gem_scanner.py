#!/usr/bin/env python3
"""
Unit and integration tests for Diablo Immortal Gem Scanner CLI.
"""

import json
from pathlib import Path
import re
import shutil
import subprocess
import unittest

import cv2
import numpy as np

from gem_scanner import (
    GemDatabase,
    detect_grid_cells,
    extract_quantity,
    extract_rank,
    get_gem_core,
    scan_inventory,
)

SAMPLE_IMAGE = Path(__file__).parent / "sample" / "a.jpeg"
SAMPLE_LESS_ROWS = Path(__file__).parent / "sample" / "sample-less-rows.jpeg"
SAMPLE_NON_LEGENDARY = Path(__file__).parent / "sample" / "sample-non-legendary-gems.jpeg"
TARGET_DIR = Path(__file__).parent / "target" / "tests"


class TestGemScanner(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        if not SAMPLE_IMAGE.exists():
            raise FileNotFoundError(f"Sample image not found: {SAMPLE_IMAGE}")
        cls.img = cv2.imread(str(SAMPLE_IMAGE))
        TARGET_DIR.mkdir(parents=True, exist_ok=True)

    @classmethod
    def tearDownClass(cls):
        if TARGET_DIR.exists():
            shutil.rmtree(TARGET_DIR, ignore_errors=True)

    def test_detect_grid_cells(self):
        """Verify grid detection identifies 6 full rows and 6 full columns, ignoring partial cuts."""
        rows, cols, step = detect_grid_cells(self.img)
        self.assertEqual(len(rows), 6, f"Expected 6 full rows, got {len(rows)}")
        self.assertEqual(len(cols), 6, f"Expected 6 full cols, got {len(cols)}")
        self.assertGreaterEqual(step, 100.0)
        self.assertLessEqual(step, 130.0)

        # Verify partial row 7 (> 706px) is excluded
        last_row_bottom = rows[-1][1]
        self.assertLessEqual(last_row_bottom, 715)

    def test_detect_grid_cells_less_rows(self):
        """Verify grid detection on cropped sample with only 2 full rows and 6 columns."""
        if not SAMPLE_LESS_ROWS.exists():
            return
        img_less = cv2.imread(str(SAMPLE_LESS_ROWS))
        rows, cols, step = detect_grid_cells(img_less)
        self.assertEqual(len(rows), 2, f"Expected 2 full rows, got {len(rows)}")
        self.assertEqual(len(cols), 6, f"Expected 6 full cols, got {len(cols)}")

        # Verify R3 is extracted from top-left cell
        cell_0_0 = img_less[rows[0][0] : rows[0][1], cols[0][0] : cols[0][1]]
        self.assertEqual(extract_rank(cell_0_0), "R3")

    def test_detect_grid_cells_non_legendary(self):
        """Verify grid detection on sample with 5 full rows x 6 columns including normal gems."""
        if not SAMPLE_NON_LEGENDARY.exists():
            return
        img_nonleg = cv2.imread(str(SAMPLE_NON_LEGENDARY))
        rows, cols, step = detect_grid_cells(img_nonleg)
        self.assertEqual(len(rows), 5, f"Expected 5 full rows, got {len(rows)}")
        self.assertEqual(len(cols), 6, f"Expected 6 full cols, got {len(cols)}")

        # Verify Row 2 Col 5 extracts R1 (not R4)
        cell_2_5 = img_nonleg[rows[2][0] : rows[2][1], cols[5][0] : cols[5][1]]
        self.assertEqual(extract_rank(cell_2_5), "R1")
        self.assertEqual(extract_quantity(cell_2_5), 1)

    def test_extract_rank(self):
        """Verify rank extraction for R1 and R5."""
        rows, cols, _ = detect_grid_cells(self.img)

        # Cell (0, 0) is R1
        cell_0_0 = self.img[rows[0][0] : rows[0][1], cols[0][0] : cols[0][1]]
        self.assertEqual(extract_rank(cell_0_0), "R1")

        # Cell (3, 1) is R5
        cell_3_1 = self.img[rows[3][0] : rows[3][1], cols[1][0] : cols[1][1]]
        self.assertEqual(extract_rank(cell_3_1), "R5")

    def test_extract_quantity(self):
        """Verify quantity extraction for various numbers and empty (default 1)."""
        rows, cols, _ = detect_grid_cells(self.img)

        # Cell (0, 0): Qty 2
        cell_0_0 = self.img[rows[0][0] : rows[0][1], cols[0][0] : cols[0][1]]
        self.assertEqual(extract_quantity(cell_0_0), 2)

        # Cell (0, 1): Qty 1 (no quantity text shown)
        cell_0_1 = self.img[rows[0][0] : rows[0][1], cols[1][0] : cols[1][1]]
        self.assertEqual(extract_quantity(cell_0_1), 1)

        # Cell (3, 0): Qty 3
        cell_3_0 = self.img[rows[3][0] : rows[3][1], cols[0][0] : cols[0][1]]
        self.assertEqual(extract_quantity(cell_3_0), 3)

        # Cell (3, 4): Qty 4
        cell_3_4 = self.img[rows[3][0] : rows[3][1], cols[4][0] : cols[4][1]]
        self.assertEqual(extract_quantity(cell_3_4), 4)

        # Cell (4, 4): Qty 5
        cell_4_4 = self.img[rows[4][0] : rows[4][1], cols[4][0] : cols[4][1]]
        self.assertEqual(extract_quantity(cell_4_4), 5)

        # Cell (3, 5): Qty 6
        cell_3_5 = self.img[rows[3][0] : rows[3][1], cols[5][0] : cols[5][1]]
        self.assertEqual(extract_quantity(cell_3_5), 6)

    def test_generate_unknown_parameter(self):
        """Verify --generate-unknown saves files, while omitting it does not save files."""
        out_dir = TARGET_DIR / "gen_unknown"
        if out_dir.exists():
            shutil.rmtree(out_dir)

        # Run WITH generate_unknown and position
        db_with = GemDatabase(
            baseimage_dir=TARGET_DIR / "empty_base",
            threshold=0.80,
            generate_unknown_dir=out_dir,
        )
        rows, cols, _ = detect_grid_cells(self.img)
        cell_0_0 = self.img[rows[0][0] : rows[0][1], cols[0][0] : cols[0][1]]
        name1 = db_with.identify_or_register(cell_0_0, position=(0, 0))

        self.assertTrue(re.match(r"^unknown-[0-9a-f]{8}-01x01$", name1), f"Expected unknown-<hex>-01x01, got {name1}")
        self.assertTrue((out_dir / f"{name1}.png").exists(), "Expected unknown image to be saved")

        # Run WITHOUT generate_unknown
        no_save_dir = TARGET_DIR / "no_save"
        if no_save_dir.exists():
            shutil.rmtree(no_save_dir)

        db_without = GemDatabase(
            baseimage_dir=TARGET_DIR / "empty_base",
            threshold=0.80,
            generate_unknown_dir=None,
        )
        name2 = db_without.identify_or_register(cell_0_0, position=(2, 4))
        self.assertTrue(re.match(r"^unknown-[0-9a-f]{8}-03x05$", name2), f"Expected unknown-<hex>-03x05, got {name2}")
        self.assertFalse(no_save_dir.exists(), "No directory should be created when generate_unknown is None")

        # Run with explicit random_id
        db_custom = GemDatabase(
            baseimage_dir=TARGET_DIR / "empty_base",
            threshold=0.80,
            generate_unknown_dir=None,
        )
        name_custom = db_custom.identify_or_register(cell_0_0, position=(1, 2), random_id="aabbccdd")
        self.assertEqual(name_custom, "unknown-aabbccdd-02x03")

        # Run without position (fallback) on fresh database
        db_fallback = GemDatabase(
            baseimage_dir=TARGET_DIR / "empty_base",
            threshold=0.80,
            generate_unknown_dir=None,
        )
        name3 = db_fallback.identify_or_register(cell_0_0, position=None)
        self.assertTrue(re.match(r"^unknown-[0-9a-f]{8}$", name3), f"Expected unknown-<hex>, got {name3}")

    def test_ignored_prefix_filters_out_cells(self):
        """Verify that gem templates with 'ignored-' prefix are excluded from output and logged."""
        base_dir = TARGET_DIR / "ignored_test_base"
        base_dir.mkdir(parents=True, exist_ok=True)

        img_less = cv2.imread(str(SAMPLE_LESS_ROWS))
        rows, cols, _ = detect_grid_cells(img_less)

        # Cell (1, 4) is blue orb. Save as ignored-blue-orb.png
        blue_orb_cell = img_less[rows[1][0] : rows[1][1], cols[4][0] : cols[4][1]]
        cv2.imwrite(str(base_dir / "ignored-blue-orb.png"), blue_orb_cell)

        # Run CLI with this baseimage to check both output filtering and logging
        proc = subprocess.run(
            [
                "python3",
                "gem_scanner.py",
                str(SAMPLE_LESS_ROWS),
                "-b",
                str(base_dir),
            ],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True,
            check=True,
        )

        lines = proc.stdout.strip().split("\n")
        # Total cells in sample-less-rows is 12 (2 rows x 6 cols).
        # Both Cell (1, 4) and Cell (1, 5) are blue orbs and should be ignored!
        self.assertEqual(len(lines), 10, f"Expected 10 cells (2 ignored), got {len(lines)}")
        for line in lines:
            data = json.loads(line)
            self.assertFalse(data["gem"].startswith("ignored-"), "Ignored gems must not appear in output")

        # Verify logging on stderr
        self.assertIn("Ignoring non-legendary icon: ignored-blue-orb at row 2, col 5", proc.stderr)
        self.assertIn("Ignoring non-legendary icon: ignored-blue-orb at row 2, col 6", proc.stderr)

    def test_configured_gem_matching(self):
        """Verify that known gem icons in baseimage are identified by their kebab-case filename."""
        base_dir = TARGET_DIR / "match_base"
        base_dir.mkdir(parents=True, exist_ok=True)

        rows, cols, _ = detect_grid_cells(self.img)
        cell_0_0 = self.img[rows[0][0] : rows[0][1], cols[0][0] : cols[0][1]]
        cv2.imwrite(str(base_dir / "frozen-heart.png"), cell_0_0)

        results = scan_inventory(
            image_path=SAMPLE_IMAGE,
            baseimage_dir=base_dir,
            threshold=0.80,
        )

        # Cell (0, 0) should be recognized as frozen-heart
        self.assertEqual(results[0]["gem"], "frozen-heart")

    def test_cli_execution_with_stderr_warning_when_not_saving(self):
        """Verify CLI outputs JSONL on stdout and warns on stderr with position when unknown icons are found."""
        proc = subprocess.run(
            [
                "python3",
                "gem_scanner.py",
                str(SAMPLE_LESS_ROWS),
                "-b",
                str(TARGET_DIR / "empty_base"),
            ],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True,
            check=True,
        )

        # stdout should have 12 JSONL lines
        lines = proc.stdout.strip().split("\n")
        self.assertEqual(len(lines), 12)

        # stderr should report finding unknown icons with exact row and col position
        self.assertIn("Found unknown icon", proc.stderr)
        self.assertIn("row 1, col 1", proc.stderr)

    def test_cli_multiple_images(self):
        """Verify CLI can process multiple image files in a single invocation."""
        proc = subprocess.run(
            [
                "python3",
                "gem_scanner.py",
                str(SAMPLE_LESS_ROWS),
                str(SAMPLE_IMAGE),
                "-b",
                str(TARGET_DIR / "empty_base"),
            ],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True,
            check=True,
        )

        lines = proc.stdout.strip().split("\n")
        # 12 cells from sample-less-rows + 36 cells from sample/a.jpeg = 48 total lines
        self.assertEqual(len(lines), 48)
        self.assertIn("Scanning image: ", proc.stderr)

    def test_cli_generate_unknown_creates_row_col_files(self):
        """Verify CLI --generate-unknown creates files formatted as unknown-<row>x<col>-<hex>.png and shares random ID."""
        gen_dir = TARGET_DIR / "cli_gen_unknown"
        if gen_dir.exists():
            shutil.rmtree(gen_dir)

        proc = subprocess.run(
            [
                "python3",
                "gem_scanner.py",
                str(SAMPLE_LESS_ROWS),
                "--generate-unknown",
                str(gen_dir),
                "-b",
                str(TARGET_DIR / "empty_base"),
            ],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True,
            check=True,
        )

        saved_files = list(gen_dir.glob("*.png"))
        self.assertGreater(len(saved_files), 0, "Expected unknown files to be saved")
        for f in saved_files:
            self.assertTrue(
                re.match(r"^unknown-[0-9a-f]{8}-\d{2}x\d{2}\.png$", f.name),
                f"File {f.name} does not match expected unknown-<hex>-<row>x<col>.png pattern",
            )
        # Ensure row 1, col 1 is generated
        row1_col1_matches = [f for f in saved_files if f.name.endswith("-01x01.png")]
        self.assertEqual(len(row1_col1_matches), 1, "Expected exactly one unknown-*-01x01.png file")

        # Ensure all unknown icons from the same input image share the same random ID
        random_ids = {re.match(r"^unknown-([0-9a-f]{8})-\d{2}x\d{2}\.png$", f.name).group(1) for f in saved_files}
        self.assertEqual(len(random_ids), 1, f"Expected all files to share 1 random ID, got: {random_ids}")


if __name__ == "__main__":
    unittest.main()
