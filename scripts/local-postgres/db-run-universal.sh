#!/usr/bin/env bash
set -euo pipefail

SERVICE_NAME="postgresql-x64-16"

# Erkennen, ob wir in WSL sind
IS_WSL=false
if grep -qi "microsoft" /proc/version 2>/dev/null; then
  IS_WSL=true
fi

###############################################################
# Hilfsfunktion: Windows-Befehl ausführen (WSL + GitBash)
###############################################################
run_windows_cmd() {
  local cmd="$1"
  powershell.exe -Command "$cmd"
}

###############################################################
# Hilfe
###############################################################
usage() {
  echo "PostgreSQL DB-Kommandos (local-postgres):"
  echo "  ./db-run-universal.sh status"
  exit 0
}

###############################################################
# Status
###############################################################
status() {
  echo "▶ Prüfe PostgreSQL Dienststatus..."

  # Ausgabe abfangen
  local output
  output="$(run_windows_cmd "Get-Service -Name $SERVICE_NAME | Format-Table -AutoSize")"

  echo "$output"

  # Prüfen, ob Dienst gestoppt ist
  if echo "$output" | grep -q "Stopped"; then
    echo ""
    echo "⚠️  PostgreSQL Dienst ist gestoppt."
    echo "➡️  Bitte manuell starten:"
    echo "    • Windows Startmenü öffnen"
    echo "    • 'services.msc' eingeben"
    echo "    • Dienst suchen: postgresql-x64-16"
    echo "    • Rechtsklick → Starten"
    echo ""
  fi
}

###############################################################
# Routing
###############################################################
CMD="${1:-}"

case "$CMD" in
  --help)
    usage
    ;;
  status)
    status
    ;;
  *)
    echo "❌ Unbekanntes Kommando: $CMD"
    echo "ℹ️  Hilfe: ./db-run-universal.sh --help"
    exit 1
    ;;
esac
