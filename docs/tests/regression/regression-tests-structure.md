# Teststruktur

Die Regressionstests sind nach API‑Bereichen organisiert.

## Bereiche
- **TaskLists CRUD**
- **Tasks CRUD**
- **UseCases** (Complete, Reopen, Archive, Restore)
- **Fehlerfälle** (Validation, NotFound, MethodNotAllowed)

## Suites
- Bereichsspezifische Suites
- Gesamtsuite für alle API‑Tests

## Profil‑Unabhängigkeit
Die Struktur ist identisch für **local-dev** und **compose-dev**, da die Tests nur HTTP‑Requests senden und keine Spring‑Konfiguration laden.

---

## Warum diese Struktur ideal ist
- 1:1 Spiegelung der API‑Interfaces
- klare Trennung CRUD / Tasks / UseCases
- jede Suite kann separat laufen
- perfekt für spätere CI‑Automatisierung
- extrem leicht erweiterbar
