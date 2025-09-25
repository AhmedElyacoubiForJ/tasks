#!/bin/bash
# ----------------------------------------
# 📄 Zeigt Logs der App oder Datenbank
# ----------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
source "$SCRIPT_DIR/colors.sh"

ENV_FILE="$ROOT_DIR/db-config/container/dev/.env.dev"

# ENV laden
if [[ ! -f "$ENV_FILE" ]]; then
  echo -e "${RED}❌ ENV-Datei nicht gefunden: $ENV_FILE${NC}"
  exit 1
fi

set -a
source "$ENV_FILE"
set +a

# ----------------------------------------
# 📡 Auswahlmenü
# ----------------------------------------

echo ""
echo -e "${BLUE}📄 Logs anzeigen:${NC}"
echo "1) App-Logs ($APP_CONTAINER)"
echo "2) DB-Logs  ($DB_CONTAINER)"
read -p "❓ Welche Logs möchtest du sehen? [1/2]: " choice

# ----------------------------------------
# 🚀 Logs anzeigen
# ----------------------------------------

if [[ "$choice" == "1" ]]; then
  echo -e "${BLUE}📡 Zeige Logs von $APP_CONTAINER...${NC}"
  docker logs -f "$APP_CONTAINER"
elif [[ "$choice" == "2" ]]; then
  echo -e "${BLUE}📡 Zeige Logs von $DB_CONTAINER...${NC}"
  docker logs -f "$DB_CONTAINER"
else
  echo -e "${RED}❌ Ungültige Auswahl: '$choice'${NC}"
  exit 1
fi
