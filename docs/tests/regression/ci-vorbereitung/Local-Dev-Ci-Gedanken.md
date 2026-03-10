Du kannst GitHub Actions problemlos so konfigurieren, dass es:

1. deine Spring‑Boot‑App im **local‑dev**‑Profil startet,
2. H2 verwendet (kein Docker, kein Testcontainer nötig),
3. nur **bestimmte Regressionstests** ausführt,
4. und dir damit zeigt, ob GitHub die App sauber starten + testen kann.

Das ist absolut machbar – und dein `application-test.yml` ist bereits perfekt dafür vorbereitet.

---

## Warum GitHub Actions damit klarkommt

GitHub Actions kann:

- Java 17/21 installieren
- Maven ausführen
- deine App starten
- parallel Tests laufen lassen
- Ports öffnen (z.B. 8080)
- H2 In‑Memory nutzen (funktioniert ohne zusätzliche Services)

Das heißt: **Dein Setup ist CI‑ready**, ohne dass du Docker oder Testcontainers brauchst.

---

## Was du *jetzt* brauchst (für regression-tests-ci.md)

Eine erste CI‑Version, die nur Folgendes tut:

1. **App starten** im Hintergrund
2. **Warten**, bis sie erreichbar ist
3. **Nur bestimmte Tests** ausführen
4. **Feedback geben**, ob alles funktioniert

Damit testest du, ob GitHub Actions:

- die App starten kann
- das Profil `local-dev` akzeptiert
- die Tests gegen Port 8080 laufen lassen kann

---

## Beispiel‑Workflow (kompakt, minimal, perfekt für deinen Zweck)

Das kommt später in `.github/workflows/regression-tests.yml`  
(aber wir dokumentieren es jetzt in `regression-tests-ci.md`).

```yaml
name: API Regression Tests

on:
  push:
    branches: [ main ]
  pull_request:

jobs:
  regression-tests:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Setup JDK
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 21

      - name: Start Application (local-dev)
        run: |
          nohup mvn spring-boot:run -Dspring-boot.run.profiles=local-dev &
          sleep 15

      - name: Run Selected Regression Tests
        run: mvn -Dtest=TaskApiFullTestSuite test
```

### Warum das funktioniert

- `nohup` startet die App im Hintergrund
- `sleep 15` gibt der App Zeit zum Hochfahren
- Tests laufen gegen `http://localhost:8080`
- H2 wird automatisch verwendet
- Keine externe DB nötig

---

## Was du später erweitern kannst

- Health‑Check statt `sleep`
- TestContainers (falls du echte DB willst)
- App als JAR starten statt Maven Plugin
- Separate Jobs für CRUD / Tasks / UseCases
- Caching für Maven Dependencies
- Matrix‑Builds (Java 17 + 21)

Aber **für den ersten Schritt** ist das oben perfekt.

---

## Vorschlag für deine Datei `regression-tests-ci.md`

```markdown
# Regressionstests – CI (GitHub Actions)

Die CI führt ausgewählte Regressionstests automatisch aus, sobald auf GitHub gepusht wird. Ziel ist zu prüfen, ob GitHub die Anwendung starten und die Tests erfolgreich ausführen kann.

## Ablauf

1. Anwendung im Profil `local-dev` starten  
2. Warten, bis die App erreichbar ist  
3. Nur definierte Regressionstests ausführen  
4. Ergebnis zurückmelden

## Beispiel-Workflow (minimal)

```yaml
name: API Regression Tests

on:
  push:
    branches: [ main ]
  pull_request:

jobs:
  regression-tests:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Setup JDK
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 21

      - name: Start Application (local-dev)
        run: |
          nohup mvn spring-boot:run -Dspring-boot.run.profiles=local-dev &
          sleep 15

      - name: Run Selected Regression Tests
        run: mvn -Dtest=TaskApiFullTestSuite test
```

## Nächste Schritte

- Health‑Check statt `sleep`
- Erweiterung auf mehrere TestSuites
- Integration in Merge‑Checks
- Optional: TestContainers für realistische Umgebungen
```

---

Wenn du möchtest, kann ich dir als Nächstes:

- einen **Health‑Check** bauen (statt `sleep 15`)  
- die **optimale TestSuite** auswählen, die du zuerst laufen lassen solltest  
- oder den Workflow so umbauen, dass er **schneller** läuft (Maven Cache, JAR‑Start, usw.)

Was wäre dein nächster Schritt?