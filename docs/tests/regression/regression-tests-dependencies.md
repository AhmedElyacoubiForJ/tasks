# Abhängigkeiten

## Technische Basis
- **JUnit 5**
- **RestAssured**
- **JSON-Assert / Hamcrest**
- **Maven Surefire Plugin**

## Keine Spring-Testprofile notwendig

Regressionstests verwenden **keine** Spring‑Konfigurationen aus `src/test/resources`.

Sie benötigen **keine**:
- `application-test.yml`
- `application-local-dev-test.yml`
- `application-compose-dev-test.yml`

Grund:
> RestAssured testet die API wie ein externer Client und lädt keinen Spring‑Kontext.

## Laufende Anwendung erforderlich
Die Tests benötigen eine laufende Instanz der Anwendung:

- **local-dev** (H2, IntelliJ)
- **compose-dev** (Docker + Postgres)

Beide Profile sind über `http://localhost:8080/api` erreichbar.
