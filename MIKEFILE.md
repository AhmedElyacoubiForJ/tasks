> Dokumentation des aktuellen Docker-Workflows als **`MAKEFILE.md`** – inklusive Erklärungen zu jedem Befehl
> und einem klaren Verweis auf die zugehörige `Makefile`.

---

# 📘 MAKEFILE.md

## 🧩 Projektüberblick

Dieses Projekt verwendet zwei Docker-Container:

- `postgres:alpine` für die Datenbank
- `myimage:tasks-app` für die Spring Boot Anwendung

> Da `docker-compose` aktuell nicht stabil funktioniert, wird die Umgebung **direkt mit Docker-Befehlen** gesteuert.  
> Das `Makefile` dient als Kommandozentrale für wiederholbare Aktionen wie Start, Stop, Backup und Build.

---

## 📂 Makefile-Speicherort

Die Datei heißt **`Makefile`** (ohne Endung) und liegt im Hauptverzeichnis des Projekts:

```
projektordner/
├── Makefile
├── .env
├── Dockerfile
├── backup/
├── init.sql
└── src/
```

---

## ⚙️ Befehle im Makefile

| Befehl         | Beschreibung                                                                |
|----------------|-----------------------------------------------------------------------------|
| `make help`    | Zeigt alle verfügbaren Befehle                                              |
| `make db-up`   | Startet den PostgreSQL-Container mit Volume und Umgebungsvariablen          |
| `make app-up`  | Startet die Spring Boot App mit Port-Mapping und `.env`                     |
| `make stop`    | Stoppt und entfernt beide Container                                         |
| `make backup`  | Erstellt ein `.tar.gz` Backup des Datenbank-Volumes mit Zeitstempel         |
| `make restore` | Stellt ein Backup wieder her (Parameter: `FILE=...`)                        |
| `make status`  | Zeigt alle laufenden Container                                              |
| `make logs`    | Zeigt die Logs des App-Containers                                           |
| `make logs-db` | Zeigt die Logs des Datenbank-Containers                                     |
| `make build`   | Baut das App-Image neu aus dem lokalen Dockerfile                           |
| `make test`    | Führt Healthchecks für App & Datenbank aus                                  |
| `make init-db` | Führt ein SQL-Skript (`init.sql`) in der Datenbank aus                      |
| `make version` | Zeigt die aktuelle Version des Projekts: z.B. Git-Tag, Zeitstempel          |
| `make deploy`  | Simuliert ein Deployment-Prozess (Build, Tagging, Push)                     |
| `make clean`   | Entfernt Backups, gestoppte Container und ungenutzte Images                 |
| `make release` | Baut ein neues Image und taggt es mit Zeitstempel                           |
| `make about`   | Zeigt eine Übersicht über die Fähigkeiten des Systems                       |

---

## 🧪 Beispiel: Anwendung starten

```bash
make db-up     # Datenbank starten
make app-up    # App starten
make status    # Container prüfen
```

---

## 🧪 make test

➡️ Führt einfache Tests aus, um zu prüfen, ob die App und die Datenbank korrekt laufen.  
Das umfasst z.B. einen `curl`-Request auf `/actuator/health` und einen `psql`-Befehl zur Anzeige der Tabellen.

```bash
make test
```

---

## 📦 Beispiel: Backup erstellen

```bash
make backup
```

➡️ Das Backup wird unter `./backup/pgdata-backup-YYYY-MM-DD_HH-MM.tar.gz` gespeichert.

---

## 🔁 Beispiel: Backup wiederherstellen

```bash
make restore FILE=pgdata-backup-2025-09-09_13-00.tar.gz
```

➡️ Stellt das angegebene Backup in das Volume `pgdata` zurück.

---

## 🧱 Beispiel: SQL-Skript ausführen

```bash
make init-db
```

➡️ Führt die Datei `init.sql` in der Datenbank `tasks_db` aus.

---

## 📜 Beispiel: Logs anzeigen

```bash
make logs     # App-Logs
make logs-db  # Datenbank-Logs
```

---

## 🚀 Beispiel: Deployment & Release

```bash
make deploy   # Simuliert Deployment
make release  # Baut & taggt neues Release
```

---

## 🧹 Beispiel: Aufräumen

```bash
make clean
```

➡️ Entfernt alte Backups, gestoppte Container und ungenutzte Images.

---

## 📘 Beispiel: Übersicht anzeigen

```bash
make about
```

➡️ Zeigt eine charmante Zusammenfassung aller Funktionen des Systems.

---

## 📌 Hinweise

- Stelle sicher, dass `make` installiert ist (`sudo apt install make`)
- Alle Befehle im Makefile müssen mit **echtem Tabulator** eingerückt sein
- Die Datei muss exakt `Makefile` heißen (ohne `.txt`, `.sh`, etc.)

---

## 🔗 Link zur Datei

Da man lokal arbeitet, ist der Pfad zur Datei einfach:

```
./Makefile
```

Man kann sie jederzeit erweitern oder anpassen.

---

> Später werden wir auf `docker compose` umsteigen und das Makefile um neue Targets wie `make compose-up`, `make compose-down`, `make migrate`, etc. ergänzen.  
> Du hast jetzt ein stabiles, elegantes Steuerpult für dies Projekt — mit Persönlichkeit und Power 💪🧠