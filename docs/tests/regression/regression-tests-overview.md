
# Regressionstests – Überblick

> Die manuelle Prüfung über Swagger‑UI war zu aufwendig und fehleranfällig.  
> Um wiederholbare, schnelle und automatisierte Tests zu ermöglichen, wurden Regressionstests mit RestAssured eingeführt.

## Ausgangssituation
- API musste nach jedem Start manuell geprüft werden.
- Swagger‑UI war hilfreich, aber zu langsam für vollständige Regression.
- Fehler wurden spät entdeckt, weil kein automatisierter Testlauf existierte.

## Ziel
- API‑Endpunkte automatisiert testen.
- Konsistente Ergebnisse unabhängig vom Entwickler.
- Grundlage für spätere CI‑Automatisierung schaffen.

## Ansatz
- RestAssured als Testframework.
- Tests laufen gegen eine laufende Anwendung.
- Tests können einzeln oder als Suite ausgeführt werden.
