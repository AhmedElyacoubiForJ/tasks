# User-Profile & Automatisierung** 🧑‍🏫🐳  

- Wie PostgreSQL-User beim Containerstart entstehen
- Warum sie verschwinden, wenn du den Container löschst
- Wie du das **dauerhaft und automatisiert** lösen kannst

---

## 🧩 **Step 1: Workshop-Setup**

### 🔧 Wir definieren:

```dotenv
# Datei: .env.workshop
DB_CONTAINER=pg-workshop
DB_IMAGE=postgres:alpine
DB_PORT=5433
POSTGRES_USER=user_dev
POSTGRES_PASSWORD=dev_pass
POSTGRES_DB=postgres
WORKSHOP_VOLUME=pgdata-workshop
```

➡️ Port 5433, damit es nicht mit deinem Dev-Container kollidiert  
➡️ Volume `pgdata-workshop`, damit wir alles isoliert testen

---

## 🧩 **Step 2: Container starten**

```bash
docker rm -f pg-workshop 2>/dev/null
docker volume rm pgdata-workshop 2>/dev/null

docker run -d --name pg-workshop \
  --env-file .env.workshop \
  -v pgdata-workshop:/var/lib/postgresql/data \
  -p 5433:5432 \
  postgres:alpine
```

➡️ Jetzt läuft ein frischer Container mit Volume `pgdata-workshop`

---

## 🧩 **Step 3: Ist-Zustand analysieren**

### 🔍 Was passiert beim Start?

- PostgreSQL liest `POSTGRES_USER=user_dev`
- Erstellt **nur beim allerersten Start**:
    - Rolle `user_dev`
    - Passwort `dev_pass`
    - Datenbank `postgres` (oder `POSTGRES_DB`, falls gesetzt)

### ❗ Wichtig:

- Diese Initialisierung passiert **nur**, wenn `/var/lib/postgresql/data` **leer ist**
- Sobald das Volume existiert, wird **nichts mehr neu erstellt**
- Wenn du den Container löschst, aber das Volume **nicht**, bleibt alles erhalten
- Wenn du das Volume **auch löschst**, ist alles weg → beim nächsten Start wird `user_dev` neu erstellt

---

## 🧩 **Step 4: Testen**

```bash
docker exec -it pg-workshop psql -U user_dev -d postgres -c "\du"
```

➡️ Du solltest `user_dev` sehen

Jetzt:
```bash
docker rm -f pg-workshop
docker run -d --name pg-workshop \
  --env-file .env.workshop \
  -v pgdata-workshop:/var/lib/postgresql/data \
  -p 5433:5432 \
  postgres:alpine
```

➡️ **Kein neuer User wird erstellt**, weil das Volume schon Daten enthält

---

Perfekt, Ahmed — wir erweitern **Step 4: Testen** jetzt so, dass du wirklich verstehst, wie PostgreSQL mit Volumes und Benutzern umgeht. Ziel: **prüfen, ob `user_dev` im Volume gespeichert wurde oder verschwindet**, wenn der Container gelöscht wird.

---

## 🧩 Step 4 (erweitert): Benutzerpersistenz im Volume testen

### 🔧 1. Container stoppen und löschen

```bash
docker rm -f pg-workshop
```

➡️ Der Container ist jetzt weg, aber das Volume `pgdata-workshop` existiert noch.

---

### 🔍 2. Neuen Container starten — **ohne POSTGRES_USER**

```bash
docker run -d --name pg-workshop \
  -v pgdata-workshop:/var/lib/postgresql/data \
  -p 5433:5432 \
  postgres:alpine
```

➡️ Jetzt starten wir den Container **ohne `.env.workshop`**, also ohne Initialisierung.  
Der Container verwendet den Standard-User `postgres`.

---

### 🧪 3. Prüfen, ob `user_dev` noch existiert

```bash
docker exec -it pg-workshop psql -U postgres -d postgres -c "\du"
```

➡️ Du bekommst eine Liste aller Rollen. Wenn `user_dev` **noch da ist**, bedeutet das:

✅ **Benutzer wurde im Volume gespeichert**  
❌ Wenn `user_dev` fehlt, wurde er **nicht persistent gespeichert**

---

## 🧠 Erklärung: Warum Benutzer manchmal verschwinden

- PostgreSQL speichert **alle Rollen und Datenbanken im Volume**
- Aber: Die Initialisierung (`POSTGRES_USER`, `POSTGRES_DB`) passiert **nur**, wenn das Volume **leer ist**
- Wenn du den Container **neu startest mit leerem Volume**, wird `user_dev` neu erstellt
- Wenn du den Container **neu startest mit vorhandenem Volume**, passiert **keine Initialisierung**
- Und wenn `user_dev` **nicht im Volume war**, ist er **weg**

---

## ✅ Fazit dieses Tests

| Aktion                        | Ergebnis                                  |
|------------------------------|-------------------------------------------|
| Container gelöscht, Volume bleibt | `user_dev` bleibt erhalten (wenn korrekt erstellt) |
| Container neu gestartet ohne Env | `user_dev` bleibt erhalten, wenn im Volume |
| Volume gelöscht              | `user_dev` wird beim nächsten Start neu erstellt (wenn `POSTGRES_USER` gesetzt) |

---

Wenn du willst, können wir jetzt ein `db-user-init.sh` schreiben, das prüft, ob ein Benutzer existiert — und ihn bei Bedarf neu anlegt. Oder wir dokumentieren diesen Workshop als Markdown für dein Team. Sag einfach, wie du weitermachen willst 💼🧠🐳

---

## 🧩 **Step 5: Automatisierungsidee**

Wenn du willst, können wir ein Skript schreiben:

- prüft, ob `user_dev` existiert
- erstellt ihn, falls nicht
- kann auch Rollen, Rechte, Datenbanken automatisiert anlegen

➡️ So kannst du später mit `container-dev`, `container-prod`, `container-test` alles automatisieren

---

## ✅ Fazit

| Aktion                         | Ergebnis                              |
|-------------------------------|----------------------------------------|
| Container löschen             | User bleibt erhalten (wenn Volume da) |
| Volume löschen                | User wird beim nächsten Start neu erstellt |
| Volume behalten               | Keine neue Initialisierung            |
| Automatisierung nötig         | Für komplexe User- und DB-Setups      |

---

Wollen wir jetzt ein `db-user-init.sh` schreiben, das prüft und ggf. `user_dev` neu anlegt? Oder lieber gleich mit `container-prod` weitermachen? Du gibst den Takt vor 💼🧠🐳