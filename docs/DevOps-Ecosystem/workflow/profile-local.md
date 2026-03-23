## 🧪 **Workflow: Lokal starten ohne Docker (PostgreSQL läuft auf Windows)**

> Diese Umgebung ist ideal für schnelle Iterationen, UI-Entwicklung mit HTMX und Live-Reload durch Spring Devtools.
> Sie nutzt lokale Ressourcen und vermeidet Container-Overhead — perfekt für fokussiertes Entwickeln.

---

### 🔧 Voraussetzungen

- PostgreSQL läuft lokal auf dem Host-System (z.B. Port `5432`)
- Die Applikation verwendet drei Profile:
  - `local-dev` → für lokale Entwicklung mit H2
  - `test` → für automatisierte Tests
  - `local-prod` → für lokale Produktion mit PostgreSQL
- Die Datei `application.yml` enthält:
  ```yaml
  spring:
    profiles:
      default: local-dev
  ```
➡️ Ohne explizite Angabe startet die App im `local-dev`-Modus

---

### 💡 Warum diese Umgebung?

- **HTMX** ermöglicht dynamische UI-Komponenten ohne komplexes JavaScript-Framework  
  → Änderungen an HTML-Templates wirken sofort
- **Spring Devtools** sorgt für automatisches Neustarten bei Code-Änderungen  
  → Kein manuelles Rebuild nötig
- **H2-Datenbank** ist speicherbasiert und blitzschnell  
  → Ideal für UI-Entwicklung und Validierung von Interaktionen

---

### 🔨 Befehle zum Starten (mvnw || Skripte)

```bash
# 🧪 1. Entwicklung starten (H2-Datenbank, lokale Entwicklung)
SPRING_PROFILES_ACTIVE=local-dev ./mvnw spring-boot:run
./scripts/local/run-dev.sh

# 🧪 2. Tests ausführen (Testdatenbank, Unit/Integration Tests)
SPRING_PROFILES_ACTIVE=test ./mvnw test
./scripts/local/run-test.sh

# 🧪 3. Produktion simulieren (PostgreSQL, Konfiguration über .env)
export $(grep -v '^#' ./db-config/local/prod/.env | xargs)
SPRING_PROFILES_ACTIVE=local-prod ./mvnw spring-boot:run
./scripts/local/run-prod.sh
```

➡️ Die Anwendung nutzt lokale Ressourcen, keine Container.  
➡️ Ideal für schnelles Entwickeln, Testen und manuelles Validieren der Produktivkonfiguration.

---

### 📦 Empfohlene Tools für diese Umgebung

- **PostgreSQL** lokal installiert (z.B. über Windows Installer oder WSL)
- **Spring Devtools** aktiviert in `application-local-dev.yml`
- **HTMX** eingebunden in `Thymeleaf`-Templates
- **JavaFaker** für schnelle Testdaten