#!/bin/bash
# ----------------------------------------
# 🔄 Neustart der Dev-Umgebung (Container)
# ----------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
source "$SCRIPT_DIR/colors.sh"

# Skriptpfade definieren
STOP_SCRIPT="$SCRIPT_DIR/stop.sh"
RUN_SCRIPT="$SCRIPT_DIR/dev.sh"

# ----------------------------------------
# 📦 Skriptprüfung
# ----------------------------------------

if [[ ! -f "$STOP_SCRIPT" || ! -f "$RUN_SCRIPT" ]]; then
  echo -e "${RED}❌ Fehlende Skripte: Stelle sicher, dass '$STOP_SCRIPT' und '$RUN_SCRIPT' vorhanden sind.${NC}"
  exit 1
else
  echo -e "${GREEN}✅ Beide Skripte gefunden.${NC}"
fi

# ----------------------------------------
# 🛑 Stoppen
# ----------------------------------------

echo ""
echo -e "${BLUE}🛑 Stoppe laufende Container...${NC}"
bash "$STOP_SCRIPT" <<< "n"  # Volume bleibt erhalten

sleep 2

# ----------------------------------------
# 🚀 Starten
# ----------------------------------------

echo ""
echo -e "${BLUE}🚀 Starte Container neu...${NC}"
bash "$RUN_SCRIPT"

echo ""
echo -e "${GREEN}✅ Neustart abgeschlossen.${NC}"
exit 0
