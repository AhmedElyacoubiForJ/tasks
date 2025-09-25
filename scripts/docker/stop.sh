#!/bin/bash
# ----------------------------------------
# 🛑 Stoppt und entfernt Dev-Container & Volume
# ----------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
source "$SCRIPT_DIR/colors.sh"

ENV_FILE="$ROOT_DIR/db-config/container/dev/.env.dev"

# ENV laden
if [[ ! -f "$ENV_FILE" ]]; then
  echo -e "${RED}❌ ENV-Datei nicht gefunden: $ENV_FILE${NC}"
  exit 1
else
  echo -e "${GREEN}✅ ENV-Datei gefunden: $ENV_FILE${NC}"
fi

set -a
source "$ENV_FILE"
set +a

echo ""
echo -e "${BLUE}🛑 Stoppe Dev-Container...${NC}"

# ----------------------------------------
# 🔻 App-Container stoppen und entfernen
# ----------------------------------------

if docker ps -a --format '{{.Names}}' | grep -q "^$APP_CONTAINER$"; then
  echo -e "${YELLOW}🔻 Entferne App-Container: $APP_CONTAINER${NC}"
  docker rm -f "$APP_CONTAINER"
else
  echo -e "${BLUE}ℹ️ App-Container '$APP_CONTAINER' läuft nicht.${NC}"
fi

# ----------------------------------------
# 🔻 DB-Container stoppen und entfernen
# ----------------------------------------

if docker ps -a --format '{{.Names}}' | grep -q "^$DB_CONTAINER$"; then
  echo -e "${YELLOW}🔻 Entferne DB-Container: $DB_CONTAINER${NC}"
  docker rm -f "$DB_CONTAINER"
else
  echo -e "${BLUE}ℹ️ DB-Container '$DB_CONTAINER' läuft nicht.${NC}"
fi

# ----------------------------------------
# 📦 Volume optional entfernen
# ----------------------------------------

echo ""
read -p "❓ Volume '$VOLUME' auch löschen? [y/N]: " confirm
if [[ "$confirm" == "y" || "$confirm" == "Y" ]]; then
  echo -e "${YELLOW}🧹 Entferne Volume: $VOLUME${NC}"
  docker volume rm "$VOLUME"
else
  echo -e "${BLUE}📦 Volume bleibt erhalten: $VOLUME${NC}"
fi

echo ""
echo -e "${GREEN}✅ Dev-Umgebung wurde gestoppt.${NC}"
exit 0
