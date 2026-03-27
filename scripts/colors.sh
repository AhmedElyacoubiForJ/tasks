#!/bin/bash
# ----------------------------------------
# 🎨 Farbdefinitionen für Bash-Ausgabe
# ----------------------------------------
# Diese Datei kann in andere Skripte eingebunden werden:
# source "$SCRIPT_DIR/colors.sh"
# Danach stehen die Farbvariablen zur Verfügung:
# echo -e "${GREEN}✅ Erfolg${NC}"

# Farben
RED='\033[0;31m'      # Fehler / kritisch
GREEN='\033[0;32m'    # Erfolg / OK
YELLOW='\033[1;33m'   # Warnung / Hinweis
BLUE='\033[0;34m'     # Info / neutral
NC='\033[0m'          # Reset / keine Farbe
