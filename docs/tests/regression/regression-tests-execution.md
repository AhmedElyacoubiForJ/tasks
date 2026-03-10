# Regressionstests – Ausführung

## Voraussetzungen
- Anwendung läuft lokal auf Port 8080.
- Profil **local-dev** aktiv (H2).

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
- RestAssured ruft echte API‑Endpunkte auf.
- Responses werden auf Status, Struktur und Inhalt geprüft.
- Tests simulieren typische Nutzeraktionen (CRUD, Filter, Patch, UseCases).

## Nächste Schritte (CI)
- Tests automatisch in GitHub Actions ausführen.
- App im CI starten (Spring Boot + H2).
- Regressionstests als Pflichtlauf vor jedem Merge.
 
[//]: # (- wie die Tests strukturiert sind &#40;Namenskonventionen, Suites, Testklassen‑Layout&#41;)
