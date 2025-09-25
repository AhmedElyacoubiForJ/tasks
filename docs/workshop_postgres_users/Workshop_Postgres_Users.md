

* die einfachen **Konzepte zu Rollen/Benutzern**,
* den kompletten **Docker-run-Workflow**,
* die **Korrektur mit Schema-Rechten**,
* und eine **Checkliste zum Prüfen der Rechte**.

---

# 🏫 Workshop: PostgreSQL-Benutzerprofile (dev / test / prod) mit Docker (postgres:alpine)

## 1️⃣ Grundwissen: Rollen & Benutzer in PostgreSQL

> In PostgreSQL sind **Benutzer nichts anderes als Rollen mit LOGIN-Recht**.

### 🔑 Wichtige Punkte
- **Rolle (Role)** = Container für Rechte.
- **Benutzer (User)** = Rolle **mit LOGIN-Recht**.
- Rechte („Privileges“) können auf **verschiedenen Ebenen** vergeben werden:
  - **Globale Attribute**: `SUPERUSER`, `CREATEDB`, `CREATEROLE` …
  - **Datenbank-Ebene**: `CONNECT`, `CREATE` (neue Schemas anlegen)
  - **Schema-Ebene**: `CREATE` (Tabellen, Views, Funktionen …)
  - **Objekt-Ebene**: `SELECT`, `INSERT`, `UPDATE`, `DELETE` auf einzelne Tabellen.

💡 **Best Practice:**  
Rollen trennen und immer nur die **minimal nötigen Rechte (Least Privilege)** vergeben.

---

## 2️⃣ Ziel

Wir legen drei Benutzerprofile an:

| Umgebung    | User        | Typische Rechte                 |
|-------------|-------------|---------------------------------|
| Entwicklung | `user_dev`  | Tabellen anlegen & Daten ändern |
| Test        | `user_test` | nur lesend (`SELECT`)           |
| Produktion  | `user_prod` | nur lesend (`SELECT`)           |

---

## 3️⃣ Projektstruktur
```

Workshop\_Postgres\_Users/
├─ .env
└─ init.sql

````

---

## 4️⃣ `.env` – sensible Zugangsdaten
> Nicht in Git committen! (`.gitignore`)

```env
POSTGRES_PASSWORD=admin_secret
POSTGRES_DB=app_db

DEV_USER=user_dev
DEV_PASSWORD=dev_secret

TEST_USER=user_test
TEST_PASSWORD=test_secret

PROD_USER=user_prod
PROD_PASSWORD=prod_secret
````

---

## 5️⃣ `init.sql` – Rollen und Rechte anlegen

```sql
-- Haupt-Datenbank ist app_db (wird vom Container automatisch erstellt)

-- 1️⃣ Benutzer anlegen (Rollen mit LOGIN)
CREATE ROLE user_dev  WITH LOGIN PASSWORD 'dev_secret';
CREATE ROLE user_test WITH LOGIN PASSWORD 'test_secret';
CREATE ROLE user_prod WITH LOGIN PASSWORD 'prod_secret';

-- 2️⃣ Datenbank-Rechte
GRANT CONNECT ON DATABASE app_db TO user_dev;
GRANT CONNECT ON DATABASE app_db TO user_test;
GRANT CONNECT ON DATABASE app_db TO user_prod;

-- dev darf eigene Schemas erstellen
GRANT CREATE ON DATABASE app_db TO user_dev;

-- 3️⃣ Schema-Rechte
-- 👉 wichtig: Tabellen liegen immer in einem Schema (standard: public)
GRANT CREATE ON SCHEMA public TO user_dev;

-- test & prod dürfen nur lesen
GRANT SELECT ON ALL TABLES IN SCHEMA public TO user_test;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO user_prod;
```

---

## 6️⃣ PostgreSQL mit Docker starten (ohne docker-compose)

Im Ordner `Workshop_Postgres_Users`:

```bash
docker run -d \
  --name pg_app \
  -e POSTGRES_PASSWORD=admin_secret \
  -e POSTGRES_DB=app_db \
  -p 5432:5432 \
  -v $(pwd)/init.sql:/docker-entrypoint-initdb.d/init.sql \
  postgres:alpine
```

✔️ Das offizielle Postgres-Image führt beim **allerersten Start** alle SQL-Dateien in
`/docker-entrypoint-initdb.d/` automatisch aus.

---

## 7️⃣ Verbindung testen

### Als Admin (`postgres`)

```bash
docker exec -it pg_app psql -U postgres -d app_db
```

In psql:

```sql
\du        -- zeigt globale Rollenattribute
\l+ app_db -- zeigt DB-Rechte
\dn+       -- zeigt Schema-Rechte
```

---

### Als user\_dev

```bash
docker exec -it -e PGPASSWORD=dev_secret pg_app psql -U user_dev -d app_db
```

Test:

```sql
CREATE TABLE demo_table (
  id SERIAL PRIMARY KEY,
  name TEXT
);
INSERT INTO demo_table (name) VALUES ('Hello Dev');
SELECT * FROM demo_table;
```

### Als user\_test (nur lesen)

```bash
docker exec -it -e PGPASSWORD=test_secret pg_app psql -U user_test -d app_db
```

Test:

```sql
SELECT * FROM demo_table;      -- ✅ sollte gehen
INSERT INTO demo_table VALUES (2,'x');  -- ❌ Permission denied
```

### Als user\_prod (nur lesen)

```bash
docker exec -it -e PGPASSWORD=prod_secret pg_app psql -U user_prod -d app_db
```

---

## 8️⃣ Wichtige Prüf-Befehle (Rechte-Audit)

| Zweck                           | psql-Befehl                      |
|---------------------------------|----------------------------------|
| Alle Rollen & globale Attribute | `\du`                            |
| Rechte auf Datenbanken          | `\l+` oder `\z app_db`           |
| Rechte auf Schemas              | `\dn+`                           |
| Rechte auf Tabellen             | `\z` oder `\z public.demo_table` |

---

## 9️⃣ Best Practices aus der Praxis

1. **Least Privilege** – Prod-User hat nur, was er unbedingt braucht.
2. **Migrations-User** – für Flyway/Liquibase separaten User mit `CREATE`-Rechten.
3. **Secrets sicher verwalten** – z. B. `.env` nie committen, in CI/CD als „Secrets“ speichern.
4. **Audits** – mit `\z` und `\dn+` regelmäßig Rechte prüfen.

---

## 10️⃣ Container-Management

| Aktion                            | Befehl                               |
|-----------------------------------|--------------------------------------|
| Container stoppen                 | `docker stop pg_app`                 |
| Container starten                 | `docker start pg_app`                |
| Container löschen (inkl. Daten)   | `docker rm -f pg_app`                |
| mit Daten-Volume statt Host-Mount | `-v pgdata:/var/lib/postgresql/data` |

---

### ✅ Zusammenfassung

* **Benutzer = Rollen mit LOGIN-Recht**.
* **CREATE ON DATABASE** ≠ **CREATE ON SCHEMA**.
* Tabellen können nur erstellt werden, wenn der User im Schema selbst `CREATE` hat.
* Mit `docker run` + `init.sql` lassen sich dev/test/prod-User und deren Rechte vollautomatisch anlegen und prüfen.
