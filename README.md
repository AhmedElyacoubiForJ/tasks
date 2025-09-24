# 📝 Tasks – Spring Boot Projekt zur Aufgabenverwaltung

**Tasks** ist eine produktionsreife Spring Boot Anwendung zur Verwaltung von Aufgaben.  
Sie demonstriert saubere Architektur, sichere Benutzerverwaltung und flexible Umgebungskonfiguration (Dev, Prod, CI, Test, Local).

---

## 🚀 Schnellstart für lokale Entwicklung

Für schnelles Testen mit H2-Datenbank und Live-Reload:

```bash
./run-local-dev.sh
```

> Dieses Skript startet die Anwendung mit dem Profil `local-dev`, ideal für HTMX-Integration und schnelle UI-Iterationen.

---

## 🧭 Startumgebungen

Das Projekt unterstützt mehrere Umgebungen:

- `local-dev` – lokale Entwicklung mit H2 und Hotdeployment
- `container-dev` – Docker-basierte Entwicklung mit PostgreSQL
- `container-prod` – produktionsnahe Umgebung mit sicherer Benutzertrennung
- `test` – automatisierte Tests mit isolierter Datenbank
- `ci` – Continuous Integration Setup für Pipelines

> 👉 Details und Startbefehle findest du im [Workflow-Bereich](./docs/workflow/README.md)  
> Dort findest du:
> - [`workflow-local.md`](./docs/workflow/workflow-local.md) – lokale Entwicklung mit H2 & Hotreload
> - [`workflow-docker.md`](./docs/workflow/workflow-docker.md) – Compose-basierte Umgebung mit PostgreSQL
> - `README.md` – Übersicht aller Startprofile und Empfehlungen

---

## 🧱 Projektstruktur (Auszug)

```plaintext
tasks/
├── db-config/
│   ├── container/     # dev, prod, test
│   └── local/         # lokale Skripte & Tools
├── docker-compose.yml
├── .env.prod
├── run-local-dev.sh
├── README.md
└── src/
```

---

## 🔐 Sicherheit & Benutzerrollen

- Trennung von Superuser und App-User
- Granulare Rechtevergabe in PostgreSQL
- Spring Security mit rollenbasierter Zugriffskontrolle

---

## 📦 Technologien

**Backend & Architektur:**
- **Spring Boot** – Framework für produktionsreife Java-Anwendungen
- **Spring Data JPA** – Datenbankzugriff mit Repository-Abstraktion
- **MapStruct** – Typ-sichere DTO-Mapping-Lösung
- **OpenAPI** – automatische API-Dokumentation
- **Lombok** – Reduktion von Boilerplate-Code
- **LibrePDF** – PDF-Generierung aus Java
- **JavaFaker** – Testdaten-Generierung für Entwicklung und Tests

**Frontend & UI:**
- **Thymeleaf** – serverseitiges HTML-Templating
- **HTMX** – moderne UI-Interaktionen ohne JavaScript-Frameworks
  > Ziel: dynamische Komponenten wie mit React, aber leichtgewichtig

**Entwicklung & Infrastruktur:**
- **Spring Devtools** – Hotreload und Live-Reload für lokale Entwicklung
- **PostgreSQL** – produktionsreife relationale Datenbank
- **Docker & Docker Compose** – Containerisierung und Umgebungstrennung
