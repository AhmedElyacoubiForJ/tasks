## 🧪 **Workflow: Lokal starten ohne Docker (PostgreSQL läuft auf Windows)**

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

### 🔨 Befehle zum Starten (optional mit Kommentaren pro Profil)

```bash
# 🧪 1. Entwicklung starten (H2-Datenbank, lokale Entwicklung)
SPRING_PROFILES_ACTIVE=local-dev ./mvnw spring-boot:run

# 🧪 2. Tests ausführen (Testdatenbank, Unit/Integration Tests)
SPRING_PROFILES_ACTIVE=test ./mvnw test

# 🧪 3. Produktion simulieren (PostgreSQL, Konfiguration über .env)
export $(grep -v '^#' .env | xargs)
SPRING_PROFILES_ACTIVE=local-prod ./mvnw spring-boot:run
```

➡️ Die Anwendung nutzt lokale Ressourcen, keine Container.  
➡️ Ideal für schnelles Entwickeln, Testen und manuelles Validieren der Produktivkonfiguration.
