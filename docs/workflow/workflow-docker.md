# 🐳 **Workflow: Docker & Docker Compose**

> Dieser Workflow beschreibt die containerisierte Entwicklungsumgebung der Applikation. Ziel ist es, produktionsnahe Abläufe lokal zu simulieren und eine stabile Basis für CI/CD vorzubereiten.

---

## 🔹 Übersicht

1. **Docker-Skripte (manuell, portabel)**  
   → App und DB werden über Shellskripte gesteuert, ideal für lokale Entwicklung und Debugging

2. **Docker Compose (dev-orientiert)**  
   → App und DB werden gemeinsam orchestriert, ideal für CI/CD und produktionsnahe Tests

---

## 🐚 **Teil 1: Docker-Skripte (ohne Compose)**

### 📦 Struktur

Die Umgebung basiert auf zwei Hauptcontainern:

- `postgres-dev`: Datenbankcontainer mit initialem Setup via `init-db.sh`
- `tasks-app`: Applikationscontainer mit Zugriff auf die Datenbank

Alle Konfigurationen befinden sich unter:

```
db-config/container/dev/
├── .env.dev
└── init-test.sql
```

Die Steuerung erfolgt über:

```
scripts/docker/
├── dev.sh
├── stop.sh
├── restart.sh
├── logs.sh
├── status.sh
├── init-db.sh
└── reset-db.sh
```

---

### 🛠️ Skriptfunktionen

| Skript           | Zweck                                               |
|------------------|-----------------------------------------------------|
| `dev.sh`         | Startet App- und DB-Container mit Healthcheck       |
| `stop.sh`        | Beendet und entfernt Container                      |
| `restart.sh`     | Kombiniert Stop & Start                             |
| `logs.sh`        | Zeigt Live-Logs der App oder DB                     |
| `status.sh`      | Zeigt den aktuellen Zustand der Container           |
| `init-db.sh`     | Erstellt DB-Container inkl. Volume & Test-Tabelle   |
| `reset-db.sh`    | Entfernt Container & Volume, setzt DB zurück        |

Alle Skripte sind **portabel** und nutzen `ROOT_DIR`, um unabhängig vom Arbeitsverzeichnis zu funktionieren.

---

### 🧪 Healthcheck & Interaktivität

- `dev.sh` prüft den `/actuator/health` Endpoint der App
- Der Nutzer kann direkt in die Datenbank springen (`psql`)
- Optionales Initialisierungsskript (`init-test.sql`) prüft, ob `APP_DB_USER` Tabellen erstellen kann

---

### 🧰 Makefile (optional)

```bash
make dev           # Startet die Umgebung
make stop          # Beendet die Umgebung
make restart       # Setzt die Umgebung neu auf
make logs          # Zeigt Logs
make status        # Zeigt Containerstatus
make init-db       # Erstellt DB-Container
make reset-db      # Setzt DB zurück
```

---

> ➡️ Dieser Workflow ist modular, portabel und CI-kompatibel. Er bildet die Brücke zwischen lokalem Entwickeln und automatisierter Qualitätssicherung.

---

## 📦 **Teil 2: Docker Compose (dev-orientiert)**

> Für produktionsnahe Tests und CI/CD wird `docker-compose-dev.yml` verwendet.  
> Ziel: deklarative Orchestrierung, reproduzierbare Builds, klare Trennung von Dev & Prod.

### 🔧 Aufbau

```yaml
services:
  app:
    build:
      context: ./docker/app
      dockerfile: Dockerfile
    image: myimage:tasks-app
    ports:
      - "8080:8080"
    env_file:
      - ./db-config/container/dev/.env.dev
    depends_on:
      - db

  db:
    image: postgres:16
    volumes:
      - pgdata:/var/lib/postgresql/data
    env_file:
      - ./db-config/container/dev/.env.dev
    ports:
      - "5432:5432"

volumes:
  pgdata:
```

### 🧠 Kommentarblock zur Build-Strategie

```yaml
# ----------------------------------------
# 🧱 Build-Kontext: Portables Szenario für Image-Erstellung
# ----------------------------------------
# Wir verwenden einen expliziten Build-Kontext, um das Image aus einem modularen Verzeichnis zu erstellen.
# Dadurch vermeiden wir unnötige Dateien im Build (z.B. .git, node_modules, docs) und halten das Setup wartbar.
# Das Verzeichnis docker/app enthält den Dockerfile und alle relevanten Ressourcen für das App-Image.
# Vorteil: klare Trennung, portabel über ROOT_DIR referenzierbar, ideal für CI/CD und produktionsnahe Builds.
# ----------------------------------------
```