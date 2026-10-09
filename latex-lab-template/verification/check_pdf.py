#!/usr/bin/env python3
"""Проверка реально встроенных шрифтов и кегля, а не настроек .tex.

Запуск из каталога шаблона: python verification/check_pdf.py main.pdf
Требуется PyMuPDF: python -m pip install pymupdf
"""
import json
import sys
from pathlib import Path

import fitz


def inspect(path):
    document = fitz.open(path)
    errors = []
    pages = []
    for number, page in enumerate(document, 1):
        spans = []
        for block in page.get_text("dict")["blocks"]:
            for line in block.get("lines", []):
                for span in line["spans"]:
                    if not span["text"].strip():
                        continue
                    spans.append({key: span[key] for key in
                                  ("text", "font", "size", "origin", "bbox")})
                    font = span["font"].replace(" ", "").replace("-", "")
                    if "TimesNewRoman" not in font:
                        errors.append(f"Стр. {number}: другой шрифт {span['font']}: {span['text']!r}")
                    if abs(span["size"] - 13.0) > 0.005:
                        errors.append(f"Стр. {number}: кегль {span['size']:.4f}: {span['text']!r}")
        pages.append({"page": number, "page_size_bp": list(page.rect),
                      "fonts_and_sizes": sorted({(s["font"], round(s["size"], 4)) for s in spans}),
                      "spans": spans,
                      "images": page.get_image_info()})
    result = {"pdf": str(path), "pages": pages, "errors": errors,
              "status": "PASS" if not errors else "FAIL",
              "scope": "Текстовые объекты PDF; текст внутри растровых картинок не проверяется."}
    destination = Path(__file__).parent / f"{Path(path).stem}-audit.json"
    destination.write_text(json.dumps(result, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"{result['status']}: {len(pages)} страниц; отчёт {destination}")
    for p in pages:
        print(f"  Стр. {p['page']}: {p['fonts_and_sizes']}")
    for error in errors:
        print(error)
    return not errors


if __name__ == "__main__":
    sys.exit(0 if inspect(Path(sys.argv[1] if len(sys.argv) > 1 else "main.pdf")) else 1)
