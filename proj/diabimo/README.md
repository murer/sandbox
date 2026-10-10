# Diablo Immortal Legendary Gem Scanner CLI

A command-line tool to scan inventory screenshots of Diablo Immortal legendary gems, recognizing gem icons, ranks, and quantities, and outputting them in JSON Lines (JSONL) format.

## Features

- **Automated Grid Detection**: Automatically determines cell pitch, row/column boundaries, and ignores partial or cut-off cells at screenshot margins (handles crops with only 2 rows or full inventories).
- **Color-Masked OCR**:
  - **Rank**: Detects gold/tan font in the bottom-left (`R1`, `R2`, `R3`, `R5`, `R10`, etc.).
  - **Quantity**: Detects bright white digits in the bottom-right. Defaults to `1` when quantity text is absent.
- **Baseimage Configuration**: Matches gem centers against the committed `baseimage/` directory using normalized cross-correlation.
- **Ignored Icon Filtering**: Any template in `baseimage/` starting with the `ignored-` prefix (e.g. `ignored-tourmaline.png`, `ignored-citrine.png`) is identified as a non-legendary gem and automatically excluded from the output list.
- **Exporting Unknowns**: Use `--generate-unknown <directory>` to export unrecognized gems as `unknown-<row>-<col>-<random_hex>.png` (where row and col are 1-indexed grid positions).
- **Alerts for Unknowns**: If `--generate-unknown` is not specified, no files are written to disk; instead, unknown icons are reported on `stderr` while keeping `stdout` clean for JSONL consumption.

## Prerequisites

- **Python 3.8+**
- **System dependencies**:
  ```bash
  sudo apt install -y tesseract-ocr
  ```
- **Python dependencies**:
  ```bash
  pip install opencv-python pytesseract numpy
  ```

## Usage

### Basic Scan
Scan a screenshot matching against icons in `baseimage/`:
```bash
./gem_scanner.py sample/a.jpeg
```

### Scan Multiple Screenshots
You can pass multiple images to scan them sequentially in a single invocation:
```bash
./gem_scanner.py sample/sample-less-rows.jpeg sample/a.jpeg
```

### Export Unknown Icons
Export all unrecognized gem icons into a directory so you can configure them:
```bash
./gem_scanner.py --generate-unknown target/unknowns sample/a.jpeg sample/sample-non-legendary-gems.jpeg
```

### Custom Baseimage Directory
Specify an alternate directory for configured gem templates:
```bash
./gem_scanner.py -b /path/to/custom_baseimage sample/a.jpeg
```

### Options
```text
positional arguments:
  images                Paths to one or more inventory screenshot images.

options:
  -h, --help            Show this help message and exit.
  -b, --baseimage, --baseimage-dir BASEIMAGE_DIR
                        Directory containing configured gem icon images (default: ./baseimage).
  --generate-unknown DIRECTORY
                        Directory where newly discovered unknown gem images should be saved.
  -t, --threshold THRESHOLD
                        Similarity threshold for gem matching (0.0 to 1.0, default: 0.80).
  -f, --format {jsonl,json}
                        Output format: 'jsonl' (default) or 'json'.
  -v, --verbose         Print diagnostic progress to stderr.
```

## Workflow: Configuring Gem Icons

1. **Scan and Export Unknowns**:
   ```bash
   ./gem_scanner.py sample/a.jpeg --generate-unknown target/unknowns
   ```

2. **Configure Legendary Gems**:
   Move and rename unknown images to `baseimage/` using their kebab-case names:
   ```bash
   cp target/unknowns/unknown-1-1-de5bcce2.png baseimage/frozen-heart.png
   cp target/unknowns/unknown-1-2-b7bfcc71.png baseimage/gloom-cask.png
   ```

3. **Ignore Non-Legendary Gems**:
   Prefix normal gem icons with `ignored-` so they are automatically excluded from the output:
   ```bash
   cp target/unknowns/unknown-2-5-d4cdd0df.png baseimage/ignored-tourmaline.png
   cp target/unknowns/unknown-2-6-09a7fe69.png baseimage/ignored-citrine.png
   ```

4. **Run the Scanner**:
   Subsequent runs will now output recognized legendary gems and omit all `ignored-*` items:
   ```bash
   ./gem_scanner.py sample/sample-non-legendary-gems.jpeg
   ```

## Running Tests

Execute the test suite (test artifacts use the gitignored `target/` directory):
```bash
python3 -m unittest test_gem_scanner.py -v
```
