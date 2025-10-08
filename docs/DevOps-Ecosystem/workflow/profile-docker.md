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

➡️ Details zu jedem Skript findest du in [`docs/scripts.md`](../dev-tools/scripts.md)

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

# 🧪 Laufzeitumgebung: `compose-dev`

Die Umgebung `compose-dev` simuliert eine produktionsnahe Container-Landschaft für lokale Entwicklung und Diagnose. Sie verwendet:

- `docker-compose-dev.yml` im Projekt-Root
- `Dockerfile.dev` für die App
- `.env.compose-dev` zur Steuerung aller ENV-Variablen
- `init-dev-db.sh` zur Initialisierung der Datenbank beim Containerstart
- Diagnose- und Steuerungsskripte unter `scripts/compose-dev/`
- Makefile-Kommandos in `Makefile.compose`

---

## 🔧 Start & Stop

```bash
make compose-dev-up      # Umgebung starten
make compose-dev-down    # Umgebung stoppen
make compose-dev-restart # Umgebung neu starten
```

---

## 📜 Logs & Diagnose

```bash
make compose-dev-logs
```

Dieses Kommando zeigt:

- ENV-Diagnose (Parser-Check & Container-Check)
- Aktive Spring-Profile
- Live-Logs der App

---

## 🧹 Cleanup & Volume-Handling

```bash
make compose-dev-clean         # Container & Volume entfernen
make compose-dev-volumes       # Volume-Details anzeigen
make compose-dev-db-init       # Datenbank manuell initialisieren (optional)
```

---

## 📁 ENV-Datei `.env.compose-dev`

Steuert alle Variablen für App & DB:

```env
POSTGRES_USER=admin
POSTGRES_DB=tasks_dev_db
POSTGRES_PASSWORD=adminpass
APP_DB_USER=dev_user
APP_DB_PASSWORD=dev_secret
SPRING_PROFILES_ACTIVE=compose-dev
VOLUME=pgdata-compose-dev
```

---

## 📂 Diagnose-Tools

- `scripts/compose-dev/logs.sh` → ENV-Check + Logs
- `scripts/compose-dev/restart.sh` → Umgebung neu starten
- `scripts/compose-dev/init-db.sh` → ruft `init-dev-db.sh` im Root auf

---

### 📄 Weitere Hinweise

- Siehe auch: `DB-DIAGNOSTIK.md` für Datenbankanalyse
- Siehe auch: `SESSION-METRICS-EXAMPLE.md` für Hibernate-Cache-Statistiken
- Die Umgebung ist modular erweiterbar für `compose-prod`, `container-dev`, `local`

---

> 🧱 Dieser Workflow ist modular, portabel und CI-kompatibel.  
> Er bildet die Brücke zwischen lokalem Entwickeln und automatisierter Qualitätssicherung.
> Wir haben hier nicht nur eine Umgebung gebaut, sondern ein **Entwickler-Ökosystem**, das sich selbst erklärt, selbst testet und selbst heilt.

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