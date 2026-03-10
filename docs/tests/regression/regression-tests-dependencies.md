# Regressionstests – Dependencies & Setup

Für die API‑Regressionstests wird RestAssured verwendet.

## Dependencies (pom.xml)

```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.4.0</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>json-path</artifactId>
    <version>5.4.0</version>
    <scope>test</scope>
</dependency>
```

## Testumgebung
- Profil: **local-dev**
- Datenbank: **H2 In‑Memory**
- Anwendung muss **vor Teststart laufen** (Port 8080)

## Struktur
- Tests liegen unter `src/test/java/...`
- Tests können einzeln oder als Suite ausgeführt werden.
- Jede Suite deckt einen API‑Bereich ab (Task, TaskList, UseCases).
