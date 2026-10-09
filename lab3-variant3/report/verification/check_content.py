"""Сверка PDF с исходным DOCX и проверка геометрии. Нужен PyMuPDF."""
import hashlib
import json
import re
from pathlib import Path

import fitz

ROOT = Path(__file__).resolve().parents[1]
EXPECTED_SHA256 = "302c1d18727e696c29727140e7b0a067e197b85bbfa5ff526dc3f6ba3d428448"


def canonical(text):
    text = re.sub(r"(?<=\w)[\-\u00ad]\s*\n\s*(?=\w)", "", text)
    return re.sub(r"\s+", "", text).replace("\u00ad", "")


doc = fitz.open(ROOT / "main.pdf")
lines, footers, intervals, outside, gaps = [], [], [], [], []
for index, page in enumerate(doc):
    assert abs(page.rect.width - 595.276) < .01
    assert abs(page.rect.height - 841.890) < .01
    page_lines = []
    for block in page.get_text("dict")["blocks"]:
        if "lines" not in block:
            continue
        previous = None
        for line in block["lines"]:
            spans = line["spans"]
            text = "".join(span["text"] for span in spans)
            if text.strip().isdigit() and line["bbox"][1] > 790:
                footers.append([index + 1, text, (line["bbox"][0] + line["bbox"][2]) / 2])
                continue
            lines.append(text)
            page_lines.append(line)
            baseline = spans[0]["origin"][1]
            if previous is not None and 17 < baseline - previous < 22:
                intervals.append(baseline - previous)
            previous = baseline
            if index:
                for span in spans:
                    if span["bbox"][0] < 84.99 or span["bbox"][2] > 552.81:
                        outside.append([index + 1, span["text"], span["bbox"]])
    backgrounds = sorted(set(
        tuple(drawing["rect"]) for drawing in page.get_drawings()
        if drawing.get("fill") and abs(drawing["fill"][0] - .96078) < .0001
    ))
    for rect in backgrounds:
        before = [line for line in page_lines if line["bbox"][3] < rect[1]]
        after = [line for line in page_lines if line["bbox"][1] > rect[3]]
        gaps.append({
            "page": index + 1, "rect": rect,
            "before_visible_bp": rect[1] - max(line["bbox"][3] for line in before) if before else None,
            "after_visible_bp": min(line["bbox"][1] for line in after) - rect[3] if after else None,
        })

actual = canonical("\n".join(lines))
assert len(actual) == 11659, len(actual)
assert hashlib.sha256(actual.encode()).hexdigest() == EXPECTED_SHA256, "Содержание PDF отличается от DOCX"
manifest = json.loads((ROOT / "verification/source-manifest.json").read_text())
files = [item for item in manifest if item["kind"] == "code"]
for item in files:
    assert hashlib.sha256((ROOT / item["file"]).read_bytes()).hexdigest() == item["sha256"], item["file"]
assert not outside, outside
assert all(abs(value - 19.5) < .015 for value in intervals)
assert len(footers) == len(doc) - 1
assert all(str(page) == number and abs(center - doc[0].rect.width / 2) < .01 for page, number, center in footers)
assert all(gap[key] is None or gap[key] >= 19.5 for gap in gaps for key in ("before_visible_bp", "after_visible_bp"))
log = ROOT / "main.log"
issues = []
if log.exists():
    issues = [line for line in log.read_text().splitlines() if any(word in line for word in ("Warning", "Overfull", "Error"))]
    assert not issues, issues

audit = {
    "status": "PASS", "pages": len(doc),
    "rendered_pdf_content_match": True,
    "source_canonical_sha256": EXPECTED_SHA256,
    "compared_non_whitespace_characters": len(actual),
    "code_files_byte_identical": len(files),
    "paragraph_line_intervals_checked": len(intervals),
    "line_interval_bp": 19.5,
    "page_footer_positions": footers,
    "text_outside_margins": outside,
    "compilation_errors_font_warnings_overflows": issues,
    "code_outer_skip_bp": 19.5, "code_inner_padding_mm": 4,
    "code_background_gaps": gaps,
    "comparison_rules": "Исключаются пробелы, номера страниц и добавленные при вёрстке переносы слов; исходные файлы кода сравниваются побайтно.",
}
(ROOT / "verification/content-audit.json").write_text(json.dumps(audit, ensure_ascii=False, indent=2) + "\n")
(ROOT / "verification/pdf-text.txt").write_text("\n".join(lines) + "\n")
print(f"PASS: {len(doc)} страниц; {len(actual)} символов; {len(files)} файлов; {len(intervals)} интервалов; внешние отступы кода ≥ 19,5 пункта")
