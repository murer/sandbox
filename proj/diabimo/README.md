# Diablo Immortal Legendary Gem Scanner CLI

A command-line tool to scan inventory screenshots of Diablo Immortal legendary gems, recognizing gem icons, ranks, and quantities, and outputting them in JSON Lines (JSONL) format.

## Features

- **Automated Grid Detection**: Automatically determines cell pitch, row/column boundaries, and ignores partial or cut-off cells at screenshot margins.
- **Color-Masked OCR**:
  - **Rank**: Detects gold/tan font in the bottom-left (`R1`, `R5`, `R10`, etc.).
  - **Quantity**: Detects bright white digits in the bottom-right. Defaults to `1` when quantity text is absent.
- **Template Matching**: Compares gem centers against a configurable directory of known gem icons using normalized cross-correlation.
- **Auto-Registration of Unknown Gems**: Automatically saves unrecognized gem icons into the gems directory as `unknown-<random_hex>.png` and reuses the ID for identical gems in the same scan.
- **Kebab-Case Naming**: Name files in the gems directory using kebab-case (e.g. `blood-soaked-jade.png`, `frozen-heart.png`) to identify them on future runs.
- **Flexible Output**: Supports JSON Lines (`jsonl`, default) and formatted JSON array (`--format json`).

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
Scan a screenshot using the default `./gems` directory:
```bash
./gem_scanner.py sample/a.jpeg
```

### Custom Gems Directory
Specify a custom directory for gem icons:
```bash
./gem_scanner.py sample/a.jpeg -d /path/to/my_gems
```

### Options
```text
positional arguments:
  image                 Path to the inventory screenshot image.

options:
  -h, --help            Show this help message and exit.
  -d, --gems-dir GEMS_DIR
                        Directory containing known gem icon images (default: ./gems).
  -t, --threshold THRESHOLD
                        Similarity threshold for gem matching (0.0 to 1.0, default: 0.80).
  -f, --format {jsonl,json}
                        Output format: 'jsonl' (default) or 'json'.
  -v, --verbose         Print diagnostic progress to stderr.
```

## Workflow: Configuring Known Gems

1. **Initial Run**:
   Run the scanner on your screenshot:
   ```bash
   ./gem_scanner.py sample/a.jpeg -d ./gems
   ```
   All unrecognized gems are saved into `./gems/unknown-<hex>.png`.

2. **Rename Unknown Files**:
   Inspect the images in `./gems/` and rename them to their kebab-case gem names:
   ```bash
   mv gems/unknown-de5bcce2.png gems/frozen-heart.png
   mv gems/unknown-b7bfcc71.png gems/gloom-cask.png
   ```

3. **Subsequent Runs**:
   Running the scanner again will now automatically identify those gems as `frozen-heart` and `gloom-cask`.

## Sample Output

```json
{"gem": "frozen-heart", "rank": "R1", "quantity": 2}
{"gem": "unknown-06b1f0b8", "rank": "R1", "quantity": 1}
{"gem": "gloom-cask", "rank": "R1", "quantity": 2}
{"gem": "gloom-cask", "rank": "R1", "quantity": 1}
{"gem": "unknown-8326c903", "rank": "R5", "quantity": 1}
{"gem": "unknown-61dcadfc", "rank": "R1", "quantity": 6}
```

## Running Tests

Execute the unit and integration tests:
```bash
python3 -m unittest test_gem_scanner.py -v
```
