# Regressionstests

> Die Regressionstests prüfen die API automatisiert über echte HTTP‑Aufrufe.
> Sie ersetzen die frühere manuelle Prüfung über Swagger‑UI und ermöglichen schnelle, wiederholbare und zuverlässige Testläufe.

## Dateiübersicht

- **overview** — Warum es Regressionstests gibt
- **dependencies** — Welche technischen Abhängigkeiten benötigt werden
- **execution** — Wie die Tests ausgeführt werden
- **structure** — Welche Testklassen existieren
- **ci** — Platzhalter für zukünftige CI‑Automatisierung
- **README** — Einstiegspunkt in alle Regressionstests

## Ziel der Regressionstests

- API‑Endpunkte automatisiert testen
- Fehler früh erkennen
- konsistente Ergebnisse unabhängig vom Entwickler
- Grundlage für spätere CI‑Automatisierung schaffen

## Voraussetzungen

- Anwendung läuft lokal auf Port 8080
- Profil **local-dev** aktiv (H2‑In‑Memory)
- Tests verwenden REST‑Assured als HTTP‑Client

## Testaufbau

Die Tests folgen einer klaren Struktur:

- **CRUD‑Tests für TaskLists**
- **CRUD‑Tests für Tasks innerhalb einer TaskList**
- **Use‑Case‑Tests (Complete, Reopen, Archive, Restore)**
- **Bereichsspezifische TestSuites**
- **Gesamtsuite für alle API‑Tests**

Die Struktur spiegelt exakt die API‑Interfaces wider.

## Ausführung

### Einzeltest
```
mvn -Dtest=TaskListsCrudControllerHappyPathTest test
```

### Bereichssuite
```
mvn -Dtest=TaskListsCrudTestSuite test
```

### Gesamte API‑Suite
```
mvn -Dtest=TaskApiFullTestSuite test
```

## Testprinzip

- REST‑Assured ruft echte API‑Endpunkte auf
- Responses werden auf Status, Struktur und Inhalt geprüft
- Tests simulieren typische Nutzeraktionen (CRUD + UseCases)
- Fehlerfälle (Validation, NotFound, MethodNotAllowed) sind abgedeckt

## Nächste Schritte

- Tests in GitHub Actions automatisieren
- Anwendung im CI starten (Spring Boot + H2)
- Regressionstests als Pflichtlauf vor jedem Merge
