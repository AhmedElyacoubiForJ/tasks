# Swagger / OpenAPI

Dieser Ordner enthält alle Dateien rund um die OpenAPI‑Spezifikation der Anwendung.  
Die API‑Definition kann manuell oder später automatisiert (CI) exportiert werden.

## Inhalte

- `openapi.yaml` — vollständige OpenAPI‑Spezifikation (YAML)
- `openapi.json` — optionale JSON‑Variante
- `OpenAPI-Exportieren.md` — Anleitung für manuellen Export
- `OpenApi-Export-CI-Ready.md` — Roadmap für CI‑Automatisierung

## Zweck

- zentrale Ablage der API‑Spezifikation
- Grundlage für API‑Dokumentation, Client‑Generierung und CI‑Pipelines
- versionierbar und unabhängig von Code‑Änderungen

## Skripte

Unter `scripts/swagger/` liegen Export‑Skripte:

- `export-openapi.sh`
- `export-openapi.json.sh`

Diese erleichtern den lokalen Export und dienen als Basis für spätere CI‑Automatisierung.
