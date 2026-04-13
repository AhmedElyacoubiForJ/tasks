[//]: # (scripts/local-docker-db/README.md)
# Local Docker DB – Development Runner

Dieses Verzeichnis enthält alle Scripts, um die lokale Entwicklungsumgebung konsistent zu starten:
**App (JAR oder mvn)**, **PostgreSQL** in **Docker (WSL)** und **Regression‑Tests**.

Der zentrale Einstiegspunkt ist `./scripts/local-docker-db/run`, der als Router für den lokalen `local-docker-db`-Profil 
Dev-Kommandos fungiert.
Er erkennt automatisch die Umgebung (WSL, Git Bash, Windows) und führt die entsprechenden Befehle aus.

## Ziele

- Keine lokale PostgreSQL‑Installation notwendig
- App läuft unter Windows (IntelliJ, Git Bash)
- PostgreSQL läuft in WSL (Docker)
- Einheitliches Routing über ein einziges Script
- Stabiler App‑Start über universelles Script
- Klare, farbige Fehlermeldungen und Kategorie-bezogene Hilfe
- Regression-Tests funktionieren in WSL + Windows Git Bash
- mvn-Modus unter WSL deaktiviert (ENV‑Probleme mit mvnw.cmd).
- Regression‑Tests funktionieren in WSL + Windows

---

## Befehle

### Datenbank

```bash
./scripts/local-docker-db/run db up
./scripts/local-docker-db/run db down
./scripts/local-docker-db/run db down --purge
./scripts/local-docker-db/run db test
```

### App

```bash
./scripts/local-docker-db/run app jar
    # universell, stabil, überall lauffähig (WSL, Git Bash, Linux, macOS)

./scripts/local-docker-db/run app mvn
    # nur Git Bash / Windows
    # unter WSL deaktiviert (mvnw.cmd verliert ENV-Variablen)
```

### Tests

```bash
./scripts/local-docker-db/run test regression
    # führt vollständige Regression-Test-Suite aus
    # erkennt automatisch WSL/GitBash/Linux/macOS
    # wartet auf API-Health-Status und startet dann Maven-Tests
```

---

### Kategorie‑bezogene Hilfe

Jede Kategorie hat eine eigene kompakte Hilfe:

```bash
./scripts/local-docker-db/run db --help
./scripts/local-docker-db/run app --help
./scripts/local-docker-db/run test --help
```

Die globale Hilfe:

```bash
./scripts/local-docker-db/run --help
```

---

## Warum das Profil **local-docker-db**?

Dieses Profil ist für Entwickler gedacht, die:
- kein **PostgreSQL** lokal installieren wollen
- die App **nicht** im Container laufen lassen wollen
- aber **Docker** in **WSL** installiert haben
- **App** + **PostgreSQL** gemeinsam nutzen wollen

Dabei gilt:
- Die **DB** läuft in **Docker (WSL)**.
- Die **APP** läuft **NICHT** im **Container**, sondern direkt:
  - als **JAR** (empfohlen, überall stabil)
  - oder über **mvn** (nur Git Bash / Windows)
- Die **App** spricht automatisch gegen die **Docker-DB**.
- Alle DB-Parameter kommen aus der `.env.local-docker-db` (z.B. DB‑Host, Port, User, Passwort, ...).

---

## Architektur (Kurzüberblick)

### Komponenten
- App
  läuft unter Windows (IntelliJ, Git Bash)
  mvn-Modus nur unter Git Bash
- **PostgreSQL**
  läuft in Docker (WSL)
- **Regression‑Tests**
  läuft immer in WSL, auch wenn sie aus Git Bash gestartet werden

### Ablauf

```ascii
Windows (App, mvnw.cmd)
        ▲
        │ API: http://<Windows-IP>:8080
        ▼
WSL (Docker: PostgreSQL, Regression Tests)
        ▲
        │ Regression ruft mvnw.cmd via cmd.exe auf
        ▼
Git Bash / IntelliJ (Startpunkt)
```

---

## Warum mvn unter WSL deaktiviert ist

`mvnw.cmd` läuft unter Windows/cmd.exe.
cmd.exe kann ENV-Variablen aus WSL nicht zuverlässig übernehmen.
- Whitespace wird eingefügt
- JDBC-URLs werden zerstört
- Ports werden falsch interpretiert
- Spring DevTools verliert ENV beim Restart

Ergebnis: **kaputte DB-Connections**.
Lösung:
- **mvn** nur unter **Git Bash / Windows** erlauben
- unter **WSL** → **JAR‑Modus** verwenden

---

## Voraussetzungen

- Docker mit WSL-Backend
- Java 21 unter Windows
- Git Bash installiert
- `.env.local-docker-db` vorhanden