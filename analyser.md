Du hast jetzt **den entscheidenden Punkt erreicht**:  
Compose‑dev läuft, Swagger UI lädt, aber **der erste echte API‑Call liefert 500**.  
Das bedeutet: **Docker‑Setup ist korrekt**, aber **die App selbst hat ein Problem** – meistens:

- DB‑Connection falsch
- Profil nicht aktiv
- Tabellen fehlen
- Flyway/Liquibase nicht gelaufen
- Hostname falsch (`localhost` statt `db`)
- Init‑Script nicht ausgeführt
- Fehler im Repository/Service

Damit wir das sauber lösen, brauchen wir **die Logs der App**.  
Ohne Logs stochern wir im Nebel.

---

# 🔍 Schritt 1 — Bitte gib mir die App‑Logs

Führe das aus:

```bash
docker compose -f docker-compose-dev.yml logs -f app
```

oder:

```bash
docker logs -f tasks-app
```

Und kopiere **die ersten 50–80 Zeilen** hier rein, besonders:

- Startup‑Phase
- Profil‑Info
- DB‑Connection
- Flyway/Liquibase
- Exceptions

Damit kann ich dir **sofort** sagen, wo der Fehler liegt.

---

# 🧠 Warum der Fehler fast sicher in der DB‑Konfiguration liegt

Du hast gesagt:

> TaskList abfragen → 500

Das bedeutet:

- Controller läuft
- Swagger läuft
- App startet
- aber Repository/DB‑Layer wirft Exception

Die häufigsten Ursachen:

### 1) **JDBC‑URL falsch**
In Docker Compose muss der Host **db** heißen, nicht localhost.

Beispiel:

```
spring.datasource.url=jdbc:postgresql://db:5432/tasks_dev_db
```

Wenn da noch `localhost` steht → 500.

### 2) **Profil nicht aktiv**
Wenn `SPRING_PROFILES_ACTIVE=compose-dev` nicht greift, nutzt Spring evtl. H2 oder Prod‑Config.

### 3) **Tabellen fehlen**
Wenn Flyway/Liquibase nicht läuft → Repository wirft Fehler.

### 4) **Init‑Script nicht ausgeführt**
Wenn dein `init-dev-db.sh` nicht korrekt ist → DB leer.

### 5) **User/Passwort falsch**
Wenn App falsche Credentials nutzt → Connection refused.

---

# 🧪 Schritt 2 — Bitte teste noch einen Call

Rufe im Browser auf:

```
http://localhost:8080/actuator/health
```

Wenn:

- **UP** → App läuft
- **DOWN** → DB‑Connection kaputt

Das bestätigt die Richtung.

---

# 🧭 Schritt 3 — Wir analysieren gemeinsam

Sobald du mir die Logs gibst, kann ich dir **in wenigen Minuten** sagen:

- wo der Fehler liegt
- wie du ihn behebst
- ob `.env.compose-dev` korrekt ist
- ob dein Profil geladen wird
- ob Flyway/Liquibase läuft
- ob die DB‑URL stimmt

---

Schick mir bitte **die App‑Logs**, dann lösen wir das sofort.