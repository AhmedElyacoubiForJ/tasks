#!/bin/bash
# ----------------------------------------
# 🔍 Zeigt Status der Dev-Container & prüft Health
# ----------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
source "$SCRIPT_DIR/colors.sh"

ENV_FILE="$ROOT_DIR/db-config/container/dev/.env.dev"

# Prüfen, ob .env-Datei existiert
if [[ ! -f "$ENV_FILE" ]]; then
  echo -e "${RED}❌ Konfigurationsdatei nicht gefunden: $ENV_FILE${NC}"
  exit 1
else
  echo -e "${GREEN}✅ Konfigurationsdatei gefunden: $ENV_FILE${NC}"
fi

# Variablen aus .env laden
set -a
source "$ENV_FILE"
set +a

echo ""
echo -e "${BLUE}🔍 Container-Status:${NC}"
docker ps -a --filter "name=$DB_CONTAINER" --filter "name=tasks-app" \
  --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo ""
echo -e "${BLUE}🧪 PostgreSQL Healthcheck:${NC}"
if docker inspect -f '{{.State.Running}}' "$DB_CONTAINER" 2>/dev/null | grep -q true; then
  docker exec "$DB_CONTAINER" pg_isready -U "$POSTGRES_USER" -d "$POSTGRES_DB"
  echo -e "${GREEN}✅ PostgreSQL läuft und ist erreichbar.${NC}"
  exit 0
else
  echo -e "${YELLOW}⚠️ Container '$DB_CONTAINER' läuft nicht.${NC}"
  exit 2
fi