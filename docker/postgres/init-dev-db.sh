#!/bin/bash
set -e

# =====================================================================
# init-dev-db.sh – Initialisiert die Entwicklungsdatenbank
# =====================================================================
# Dieses Skript wird vom offiziellen Postgres-Image automatisch ausgeführt,
# wenn der Container das erste Mal startet.
#
# Verfügbare ENV-Variablen (kommen aus .env.compose-dev):
#   POSTGRES_USER        → Superuser (admin)
#   POSTGRES_PASSWORD    → Passwort des Superusers
#   POSTGRES_DB          → Hauptdatenbank
#   APP_DB_USER          → App-User
#   APP_DB_PASSWORD      → Passwort des App-Users
#
# Ziel:
#   - App-User anlegen (falls nicht vorhanden)
#   - Rechte auf Datenbank & Schema vergeben
# Logs:
#   - docker logs postgres-dev
#   - live docker logs -f postgres-dev
# =====================================================================

echo "🏗️ Initialisiere Entwicklungsdatenbank…"

# User idempotent anlegen
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL

    DO \$\$
    BEGIN
        IF NOT EXISTS (
            SELECT FROM pg_catalog.pg_roles WHERE rolname = '$APP_DB_USER'
        ) THEN
            RAISE NOTICE 'Erstelle User: $APP_DB_USER';
            CREATE USER $APP_DB_USER WITH PASSWORD '$APP_DB_PASSWORD';
        ELSE
            RAISE NOTICE 'User $APP_DB_USER existiert bereits – überspringe.';
        END IF;
    END
    \$\$;

    -- Rechte auf Datenbank
    GRANT CONNECT ON DATABASE $POSTGRES_DB TO $APP_DB_USER;
    GRANT CREATE ON DATABASE $POSTGRES_DB TO $APP_DB_USER;

    -- Rechte auf Schema public
    GRANT USAGE ON SCHEMA public TO $APP_DB_USER;
    GRANT CREATE ON SCHEMA public TO $APP_DB_USER;

EOSQL

echo "✅ Datenbank-Initialisierung abgeschlossen."















## init-dev-db.sh
#bash
##!/bin/bash
## Stellt sicher, dass das Skript bei einem Fehler sofort abbricht
#set -e
#
## Führe das SQL-Kommando aus.
## psql wird mit den Superuser-Credentials ($POSTGRES_USER) ausgeführt,
## die der Container aus der .env.compose.dev bereits kennt.
## Die Variablen $APP_DB_USER und $APP_DB_PASSWORD werden vom Docker-Befehl
## direkt in die Umgebung des Containers geladen und sind hier verfügbar.
#psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
#    -- Erstelle den dedizierten User für die Applikation
#    CREATE USER $APP_DB_USER WITH PASSWORD '$APP_DB_PASSWORD';
#
#    -- Gib dem neuen User alle Rechte auf der für ihn bestimmten Datenbank
#    -- GRANT ALL PRIVILEGES ON DATABASE $POSTGRES_DB TO $APP_DB_USER;
#    -- Datenbank-Rechte
#    GRANT CONNECT ON DATABASE $POSTGRES_DB TO $APP_DB_USER;
#    -- dev darf eigene Schemas erstellen
#    GRANT CREATE ON DATABASE $POSTGRES_DB TO $APP_DB_USER;
#    -- wichtig: Tabellen liegen immer in einem Schema (standard: public)
#    GRANT CREATE ON SCHEMA public TO $APP_DB_USER;
#
#EOSQL