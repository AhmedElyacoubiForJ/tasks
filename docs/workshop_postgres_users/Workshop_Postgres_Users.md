> Hier ist **einen vollständigen kleinen Workshop** – **ohne docker-compose**, nur mit `docker run`.
> Es enthält **Erklärungen**, **alle Befehle**, **SQL-Skripte** und die **typischen Best Practices**.

---

# 🏫 Workshop: PostgreSQL-Benutzerprofile (dev / test / prod) mit Docker (postgres\:alpine)

---

## 1️⃣ Grundwissen: Benutzer & Rollen in PostgreSQL

> **Merksatz:** In PostgreSQL gibt es keinen Unterschied zwischen „Benutzer“ und „Rolle“.

### 🔑 Wichtige Punkte

* **Rolle (Role)** = Container für Rechte.
* **Benutzer (User)** = Rolle **mit LOGIN-Recht**.
* Rechte („Privileges“) z. B.:

    * `CREATE DATABASE` – Datenbanken anlegen
    * `CREATE` – Tabellen anlegen
    * `SELECT`, `INSERT`, `UPDATE`, `DELETE` – auf Daten zugreifen

💡 **Best Practice:**
Erstelle wenige „Rollen mit Rechten“ und weise diese Rollen den einzelnen Usern zu → Rechte einfach wiederverwendbar.

---

## 2️⃣ Ziel des Workshops

Wir legen drei typische Benutzerprofile an:

| Umgebung    | User-Name      | Rechte                  |
| ----------- | -------------- | ----------------------- |
| Entwicklung | **user\_dev**  | darf Tabellen erstellen |
| Test        | **user\_test** | darf nur lesen          |
| Produktion  | **user\_prod** | darf nur lesen          |

---

## 3️⃣ Projektstruktur

```
Workshop_Postgres_Users/
├─ init.sql
└─ .env
```

---

## 4️⃣ .env – sensible Zugangsdaten

> Erstelle die Datei `Workshop_Postgres_Users/.env` (nicht in Git committen!)

```env
POSTGRES_PASSWORD=admin_secret
POSTGRES_DB=app_db

DEV_USER=user_dev
DEV_PASSWORD=dev_secret

TEST_USER=user_test
TEST_PASSWORD=test_secret

PROD_USER=user_prod
PROD_PASSWORD=prod_secret
```

---

## 5️⃣ init.sql – SQL Skript zum Anlegen der Benutzer

> Datei: `Workshop_Postgres_Users/init.sql`
> (PostgreSQL ersetzt hier keine Variablen automatisch – setze feste Werte oder nutze `envsubst`.)

```sql
-- Haupt-Datenbank ist app_db (wird von POSTGRES_DB erzeugt)

-- 1️⃣ Benutzer anlegen
CREATE ROLE user_dev  WITH LOGIN PASSWORD 'dev_secret';
CREATE ROLE user_test WITH LOGIN PASSWORD 'test_secret';
CREATE ROLE user_prod WITH LOGIN PASSWORD 'prod_secret';

-- 2️⃣ Rechte vergeben
GRANT CONNECT ON DATABASE app_db TO user_dev;
GRANT CONNECT ON DATABASE app_db TO user_test;
GRANT CONNECT ON DATABASE app_db TO user_prod;

-- user_dev darf auch Tabellen anlegen
GRANT CREATE ON DATABASE app_db TO user_dev;

-- test und prod dürfen nur lesen
GRANT SELECT ON ALL TABLES IN SCHEMA public TO user_test;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO user_prod;
```

---

## 6️⃣ Docker Container starten (ohne docker-compose)

### 6.1. Starte den Container

```bash
docker run -d \
  --name pg_app \
  -e POSTGRES_PASSWORD=admin_secret \
  -e POSTGRES_DB=app_db \
  -p 5432:5432 \
  -v $(pwd)/init.sql:/docker-entrypoint-initdb.d/init.sql \
  postgres:alpine
```

✔️ Der Ordner `/docker-entrypoint-initdb.d/` ist ein **Feature des offiziellen Postgres-Images**:
Alle SQL-Dateien dort werden **beim allerersten Start** ausgeführt.

---

### 6.2. Prüfen, ob die Benutzer angelegt wurden

```bash
docker exec -it pg_app psql -U postgres -d app_db
```

Dann im psql-Client:

```sql
\du
```

➡️ Zeigt alle Rollen/Benutzer.

---

### 6.3. Test: als user\_dev einloggen

Öffne eine neue Shell:

```bash
docker exec -it -e PGPASSWORD=dev_secret pg_app psql -U user_dev -d app_db
```

---

## 7️⃣ Typische Best Practices

1. **Least Privilege**:
   Prod-User nur minimale Rechte (meist nur `SELECT`, ggf. `INSERT`).

2. **Migrationen** (Flyway, Liquibase):
   Erstelle einen separaten „migrations“-User mit `CREATE`-Rechten nur für den CI/CD Prozess.

3. **Secrets nicht im Code**:
   Passwörter immer über `.env` oder Secret-Management (z. B. GitHub Actions Secrets) verwalten.

4. **Backups & Audits**:
   Rechteänderungen dokumentieren.

---

## 8️⃣ Nützliche Kommandos

| Aktion                          | Befehl                                       |
| ------------------------------- | -------------------------------------------- |
| Container stoppen               | `docker stop pg_app`                         |
| Container starten               | `docker start pg_app`                        |
| Container löschen (inkl. Daten) | `docker rm -f pg_app`                        |
| Datenvolumen beibehalten        | Mount eines Docker Volumes statt `-v $(pwd)` |

---

## 🎯 Zusammenfassung

* In PostgreSQL sind **Benutzer nichts anderes als Rollen mit LOGIN**.
* Wir trennen **user\_dev**, **user\_test**, **user\_prod** für verschiedene Umgebungen.
* Mit `docker run` + `init.sql` werden Benutzer und Rechte **automatisch beim ersten Start** angelegt.

➡️ Jetzt hast du ein **komplettes, reproduzierbares Setup**, das du direkt ausführen und als Referenz-Workshop weitergeben kannst.
