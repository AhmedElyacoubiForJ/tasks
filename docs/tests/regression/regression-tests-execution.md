[//]: # (docs/tests/regression/regression-tests-execution.md)
# Regressionstests – Ausführung

## Voraussetzungen
- Anwendung läuft lokal auf Port **8080**.
- Unterstützte Profile:
    - **local-dev** (H2)
    - **compose-dev** (Docker + Postgres)
- RestAssured greift immer über  
  `http://localhost:8080/api`  
  auf die laufende Anwendung zu – unabhängig davon, ob sie lokal oder im Container läuft.

### Hinweis
Regressionstests benötigen **keine** Test‑YAMLs.  
Sie greifen nicht auf Spring‑Profile zu.

## Einzelne Tests starten
Über IDE oder Maven:

```bash
mvn -Dtest=TaskApiTest test
```

## Gesamte Suite starten
```bash
mvn test
```

## Testprinzip
- RestAssured ruft echte API‑Endpunkte auf (`/api/...`).
- Tests prüfen Statuscodes, Struktur und Inhalte der Responses.
- Szenarien decken typische Nutzeraktionen ab (CRUD, Filter, Patch, UseCases).
- Bei **compose-dev** laufen die Tests von Windows aus gegen die Docker‑Instanz (Port‑Mapping `8080:8080`).

## Nächste Schritte (CI)
- Regressionstests automatisiert in GitHub Actions ausführen.
- Anwendung im CI mit beiden Profilen starten.
- Regressionstests als Pflichtlauf vor jedem Merge.