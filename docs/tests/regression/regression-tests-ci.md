# CI – Ausblick

Die Regressionstests sollen später automatisiert in GitHub Actions laufen.

## Ziele
- Anwendung im CI starten (local-dev oder compose-dev)
- Regressionstests automatisch ausführen
- Ergebnisse als Pflichtlauf vor jedem Merge

## Wichtig
Auch im CI benötigen die Tests **keine** Test‑YAMLs.  
Sie testen die API wie ein externer Client.


[//]: # (# Regression Test CI – Übersicht & Versionierung)

[//]: # ()
[//]: # (> Dieses Dokument beschreibt die Entwicklung unserer lokalen Regression-Test-Pipeline.)

[//]: # (> Die YAML-Dateien selbst enthalten ausführliche Kommentare und dienen als primäre technische Referenz.  )

[//]: # (> Hier dokumentieren wir die Architektur, Motivation und Unterschiede der Versionen.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## CI‑Historie)

[//]: # ()
[//]: # (### v1 – Proof of Concept &#40;local-dev-regression-tests-ci-v1.yml&#41;)

[//]: # (> **Ziel:** Schnell eine funktionierende CI haben, die das Projekt baut und grundlegende Tests ausführt.)

[//]: # ()
[//]: # (**Merkmale:**)

[//]: # (- Ein einziger Job &#40;Build + Tests&#41;.)

[//]: # (- Anwendung wird gestartet, Healthcheck geprüft, Tests laufen.)

[//]: # (- Diente als Basis zum Verstehen des Ablaufs.)

[//]: # ()
[//]: # (**Einschränkungen:**)

[//]: # (- Langsam, da alles in einem Job läuft.)

[//]: # (- Fehlerdiagnose schwierig &#40;CRUD/Tasks/UseCases nicht getrennt&#41;.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### v2 – Optimierte Single-Job-CI &#40;local-dev-regression-tests-ci-v2.yml&#41;)

[//]: # (> **Ziel:** Stabilität, Geschwindigkeit und bessere Lesbarkeit.)

[//]: # ()
[//]: # (**Verbesserungen:**)

[//]: # (- Maven Cache aktiviert → schnellerer Build.)

[//]: # (- mvnw‑Rechte sauber gesetzt.)

[//]: # (- Healthcheck verbessert.)

[//]: # (- YAML optisch strukturiert &#40;Emojis, klare Steps&#41;.)

[//]: # (- Logs werden bei Fehlern ausgegeben.)

[//]: # ()
[//]: # (**Warum v2 wichtig war:**)

[//]: # (- Stabile Grundlage für spätere Parallelisierung.)

[//]: # (- Deutlich bessere Developer‑Experience im GitHub Actions UI.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (### v3 – Parallele, modulare Test-Pipeline &#40;local-dev-regression-tests-ci-v3.yml&#41;)

[//]: # (> **Ziel:** Tests trennen, CI beschleunigen, Fehler schneller erkennen.)

[//]: # ()
[//]: # (**Architektur:**)

[//]: # (- **build-jar** Job baut einmal das JAR und stellt es als Artifact bereit.)

[//]: # (- Drei parallele Testjobs:)

[//]: # (    - `crud-tests`)

[//]: # (    - `tasks-crud-tests`)

[//]: # (    - `usecase-tests`)

[//]: # (- Jeder Job startet die App separat &#40;local-dev Profil&#41;.)

[//]: # (- Jeder Job führt nur seine Suite aus.)

[//]: # ()
[//]: # (**Vorteile:**)

[//]: # (- 🚀 Schnellere Laufzeiten &#40;Parallelisierung&#41;.)

[//]: # (- 🎯 Klare Fehlerdiagnose &#40;Suite-spezifisch&#41;.)

[//]: # (- 🧱 Stabilere Pipeline &#40;UseCases blockieren CRUD nicht mehr&#41;.)

[//]: # (- 📈 Skalierbar für wachsende Testlandschaften.)

[//]: # (- 🧩 Vorbereitung für v4 &#40;compose-dev mit echter Postgres&#41;.)

[//]: # ()
[//]: # (**Hinweis:**  )

[//]: # (> Die YAML-Datei enthält ein ASCII-Diagramm, das die Pipeline visualisiert.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## v4 – compose-dev-CI &#40;docker-dev Profil&#41; – *geplant*)

[//]: # (> **Ziel:** Tests gegen eine realistische Umgebung &#40;Docker Compose + Postgres&#41;.)

[//]: # ()
[//]: # (**Geplante Architektur:**)

[//]: # (- Docker Compose startet:)

[//]: # (    - Postgres)

[//]: # (    - Spring Boot App &#40;Profil: `docker-dev`&#41;)

[//]: # (- Tests laufen gegen echte DB &#40;keine H2 mehr&#41;.)

[//]: # (- Flyway/Liquibase-Migrationen werden real geprüft.)

[//]: # (- Optionale Parallelisierung wie in v3.)

[//]: # ()
[//]: # (**Warum v4 später kommt:**)

[//]: # (- Erst v3 stabilisieren.)

[//]: # (- Docs aufräumen.)

[//]: # (- UseCase-Tests lokal finalisieren.)

[//]: # (- Dann Compose-Setup sauber integrieren.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## Zusammenfassung)

[//]: # (- **v1 → v2 → v3** bilden eine klare, nachvollziehbare CI-Evolution.)

[//]: # (- Jede Version verbessert Stabilität, Geschwindigkeit und Struktur.)

[//]: # (- Die YAML-Dateien sind bewusst ausführlich kommentiert und dienen als technische Referenz.)

[//]: # (- **v4** wird die CI produktionsähnlich machen, sobald die Zeit reif ist.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## Dateien)

[//]: # (- `local-dev-regression-tests-ci-v1.yml`)

[//]: # (- `local-dev-regression-tests-ci-v2.yml`)

[//]: # (- `local-dev-regression-tests-ci-v3.yml`)

[//]: # (- `docker-dev-regression-tests-ci-v4.yml` *&#40;folgt später&#41;*)

[//]: # ()
