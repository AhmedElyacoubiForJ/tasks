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
    ├── health.sh
    ├── init-db.sh
    ├── reset-db.sh
    ├── check-env.sh
    └── colors.sh
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
| `health.sh`      | Prüft App-Health via Actuator                       | `bash scripts/docker/health.sh`    |
| `init-db.sh`     | Erstellt DB-Container inkl. Volume & Test-Tabelle   | `bash scripts/docker/init-db.sh`   |
| `reset-db.sh`    | Entfernt Container & Volume, setzt DB zurück        | `bash scripts/docker/reset-db.sh`  |
| `check-env.sh`   | Prüft `.env`-Dateien auf Vollständigkeit & Inhalt   | `bash scripts/docker/check-env.sh` |
| `colors.sh`      | Definiert Farbvariablen für konsistente Ausgaben    | Wird automatisch eingebunden       |

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
├── .env.example     # Referenzdatei mit Kommentaren & Defaults
├── init-dev-db.sh   # Initialisierungsskript für DB-User & Struktur
└── init-test.sql    # Optionales SQL zur Prüfung von APP_DB_USER
```

---

## 🧪 Validierung & CI-Fähigkeit

### `check-env.sh`

- Prüft `.env`-Dateien auf Existenz, leere Werte und fehlende Schlüssel
- Vergleicht gegen `.env.example`
- Gibt Vorschläge für fehlende Einträge aus
- CI-freundlich durch Exit-Codes

### `health.sh`

- Prüft App-Status via `GET /actuator/health`
- Wiederholt bis zu 10x mit Timeout
- Meldet `UP` oder `nicht bereit`

---

## 🧰 Makefile-Integration

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

### `make verify` führt aus:

```bash
bash scripts/docker/check-env.sh
bash scripts/docker/status.sh || true
bash scripts/docker/health.sh || true
```

---

> ➕ Diese Datei kann bei Bedarf erweitert werden um `compose/`, `prod/`, `ci/` oder `local/`-Skripte.  
> Ziel: elegante Navigation, klare Zuständigkeiten, robuste Erweiterbarkeit.
