[//]: # (scripts/regression/README.md)
# Regression Test Runner

> Dieses Verzeichnis enthält den universellen Regression‑Test‑Runner für das Projekt.
> Das Script `run` sorgt dafür, dass die vollständige Test‑Suite stabil ausgeführt wird –
> unabhängig davon, ob du in WSL, Git Bash oder Linux/macOS arbeitest.

---

## Ziele

- Regression‑Tests sollen **immer funktionieren**, egal von wo sie gestartet werden.
- WSL‑ und Windows‑Interop wird automatisch gehandhabt.
- Die Tests warten automatisch, bis die App erreichbar ist.
- Maven wird korrekt über `mvnw.cmd` (Windows) oder `mvnw` (Linux/macOS) ausgeführt.

---

## Aufruf

Der Script führt automatisiert die gewünschten Test-Suites aus und wartet zuvor darauf, dass die API erreichbar ist.

```bash
./scripts/regression/run
```

## Verhalten

Das Script:
1. Erkennt automatisch die Umgebung (*WSL*, *Git Bash*, *Linux*, *macOS*)
2. Ermittelt die korrekte API-URL
   - unter Windows: **http://localhost:8080**
   - unter WSL: **http://<Windows-IP>:8080**
3. Wartet, bis die App "UP" meldet
    - Standard-Timeout: 60 Sekunden
4. Führt die gewünschte Test-Suite aus
    - Standard-Suite: TaskApiFullTestSuite

---

## ⚙️ Optionen

--wait <Sekunden>

Legt fest, wie lange auf die API gewartet wird, bevor das Script abbricht.

Default: 60

### Beispiel:

```bash
./scripts/regression/run --wait 10
```

---

--Suite <Name>

Wählt die Test-Suite aus, die ausgeführt werden soll.

### Default:

```code
TaskApiFullTestSuite
```

### Verfügbare Suites:
- TaskApiFullTestSuite
- TaskListsCrudTestSuite
- TaskListsTasksCrudSuite
- TaskListsUseCaseTestSuite

### Beispiel

```bash
./scripts/regression/run --suite TaskListsCrudTestSuite
```

---

## 🔀 Optionen kombinieren

```bash
./scripts/regression/run --wait 15 --suite TaskListsUseCaseTestSuite
```

---

## 🧪 Beispielausgabe

- Header-Box beim Start
- Health-Check mit Spinner
- Erfolgs- oder Fehler-Box
- Ausführung des gewälten Suite
- Footer-Box „ALL TESTS EXECUTED“

---

## Features

- Automatische Umgebungserkennung
- Automatischer Health‑Check der API
- Stabile Maven‑Ausführung über Wrapper
- Vollständig WSL‑aware

---

## Warum besondere Behandlung unter WSL?

Unter WSL laufen die Regression-Tests in Linux, aber die App läuft in Windows.  
Daher:

- Die API ist **nicht** unter `localhost` erreichbar
- Stattdessen muss die Windows-Host-IP verwendet werden
- Diese wird automatisch über `ipconfig.exe` ermittelt


## Warum wird `mvnw.cmd` unter Windows ausgeführt?

Der Maven Wrapper (`mvnw.cmd`) ist ein Windows-Tool.  
Wenn man ihn unter WSL ausführt, passieren folgende Probleme:

- Pfade werden falsch interpretiert
- `.mvn/wrapper` wird nicht gefunden
- Wrapper-Properties können nicht geladen werden
- Tests starten nicht

Darum:

- Unter WSL → `cmd.exe /C mvnw.cmd`
- Unter Linux/macOS → `./mvnw`

## Voraussetzungen

- App muss bereits laufen (Port 8080)
- Docker optional (für DB)
- Java & Maven Wrapper im Projekt vorhanden

---

## Architektur (Kurzüberblick)

- App läuft unter Windows  
   (IntelliJ, Git Bash, Java, mvnw.cmd)
- PostgreSQL läuft in WSL  
  (Docker)
- Regression‑Tests laufen immer in WSL,
  auch wenn sie aus Git Bash gestartet werden
  → sie rufen mvnw.cmd über cmd.exe auf
- *local-docker-db/run* ist der zentrale Router für alle lokalen Dev‑Kommandos

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

# Warum so einfach?
- Das Regression‑Script ist jetzt voll WSL‑aware
- Es erkennt selbst, ob es mvnw.cmd oder mvnw ausführen muss
- Erkennt selbst die korrekte API‑URL
- Benötigt keine Git‑Bash‑Interop

Es ist vollständig eigenständig – darum reicht ein einziger Aufruf.