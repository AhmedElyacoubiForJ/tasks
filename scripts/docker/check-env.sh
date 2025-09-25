#!/bin/bash
# ----------------------------------------
# 📦 Prüft .env-Dateien auf Existenz & Inhalt
# ----------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
source "$SCRIPT_DIR/colors.sh"

ENV_PATHS=(
  "$ROOT_DIR/db-config/container/dev/.env.dev"
  "$ROOT_DIR/db-config/container/prod/.env.prod"
  "$ROOT_DIR/db-config/container/dev/.env.test"
)

echo -e "${BLUE}🔍 Prüfe .env-Dateien:${NC}"
for FILE in "${ENV_PATHS[@]}"; do
  if [[ -f "$FILE" ]]; then
    echo -e "${GREEN}✅ Gefunden: $FILE${NC}"
    MISSING=$(grep -v '^#' "$FILE" | grep -E '^\s*$')
    if [[ -n "$MISSING" ]]; then
      echo -e "${YELLOW}⚠️ Leere Zeilen oder unvollständige Einträge in: $FILE${NC}"
    fi
  else
    echo -e "${RED}❌ Nicht gefunden: $FILE${NC}"
  fi
done
