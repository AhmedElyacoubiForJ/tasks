# API‑Versionierung

> Die API ist so aufgebaut, dass neue Funktionen oder Breaking Changes eingeführt werden können, ohne bestehende Clients zu beeinträchtigen. Jede Version besitzt ihren eigenen Contract‑ und Implementierungsbereich und bleibt unabhängig von Domain‑ und Application‑Schicht.

## Struktur der API‑Versionen

```
controllers/api/
v1/   → stabile, produktive API
v2/   → zukünftige Erweiterungen
```

- **v1** enthält alle aktuellen Contracts, DTOs, Validation, Responses und Controller‑Implementierungen.  
- **v2** ist vorbereitet für neue Features oder inkompatible Änderungen.  
- Domain und Application bleiben unverändert und versionierungsfrei.

## Grundprinzipien

- Keine Breaking Changes in v1.  
- Neue oder inkompatible Funktionen kommen ausschließlich in v2.  
- Jede Version besitzt eigene DTOs, Contracts und Responses.  
- Swagger zeigt jede Version separat.  
- Deprecation‑Hinweise in v1, wenn Endpunkte ersetzt werden.

## Dokumentation

- **API‑Schicht – Designüberblick**  
  `api-layer-overview.md`  
  Überblick über Aufbau, Struktur und Verantwortlichkeiten der API‑Schicht.

- **Versionierungsstrategie (v1 → v2)**  
  `api-versioning-strategy.md`  
  Regeln, Struktur und Vorteile der API‑Versionierung.
