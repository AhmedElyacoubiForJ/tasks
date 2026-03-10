# API‑Versionierung

Die API ist vollständig versionierbar aufgebaut, damit neue Funktionen oder Breaking Changes eingeführt werden können, ohne bestehende Clients zu beeinträchtigen. Jede Version besitzt ihren eigenen Contract‑ und Implementierungsbereich und bleibt unabhängig von Domain und Application.

## Struktur

```
docs/api/versionierung/
  README.md
  index.md
  api-layer-overview.md
  api-versioning-strategy.md
```

- **api-layer-overview.md** beschreibt Aufbau und Verantwortlichkeiten der API‑Schicht.
- **api-versioning-strategy.md** erklärt die Regeln und Struktur der API‑Versionierung (v1 → v2).
- **README.md** dient als Einstiegspunkt für diesen Ordner.

## Ziele der Versionierung

- Stabilität für bestehende Clients.
- Saubere Trennung zwischen alten und neuen API‑Versionen.
- Erweiterbarkeit ohne Auswirkungen auf Domain oder Application.
- Klare Struktur für zukünftige API‑Versionen.

## Übersicht der API‑Versionen

```
controllers/api/
  v1/   → stabile, produktive API
  v2/   → zukünftige Erweiterungen
```

> Jede Version enthält eigene Contracts, DTOs, Validation und Controller‑Implementierungen.
