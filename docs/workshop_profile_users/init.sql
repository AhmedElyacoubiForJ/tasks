-- Haupt-Datenbank ist app_db (wird vom Container automatisch erstellt)

-- 1️⃣ Benutzer anlegen (Rollen mit LOGIN)
-- Creating a user (role with login privilege)
CREATE ROLE user_dev  WITH LOGIN PASSWORD 'dev_secret';
CREATE ROLE user_test WITH LOGIN PASSWORD 'test_secret';
CREATE ROLE user_prod WITH LOGIN PASSWORD 'prod_secret';

-- 2️⃣ Datenbank-Rechte
GRANT CONNECT ON DATABASE app_db TO user_dev;
GRANT CONNECT ON DATABASE app_db TO user_test;
GRANT CONNECT ON DATABASE app_db TO user_prod;

-- dev darf eigene Schemas erstellen
GRANT CREATE ON DATABASE app_db TO user_dev;
GRANT CREATE ON DATABASE app_db TO user_test;

-- 3️⃣ Schema-Rechte
-- 👉 wichtig: Tabellen liegen immer in einem Schema (standard: public)
GRANT CREATE ON SCHEMA public TO user_dev;
GRANT CREATE ON SCHEMA public TO user_test;

-- test & prod dürfen nur lesen
-- GRANT SELECT ON ALL TABLES IN SCHEMA public TO user_test;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO user_prod;