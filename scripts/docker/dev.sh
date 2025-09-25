#!/bin/bash
# ----------------------------------------
# 🚀 Startet die containerisierte Dev-Umgebung
# ----------------------------------------
echo "🚀 Starte containerisierte Dev-Umgebung..."

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
source "$SCRIPT_DIR/colors.sh"

ENV_FILE="$ROOT_DIR/db-config/container/dev/.env.dev"
INIT_SCRIPT="$ROOT_DIR/db-config/container/dev/init-dev-db.sh"
LOG_FILE="$ROOT_DIR/logs/dev-start.log"

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
# 🧹 Alte Container & Volumes entfernen
# ----------------------------------------

docker rm -f "$DB_CONTAINER" "$APP_CONTAINER" 2>/dev/null
docker volume rm "$VOLUME" 2>/dev/null

# ----------------------------------------
# 🧱 Starte PostgreSQL-Container
# ----------------------------------------

docker run --name "$DB_CONTAINER" \
  -p "$DB_PORT":5432 \
  -e SPRING_PROFILES_ACTIVE="$SPRING_PROFILES_ACTIVE" \
  --env-file "$ENV_FILE" \
  -v "$INIT_SCRIPT":/docker-entrypoint-initdb.d/init-dev-db.sh \
  -v "$VOLUME":/var/lib/postgresql/data \
  -d "$DB_IMAGE"

echo -e "${BLUE}⏳ Warte bis PostgreSQL bereit ist...${NC}"
until docker exec "$DB_CONTAINER" pg_isready -U "$APP_DB_USER" -d "$DB_NAME"; do
  echo -e "${YELLOW}🕒 PostgreSQL noch nicht bereit...${NC}"
  sleep 2
done

echo -e "${GREEN}✅ PostgreSQL ist bereit — starte App-Container...${NC}"

# ----------------------------------------
# 🚀 Starte App-Container
# ----------------------------------------

docker run -d --name "$APP_CONTAINER" \
  --env-file "$ENV_FILE" \
  -p "$APP_PORT":8080 \
  "$APP_IMAGE"

# ----------------------------------------
# 📄 Logging
# ----------------------------------------

mkdir -p "$ROOT_DIR/logs"
echo "[$(date '+%Y-%m-%d %H:%M:%S')] Dev-Container gestartet" >> "$LOG_FILE"
docker ps --filter "name=$DB_CONTAINER" --filter "name=$APP_CONTAINER" >> "$LOG_FILE"
echo -e "${BLUE}📄 Log gespeichert unter $LOG_FILE${NC}"

# ----------------------------------------
# 🩺 App-Healthcheck
# ----------------------------------------

echo ""
echo -e "${BLUE}🩺 Prüfe App-Health über Actuator...${NC}"
for i in {1..10}; do
  HEALTH=$(curl -s "http://localhost:$APP_PORT/actuator/health" | grep '"status":"UP"')
  if [[ -n "$HEALTH" ]]; then
    echo -e "${GREEN}✅ App ist gesund${NC}"
    break
  else
    echo -e "${YELLOW}🕒 Versuch $i: App noch nicht bereit...${NC}"
    sleep 3
  fi
done

if [[ -z "$HEALTH" ]]; then
  echo -e "${RED}⚠️ App-Health konnte nicht bestätigt werden${NC}"
fi

# ----------------------------------------
# 🧠 Interaktive DB-Konsole
# ----------------------------------------

echo ""
read -p "❓ Möchten Sie direkt in die Datenbank springen? [y/N]: " open_db
if [[ "$open_db" == "y" || "$open_db" == "Y" ]]; then
  echo -e "${BLUE}🧠 Öffne PostgreSQL-Konsole...${NC}"
  docker exec -it "$DB_CONTAINER" psql -U "$APP_DB_USER" -d "$DB_NAME"
else
  echo -e "${GREEN}🏁 Setup abgeschlossen. Viel Erfolg beim Entwickeln!${NC}"
fi