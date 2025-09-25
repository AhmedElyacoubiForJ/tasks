#!/bin/bash
# ----------------------------------------
# 📦 Prüft .env-Dateien auf Existenz, Inhalt & Vollständigkeit
# ----------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
source "$SCRIPT_DIR/colors.sh"

ENV_PATHS=(
  "$ROOT_DIR/db-config/container/dev/.env.dev"
  "$ROOT_DIR/db-config/container/prod/.env.prod"
  "$ROOT_DIR/db-config/container/dev/.env.test"
)

EXAMPLE_FILE="$ROOT_DIR/db-config/container/dev/.env.example"

echo -e "${BLUE}🔍 Prüfe .env-Dateien:${NC}"
for FILE in "${ENV_PATHS[@]}"; do
  if [[ -f "$FILE" ]]; then
    echo -e "${GREEN}✅ Gefunden: $FILE${NC}"

    # Leere Zeilen oder unvollständige Einträge
    EMPTY=$(grep -v '^#' "$FILE" | grep -E '^\s*$')
    if [[ -n "$EMPTY" ]]; then
      echo -e "${YELLOW}⚠️ Leere Zeilen oder unvollständige Einträge in: $FILE${NC}"
    fi

    # Schlüsselprüfung gegen .env.example
    if [[ -f "$EXAMPLE_FILE" ]]; then
      echo -e "${BLUE}🔍 Vergleiche mit Referenz: $EXAMPLE_FILE${NC}"
      while IFS= read -r line; do
        if [[ "$line" =~ ^[A-Z_]+= ]]; then
          KEY="${line%%=*}"
          DEFAULT="${line#*=}"
          VALUE=$(grep "^$KEY=" "$FILE" | cut -d '=' -f2-)
          if [[ -z "$VALUE" ]]; then
            echo -e "${YELLOW}⚠️ Schlüssel '$KEY' fehlt oder ist leer in: $FILE${NC}"
            echo -e "${BLUE}💡 Vorschlag: $KEY=$DEFAULT${NC}"
          fi
        fi
      done < <(grep -v '^#' "$EXAMPLE_FILE")
    fi

  else
    echo -e "${RED}❌ Nicht gefunden: $FILE${NC}"
  fi
  echo ""
done

echo -e "${GREEN}✅ ENV-Prüfung abgeschlossen.${NC}"
exit 0
