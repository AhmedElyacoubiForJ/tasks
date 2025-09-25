#!/bin/bash
# ----------------------------------------
# 🧪 Kombinierter Healthcheck für App & DB
# ----------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
source "$SCRIPT_DIR/colors.sh"

ENV_FILE="$ROOT_DIR/db-config/container/dev/.env.dev"

# Prüfen, ob .env-Datei existiert
if [[ ! -f "$ENV_FILE" ]]; then
  echo -e "${RED}❌ Konfigurationsdatei nicht gefunden: $ENV_FILE${NC}"
  exit 1
fi

set -a
source "$ENV_FILE"
set +a

echo ""
echo -e "${BLUE}🔍 PostgreSQL Healthcheck:${NC}"
if docker inspect -f '{{.State.Running}}' "$DB_CONTAINER" 2>/dev/null | grep -q true; then
  docker exec "$DB_CONTAINER" pg_isready -U "$POSTGRES_USER" -d "$POSTGRES_DB"
  echo -e "${GREEN}✅ PostgreSQL läuft und ist erreichbar.${NC}"
else
  echo -e "${YELLOW}⚠️ Container '$DB_CONTAINER' läuft nicht.${NC}"
fi

echo ""
echo -e "${BLUE}🔍 App Healthcheck (/actuator/health):${NC}"
APP_HEALTH=$(curl -s "http://localhost:$APP_PORT/actuator/health" | grep '"status":"UP"')
if [[ -n "$APP_HEALTH" ]]; then
  echo -e "${GREEN}✅ App ist gesund.${NC}"
else
  echo -e "${YELLOW}⚠️ App ist nicht erreichbar oder nicht gesund.${NC}"
fi
