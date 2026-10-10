#!/usr/bin/env python3
"""
Unit and integration tests for Diablo Immortal Gem Scanner CLI.
"""

import json
from pathlib import Path
import shutil
import subprocess
import tempfile
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


class TestGemScanner(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        if not SAMPLE_IMAGE.exists():
            raise FileNotFoundError(f"Sample image not found: {SAMPLE_IMAGE}")
        cls.img = cv2.imread(str(SAMPLE_IMAGE))

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

    def test_gem_database_unknown_registration_and_deduplication(self):
        """Verify unknown gems are saved with unknown-<id> and identical gems share the same ID."""
        with tempfile.TemporaryDirectory() as temp_dir:
            gems_dir = Path(temp_dir)
            db = GemDatabase(gems_dir=gems_dir, threshold=0.80)

            rows, cols, _ = detect_grid_cells(self.img)

            # Cell (0, 4) and (0, 5) are both the same purple gem
            cell_0_4 = self.img[rows[0][0] : rows[0][1], cols[4][0] : cols[4][1]]
            cell_0_5 = self.img[rows[0][0] : rows[0][1], cols[5][0] : cols[5][1]]

            id_0_4 = db.identify_or_register(cell_0_4)
            id_0_5 = db.identify_or_register(cell_0_5)

            self.assertTrue(id_0_4.startswith("unknown-"))
            self.assertEqual(id_0_4, id_0_5, "Identical gems must reuse the same unknown ID")

            # Check that an image file was saved
            saved_file = gems_dir / f"{id_0_4}.png"
            self.assertTrue(saved_file.exists())

    def test_gem_database_matching_configured_gem(self):
        """Verify that renaming an unknown gem file allows subsequent recognition by kebab name."""
        with tempfile.TemporaryDirectory() as temp_dir:
            gems_dir = Path(temp_dir)

            rows, cols, _ = detect_grid_cells(self.img)
            cell_0_0 = self.img[rows[0][0] : rows[0][1], cols[0][0] : cols[0][1]]

            # First run: unknown
            db1 = GemDatabase(gems_dir=gems_dir, threshold=0.80)
            initial_id = db1.identify_or_register(cell_0_0)

            # Rename to configured gem name
            old_path = gems_dir / f"{initial_id}.png"
            new_path = gems_dir / "frozen-heart.png"
            old_path.rename(new_path)

            # Second run: reload database and verify it recognizes frozen-heart
            db2 = GemDatabase(gems_dir=gems_dir, threshold=0.80)
            recognized_name = db2.identify_or_register(cell_0_0)
            self.assertEqual(recognized_name, "frozen-heart")

    def test_cli_jsonl_output(self):
        """Verify CLI execution prints valid JSONL lines with correct schema."""
        with tempfile.TemporaryDirectory() as temp_dir:
            proc = subprocess.run(
                [
                    "python3",
                    "gem_scanner.py",
                    str(SAMPLE_IMAGE),
                    "-d",
                    temp_dir,
                    "-f",
                    "jsonl",
                ],
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                text=True,
                check=True,
            )

            lines = proc.stdout.strip().split("\n")
            self.assertEqual(len(lines), 36)

            for line in lines:
                data = json.loads(line)
                self.assertIn("gem", data)
                self.assertIn("rank", data)
                self.assertIn("quantity", data)
                self.assertTrue(data["rank"].startswith("R"))
                self.assertIsInstance(data["quantity"], int)
                self.assertGreaterEqual(data["quantity"], 1)

    def test_cli_json_array_output(self):
        """Verify CLI execution with --format json outputs a valid JSON array."""
        with tempfile.TemporaryDirectory() as temp_dir:
            proc = subprocess.run(
                [
                    "python3",
                    "gem_scanner.py",
                    str(SAMPLE_IMAGE),
                    "-d",
                    temp_dir,
                    "-f",
                    "json",
                ],
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                text=True,
                check=True,
            )

            data = json.loads(proc.stdout)
            self.assertIsInstance(data, list)
            self.assertEqual(len(data), 36)


if __name__ == "__main__":
    unittest.main()
