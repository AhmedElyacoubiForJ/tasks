bash
#!/bin/bash
# Stellt sicher, dass das Skript bei einem Fehler sofort abbricht
set -e

# Führe das SQL-Kommando aus.
# psql wird mit den Superuser-Credentials ($POSTGRES_USER) ausgeführt,
# die der Container aus der .env.dev bereits kennt.
# Die Variablen $APP_DB_USER und $APP_DB_PASSWORD werden vom Docker-Befehl
# direkt in die Umgebung des Containers geladen und sind hier verfügbar.
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- Erstelle den dedizierten User für die Applikation
    CREATE USER $APP_DB_USER WITH PASSWORD '$APP_DB_PASSWORD';

    -- Gib dem neuen User alle Rechte auf der für ihn bestimmten Datenbank
    -- GRANT ALL PRIVILEGES ON DATABASE $POSTGRES_DB TO $APP_DB_USER;
    -- Datenbank-Rechte
    GRANT CONNECT ON DATABASE $POSTGRES_DB TO $APP_DB_USER;
    -- dev darf eigene Schemas erstellen
    GRANT CREATE ON DATABASE $POSTGRES_DB TO $APP_DB_USER;
    -- wichtig: Tabellen liegen immer in einem Schema (standard: public)
    GRANT CREATE ON SCHEMA public TO $APP_DB_USER;

EOSQL