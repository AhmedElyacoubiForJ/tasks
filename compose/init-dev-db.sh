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

# docker run --name postgres-dev -p 5432:5432 --env-file ./db-config/container/dev/.env.dev -v $(pwd)/db-config/container/dev/init-dev-db.sh:/docker-entrypoint-initdb.d/init-dev-db.sh -d postgres:alpine
#  export $(grep -v '^#' ./db-config/container/dev/.env.dev | xargs)
#docker run --name $(CONTAINER_NAME) \
#-p $(DB_PORT):$(CONTAINER_PORT) \
#--env-file ./db-config/container/dev/.env.dev \
#-v $(pwd)/db-config/container/dev/init-dev-db.sh:/docker-entrypoint-initdb.d/init-dev-db.sh \
# -v $(VOLUME):/var/lib/postgresql/data \
#-d $(DB_IMAGE)

# docker run --name postgres-dev \
  #> -p 5432:5432 \
  #> --env-file ./db-config/container/dev/.env.dev \
  #> -v $(pwd)/db-config/container/dev/init-dev-db.sh:/docker-entrypoint-initdb.d/init-dev-db.sh \
  #> -v pgdata-dev:/var/lib/postgresql/data \
  #> -d postgres:alpine