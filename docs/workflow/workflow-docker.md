## 🐳 **Workflow: Docker & Docker Compose**

> Die Applikation kann containerised betrieben werden — entweder direkt über Dockerfile & Makefile oder orchestriert über Docker Compose.
> Beide Varianten sind produktionsnah und CI-kompatibel.

1. **Docker-only Workflow** → App und DB manuell starten mit Dockerfile und Makefile
2. **Docker Compose Workflow** → App und DB gemeinsam orchestrieren mit `docker-compose.yml`

---

### 🐳 **Teil 1: Starten mit Dockerfile & Makefile (ohne Compose)**

#### 🔧 Voraussetzungen

- PostgreSQL läuft separat im Container oder lokal
- Dockerfile für die App ist vorhanden (`Dockerfile`)
- Makefile enthält Build- und Run-Kommandos
- Umgebungsvariablen werden über `.env` oder direkt gesetzt

#### 🔨 Beispiel-Befehle

```bash
# 1. App-Image bauen
make build

# 2. App starten mit dev-Profil
SPRING_PROFILES_ACTIVE=dev make run

# 3. App starten mit test-Profil
SPRING_PROFILES_ACTIVE=test make run

# 4. App starten mit prod-Profil (z.B. über .env)
export $(grep -v '^#' .env | xargs)
SPRING_PROFILES_ACTIVE=prod make run
```

➡️ Die App läuft isoliert im Container, aber die Datenbank muss separat gestartet werden (z.B. via `docker run postgres` oder lokal auf Windows).

---

### 🐳 **Teil 2: Starten mit Docker Compose**

#### 🔧 Voraussetzungen

- `docker-compose.yml` ist vorhanden
- Optional: Erweiterungen wie `docker-compose.test.yml`, `docker-compose.prod.yml`
- App und PostgreSQL werden gemeinsam gestartet
- Umgebungsvariablen können über `.env` geladen werden

#### 🔨 Beispiel-Befehle

```bash
# 1. Entwicklung starten (dev-Profil)
SPRING_PROFILES_ACTIVE=dev docker-compose up --build

# 2. Tests ausführen (test-Profil)
SPRING_PROFILES_ACTIVE=test docker-compose -f docker-compose.yml -f docker-compose.test.yml up --build

# 3. Produktion starten (prod-Profil, detached)
export $(grep -v '^#' .env | xargs)
SPRING_PROFILES_ACTIVE=prod docker-compose -f docker-compose.yml -f docker-compose.prod.yml up --build --detach
```

➡️ App und Datenbank laufen gemeinsam im Containerverbund. Ideal für Integrationstests, produktionsnahe Simulation und spätere CI/CD-Anbindung.

---

## ✅ Fazit

| Variante           | Vorteil                             | Voraussetzung             |
|--------------------|--------------------------------------|---------------------------|
| Dockerfile + Make  | Schnell, flexibel, manuell steuerbar | Separater DB-Start        |
| Docker Compose     | Komplett orchestriert, CI-ready      | Compose-Dateien vorhanden |

Du kannst beide Varianten unabhängig testen — und später entscheiden, welche du in CI übernimmst.  
Wenn du willst, helfe ich dir beim Schreiben eines `Makefile`-Ziels für `prod`, `test`, `dev` oder beim Aufsetzen von `docker-compose.test.yml`. Sag einfach Bescheid, sobald du bereit bist.