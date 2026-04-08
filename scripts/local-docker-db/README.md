# Local Docker DB – Development Runner

Dieses Verzeichnis enthält alle Scripts, um die lokale Entwicklungsumgebung
(App + PostgreSQL in Docker + Regression Tests) konsistent zu starten.

## Ziele

- Keine lokale PostgreSQL‑Installation notwendig
- App läuft unter Windows (IntelliJ, Git Bash)
- PostgreSQL läuft in WSL (Docker)
- Einheitliches Routing über `run`
- Stabiler App‑Start über universelles Script
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
    # universell, stabil, überall lauffähig

./scripts/local-docker-db/run app mvn
    # nur Git Bash / Windows
    # unter WSL deaktiviert (ENV‑Probleme mit mvnw.cmd)
```

### Tests

```bash
./scripts/local-docker-db/run test regression
    # führt vollständige Regression-Test-Suite aus
    # erkennt automatisch WSL/GitBash/Linux/macOS
    # wartet auf API-Health-Status und startet dann Maven-Tests
```

## Warum das Profil **local-docker-db**?

Dieses Profil ist für Entwickler gedacht, die:
- kein PostgreSQL lokal installieren wollen
- die App nicht im Container laufen lassen wollen
- aber Docker in WSL installiert haben
- und App + PostgreSQL als Container nutzen wollen

Die App spricht automatisch gegen die docker-DB, basierend auf *.env.local-docker-db*

---

## Architektur (Kurzüberblick)

- App läuft unter windows (IntelliJ, Git Bash, mvnw.cmd)
- PostgreSQL läuft in WSL (Docker)
- Regression‑Tests laufen immer in WSL,
  auch wenn sie aus Git Bash gestartet werden
  → sie rufen *mvnw.cmd* über *cmd.exe* auf
- run ist der zentrale Router für alle lokalen Dev-Kommandos

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

## Voraussetzungen

- Docker mit WSL-Backend
- Java 21 unter Windows
- Git Bash installiert
- *.env.local-docker-db* vorhanden