#!/usr/bin/env bash
set -euo pipefail

###############################################################
# PostgreSQL STATUS CHECK (local-postgres)
# -------------------------------------------------------------
# Hinweis:
#   PostgreSQL muss als Windows-Dienst bereits laufen.
#   Dieses Script zeigt NUR den Status an.
###############################################################

ROOT_DIR="$(cd "$(dirname "$0")/../.." && pwd)"

###############################################################
# Farben laden (Fallback)
###############################################################
if [ -f "$ROOT_DIR/scripts/utils/colors.sh" ]; then
    # shellcheck disable=SC1091
    source "$ROOT_DIR/scripts/utils/colors.sh"
else
    RED='' GREEN='' YELLOW='' BLUE='' CYAN='' PURPLE='' NC=''
    BOLD_RED='' BOLD_GREEN='' BOLD_YELLOW='' BOLD_CYAN=''
    BG_RED='' BG_GREEN=''
fi

###############################################################
# Umgebung erkennen (WSL, GitBash, Linux, macOS)
###############################################################
# shellcheck disable=SC1091
source "$ROOT_DIR/scripts/lib/detect-env.sh"

SERVICE_NAME="postgresql-x64-16"

###############################################################
# Windows-Befehl ausführen
###############################################################
run_windows_cmd() {
  local cmd="$1"
  powershell.exe -Command "$cmd"
}

###############################################################
# Status anzeigen
###############################################################
status() {
  echo -e "${CYAN}▶ Prüfe PostgreSQL Dienststatus...${NC}"
  echo -e "${YELLOW}Hinweis: PostgreSQL muss vorher im Windows-Dienst gestartet sein.${NC}"
  echo ""

  local output
  output="$(run_windows_cmd "Get-Service -Name $SERVICE_NAME | Format-Table -AutoSize")"

  echo "$output"

  if echo "$output" | grep -q "Stopped"; then
    echo ""
    echo -e "${RED}⚠️  PostgreSQL Dienst ist gestoppt.${NC}"
    echo -e "${YELLOW}Bitte manuell starten:${NC}"
    echo "  • Windows Startmenü öffnen"
    echo "  • 'services.msc' eingeben"
    echo "  • Dienst suchen: postgresql-x64-16"
    echo "  • Rechtsklick → Starten"
    echo ""
  fi
}

###############################################################
# Routing
###############################################################
CMD="${1:-status}"

case "$CMD" in
  status)
    status
    ;;
  *)
    echo -e "${RED}❌ Unbekanntes Kommando: $CMD${NC}"
    echo "Verwendung:"
    echo "  ./db-status.sh status"
    exit 1
    ;;
esac
