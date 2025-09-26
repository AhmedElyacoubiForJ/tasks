#!/bin/bash
# ----------------------------------------
# 🧱 Initialisiert PostgreSQL-Container für die Dev-Umgebung
# ----------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
source "$SCRIPT_DIR/colors.sh"

ENV_FILE="$ROOT_DIR/db-config/container/dev/.env.dev"
INIT_SCRIPT="$ROOT_DIR/db-config/container/dev/init-dev-db.sh"
INIT_SQL="$ROOT_DIR/db-config/container/dev/init-test.sql"

# ----------------------------------------
# 📦 ENV laden & prüfen
# ----------------------------------------

if [[ ! -f "$ENV_FILE" ]]; then
  echo -e "${RED}❌ ENV-Datei nicht gefunden: $ENV_FILE${NC}"
  exit 1
else
  echo -e "${GREEN}✅ ENV-Datei gefunden: $ENV_FILE${NC}"
fi

set -a
source "$ENV_FILE"
set +a

# ----------------------------------------
# 🧹 Alte Container & Volume entfernen
# ----------------------------------------

echo -e "${BLUE}🧹 Entferne alten Container & Volume (falls vorhanden)...${NC}"
docker rm -f "$DB_CONTAINER" 2>/dev/null
docker volume rm "$VOLUME" 2>/dev/null

# ----------------------------------------
# 🚀 Starte neuen PostgreSQL-Container mit Init-Skript
# ----------------------------------------

echo -e "${BLUE}🚀 Starte PostgreSQL-Container mit Init-Skript...${NC}"
docker run -d --name "$DB_CONTAINER" \
  --env-file "$ENV_FILE" \
  -v "$VOLUME":/var/lib/postgresql/data \
  -v "$INIT_SCRIPT":/docker-entrypoint-initdb.d/init-dev-db.sh \
  -p "$DB_PORT":5432 \
  "$DB_IMAGE"

echo -e "${BLUE}⏳ Warte auf PostgreSQL...${NC}"
sleep 5

# ----------------------------------------
# 🧠 Prüfe, ob Datenbank existiert
# ----------------------------------------

DB_EXISTS=$(docker exec "$DB_CONTAINER" \
  psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  -tc "SELECT 1 FROM pg_database WHERE datname='$DB_NAME'" | grep -q 1 && echo "yes")

if [[ "$DB_EXISTS" != "yes" ]]; then
  echo -e "${YELLOW}⚠️ Datenbank '$DB_NAME' existiert nicht — wird erstellt...${NC}"
  docker exec "$DB_CONTAINER" \
    psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
    -c "CREATE DATABASE $DB_NAME"
else
  echo -e "${GREEN}✅ Datenbank '$DB_NAME' existiert bereits.${NC}"
fi

# ----------------------------------------
# 🧪 Optionales Initialisierungsskript ausführen
# ----------------------------------------

if [[ -f "$INIT_SQL" ]]; then
  echo -e "${BLUE}🧪 Führe Initialisierungsskript aus mit APP_DB_USER: $INIT_SQL${NC}"
  docker exec -i "$DB_CONTAINER" \
    psql -U "$APP_DB_USER" -d "$DB_NAME" < "$INIT_SQL"
  echo -e "${GREEN}✅ Initialisierung abgeschlossen.${NC}"
else
  echo -e "${YELLOW}⚠️ Kein Initialisierungsskript gefunden: $INIT_SQL${NC}"
fi

echo -e "${GREEN}🏁 PostgreSQL-Setup abgeschlossen.${NC}"
#exit 0

# ----------------------------------------
# 🔍 Prüfe, ob Tabelle existiert
# ----------------------------------------

echo ""
echo -e "${BLUE}🔍 Prüfe, ob Tabelle 'demo_table' existiert in '$DB_NAME'...${NC}"

TABLE_EXISTS=$(docker exec "$DB_CONTAINER" \
  psql -U "$APP_DB_USER" -d "$DB_NAME" \
  -tc "SELECT to_regclass('public.demo_table')" | grep -q demo_table && echo "yes")

if [[ "$TABLE_EXISTS" == "yes" ]]; then
  echo -e "${GREEN}✅ Tabelle 'demo_table' ist vorhanden.${NC}"
else
  echo -e "${YELLOW}⚠️ Tabelle 'demo_table' wurde nicht gefunden.${NC}"
fi

exit 0
# 🐳 Analyse deines PostgreSQL-Containers
# 🔍 1. Container-Status prüfen
# docker ps --filter "name=tasks-db"

# 📦 2. Volumes anzeigen
# docker volume ls
# ➡️ Du solltest pgdata-dev sehen — dein persistentes Volume

# docker inspect tasks-db | grep pgdata
# ➡️ Zeigt dir, wo das Volume im Container gemountet ist (/var/lib/postgresql/data)

# 🧠 3. Datenbank- und Rollenstruktur prüfen
# docker exec -it tasks-db psql -U dev_user -d tasks_dev_db -c "\l"
# ➡️ Zeigt dir alle Datenbanken

# docker exec -it tasks-db psql -U dev_user -d tasks_dev_db -c "\du"
# ➡️ Zeigt dir alle Rollen (z.B. dev_user, postgres)

# 📁 4. Konfigurationsdateien im Container (optional)
# docker exec -it tasks-db ls /var/lib/postgresql/data
# ➡️ Zeigt dir, ob z.B. pg_hba.conf, postgresql.conf vorhanden sind (für spätere Tuning-Zwecke)

# TODO db-status.sh

# 🔍 1. Aktive Volumes identifizieren
# docker ps -a --format "table {{.Names}}\t{{.Status}}"
# docker inspect tasks-db | grep pgdata
# ➡️ So findest du heraus, welche Container welche Volumes gerade nutzen.

# 🧹 2. Unbenutzte Volumes löschen
# docker volume prune
# ➡️ Entfernt alle Volumes, die nicht mit laufenden Containern verbunden sind. Du wirst vorher gefragt.
#
# Oder gezielt:
# docker volume rm <volume-name>

# 🧠 3. Naming-Strategie vereinheitlichen
# Dev: pgdata-dev
# Test: pgdata-test
# Prod: pgdata-prod

# 🛠️ Bonus: Volume-Check-Skript