[//]: # (docs/tests/regression/README.md)
# Regressionstests

> Die Regressionstests prüfen die API automatisiert über echte HTTP‑Aufrufe.  
> Sie ersetzen die frühere manuelle Prüfung über Swagger‑UI und ermöglichen schnelle, wiederholbare und zuverlässige Testläufe – unabhängig davon, ob die Anwendung im Profil **local-dev** oder **compose-dev** läuft.

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

- Anwendung läuft lokal auf Port **8080**
- Unterstützte Profile:
    - **local-dev** (H2‑In‑Memory, direkt in IntelliJ startbar)
    - **compose-dev** (Spring Boot + Postgres im Docker‑Container)
- Tests verwenden **REST‑Assured** als HTTP‑Client
- Zugriff erfolgt immer über:  
  `http://localhost:8080/api`

### Wichtiger Hinweis
> **Regressionstests benötigen KEINE `application-test.yml`, `application-local-dev-test.yml` oder `application-compose-dev-test.yml`.**  
> RestAssured verhält sich wie ein externer Client und greift NICHT auf Spring‑Konfigurationen zu.

## Testaufbau

Die Tests folgen einer klaren Struktur:

- CRUD‑Tests für TaskLists
- CRUD‑Tests für Tasks innerhalb einer TaskList
- Use‑Case‑Tests (Complete, Reopen, Archive, Restore)
- Bereichsspezifische TestSuites
- Gesamtsuite für alle API‑Tests

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
- Funktioniert identisch für **local-dev** und **compose-dev**, da beide Profile über denselben Port erreichbar sind

## Nächste Schritte

- Tests in GitHub Actions automatisieren
- Anwendung im CI mit beiden Profilen (**local-dev** und **compose-dev**) starten
- Regressionstests als Pflichtlauf vor jedem Merge

---

Wenn du möchtest, können wir als Nächstes die **overview**, **dependencies** oder **structure** genauso konsistent anpassen.