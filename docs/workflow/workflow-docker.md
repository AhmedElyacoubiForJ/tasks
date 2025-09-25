# 🐳 **Workflow: Docker (Skriptbasiert)**

> Dieser Workflow beschreibt die containerisierte Entwicklungsumgebung der Applikation **ohne Docker Compose**. Ziel ist es, produktionsnahe Abläufe lokal zu simulieren und eine stabile Basis für CI/CD vorzubereiten.

---

## 🔹 Übersicht

Die Umgebung wird über Shellskripte gesteuert und besteht aus zwei Hauptcontainern:

- `postgres-dev`: Datenbankcontainer mit initialem Setup via `init-db.sh`
- `tasks-app`: Applikationscontainer mit Zugriff auf die Datenbank

Alle Konfigurationen befinden sich unter:

```plaintext
db-config/container/dev/
├── .env.dev
├── .env.example
├── init-dev-db.sh
└── init-test.sql
```

Die Steuerung erfolgt über:

```plaintext
scripts/docker/
├── dev.sh
├── stop.sh
├── restart.sh
├── init-db.sh
├── reset-db.sh
├── logs.sh
├── status.sh
├── health.sh
├── check-env.sh
└── colors.sh
```

➡️ Details zu jedem Skript findest du in [`docs/scripts.md`](scripts.md)

---

## 🧪 Wichtige Abläufe

### Umgebung starten

```bash
make dev
```

- Startet App- und DB-Container
- Prüft Health-Status via `/actuator/health`
- Optional: öffnet psql-Konsole

### Umgebung stoppen

```bash
make stop
```

- Entfernt Container
- Optional: Volume löschen

### Datenbank zurücksetzen

```bash
make reset-db
make init-db
```

- Entfernt DB-Container & Volume
- Erstellt neue Datenbank inkl. App-User & Test-Tabelle

### Umgebung prüfen

```bash
make verify
```

- Führt `check-env.sh`, `status.sh` und `health.sh` aus
- Ideal für Preflight-Checks und CI

---

## 📦 Makefile-Kommandos (Auszug)

```makefile
make dev           # Startet die Umgebung
make stop          # Beendet die Umgebung
make restart       # Setzt die Umgebung neu auf
make logs          # Zeigt Logs
make status        # Zeigt Containerstatus
make init-db       # Erstellt DB-Container
make reset-db      # Setzt DB zurück
make check-env     # Prüft .env-Dateien
make health        # Prüft App-Health
make verify        # Führt vollständige Umgebungsvorprüfung aus
```

---

> 🧱 Dieser Workflow ist modular, portabel und CI-kompatibel.  
> Er bildet die Brücke zwischen lokalem Entwickeln und automatisierter Qualitätssicherung.

---

[//]: # (## 📦 **Teil 2: Docker Compose &#40;dev-orientiert&#41;**)

[//]: # ()
[//]: # (> Für produktionsnahe Tests und CI/CD wird `docker-compose-dev.yml` verwendet.  )

[//]: # (> Ziel: deklarative Orchestrierung, reproduzierbare Builds, klare Trennung von Dev & Prod.)

[//]: # ()
[//]: # (### 🔧 Aufbau)

[//]: # ()
[//]: # (```yaml)

[//]: # (services:)

[//]: # (  app:)

[//]: # (    build:)

[//]: # (      context: ./docker/app)

[//]: # (      dockerfile: Dockerfile)

[//]: # (    image: myimage:tasks-app)

[//]: # (    ports:)

[//]: # (      - "8080:8080")

[//]: # (    env_file:)

[//]: # (      - ./db-config/container/dev/.env.dev)

[//]: # (    depends_on:)

[//]: # (      - db)

[//]: # ()
[//]: # (  db:)

[//]: # (    image: postgres:16)

[//]: # (    volumes:)

[//]: # (      - pgdata:/var/lib/postgresql/data)

[//]: # (    env_file:)

[//]: # (      - ./db-config/container/dev/.env.dev)

[//]: # (    ports:)

[//]: # (      - "5432:5432")

[//]: # ()
[//]: # (volumes:)

[//]: # (  pgdata:)

[//]: # (```)

[//]: # ()
[//]: # (### 🧠 Kommentarblock zur Build-Strategie)

[//]: # ()
[//]: # (```yaml)

[//]: # (# ----------------------------------------)

[//]: # (# 🧱 Build-Kontext: Portables Szenario für Image-Erstellung)

[//]: # (# ----------------------------------------)

[//]: # (# Wir verwenden einen expliziten Build-Kontext, um das Image aus einem modularen Verzeichnis zu erstellen.)

[//]: # (# Dadurch vermeiden wir unnötige Dateien im Build &#40;z.B. .git, node_modules, docs&#41; und halten das Setup wartbar.)

[//]: # (# Das Verzeichnis docker/app enthält den Dockerfile und alle relevanten Ressourcen für das App-Image.)

[//]: # (# Vorteil: klare Trennung, portabel über ROOT_DIR referenzierbar, ideal für CI/CD und produktionsnahe Builds.)

[//]: # (# ----------------------------------------)

[//]: # (```)