#!/bin/bash
# -----------------------------------------------------------------------------
# 🎨 Farbdefinitionen für Bash-Ausgabe
# -----------------------------------------------------------------------------
# scripts/utils/colors.sh
# Standard-Farben
RED='\033[0;31m'      # Fehler / kritisch
GREEN='\033[0;32m'    # Erfolg / OK
YELLOW='\033[1;33m'   # Warnung / Hinweis
BLUE='\033[0;34m'     # Info / neutral
CYAN='\033[0;36m'     # Tipps / URLs / Pfade
PURPLE='\033[0;35m'   # Spezial-Hinweise
NC='\033[0m'          # No Color (Reset)

# Fette (Bold) Varianten für Header
BOLD_RED='\033[1;31m'
BOLD_GREEN='\033[1;32m'
BOLD_YELLOW='\033[1;33m'
BOLD_CYAN='\033[1;36m'

# Hintergrundfarben (optional, nützlich für auffällige Warnungen)
BG_RED='\033[41m'
BG_GREEN='\033[42m'
