#!/bin/bash
# ----------------------------------------
# 🔄 Setzt die Dev-Datenbank zurück (Container & Volume)
# ----------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
source "$SCRIPT_DIR/colors.sh"

ENV_FILE="$ROOT_DIR/db-config/container/dev/.env.dev"

# ENV laden
if [[ ! -f "$ENV_FILE" ]]; then
  echo -e "${RED}❌ Konfigurationsdatei nicht gefunden: $ENV_FILE${NC}"
  exit 1
else
  echo -e "${GREEN}✅ ENV-Datei gefunden: $ENV_FILE${NC}"
fi

set -a
source "$ENV_FILE"
set +a

echo ""
echo -e "${BLUE}🧹 Entferne Container & Volume...${NC}"

docker rm -f "$DB_CONTAINER" 2>/dev/null && \
  echo -e "${YELLOW}🔻 Container '$DB_CONTAINER' entfernt.${NC}" || \
  echo -e "${BLUE}ℹ️ Container '$DB_CONTAINER' war nicht aktiv.${NC}"

docker volume rm "$VOLUME" 2>/dev/null && \
  echo -e "${YELLOW}🧹 Volume '$VOLUME' entfernt.${NC}" || \
  echo -e "${BLUE}ℹ️ Volume '$VOLUME' war nicht vorhanden.${NC}"

echo ""
echo -e "${GREEN}✅ Datenbank wurde zurückgesetzt.${NC}"
exit 0
