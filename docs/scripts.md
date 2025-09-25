Sehr gerne, Ahmed — hier ist eine elegante, gut strukturierte Version von `docs/scripts.md`, die deine Skriptlandschaft dokumentiert und navigierbar macht. Sie ist modular aufgebaut, klar kommentiert und bereit für Erweiterung:

---

# 📜 **Skriptübersicht: Docker-Workflow**

> Diese Datei dokumentiert alle Shellskripte zur Steuerung der containerisierten Entwicklungsumgebung. Ziel ist eine klare Navigation, konsistente Nutzung und robuste Erweiterbarkeit.

---

## 📁 Verzeichnisstruktur

```plaintext
scripts/
└── docker/
    ├── dev.sh
    ├── stop.sh
    ├── restart.sh
    ├── logs.sh
    ├── status.sh
    ├── init-db.sh
    └── reset-db.sh
```

---

## 🛠️ Skriptbeschreibung

| Skript           | Zweck                                               | Aufrufbeispiel                     |
|------------------|-----------------------------------------------------|------------------------------------|
| `dev.sh`         | Startet App- und DB-Container mit Healthcheck       | `bash scripts/docker/dev.sh`       |
| `stop.sh`        | Beendet und entfernt Container                      | `bash scripts/docker/stop.sh`      |
| `restart.sh`     | Kombiniert Stop & Start                             | `bash scripts/docker/restart.sh`   |
| `logs.sh`        | Zeigt Live-Logs der App oder DB                     | `bash scripts/docker/logs.sh`      |
| `status.sh`      | Zeigt den aktuellen Zustand der Container           | `bash scripts/docker/status.sh`    |
| `init-db.sh`     | Erstellt DB-Container inkl. Volume & Test-Tabelle   | `bash scripts/docker/init-db.sh`   |
| `reset-db.sh`    | Entfernt Container & Volume, setzt DB zurück        | `bash scripts/docker/reset-db.sh`  |

---

## 🔧 Portabilität & Pfadlogik

Alle Skripte verwenden:

```bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
```

➡️ Dadurch sind sie unabhängig vom aktuellen Arbeitsverzeichnis und funktionieren konsistent in CI/CD, Makefiles und interaktiven Sessions.

---

## 📦 Konfigurationsdateien

```plaintext
db-config/container/dev/
├── .env.dev         # Umgebungsvariablen für DB-Container
└── init-test.sql    # Optionales SQL zur Prüfung von APP_DB_USER
```

---

## 🧰 Optional: Makefile-Integration

```makefile
make dev           # Startet die Umgebung
make stop          # Beendet die Umgebung
make restart       # Setzt die Umgebung neu auf
make logs          # Zeigt Logs
make status        # Zeigt Containerstatus
make init-db       # Erstellt DB-Container
make reset-db      # Setzt DB zurück
```

---

> ➕ Diese Datei kann bei Bedarf erweitert werden um `compose/`, `prod/`, `ci/` oder `local/`-Skripte.  
> Ziel: elegante Navigation, klare Zuständigkeiten, robuste Erweiterbarkeit.
