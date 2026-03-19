#!/bin/bash
#
# =====================================================================
# ⚠️ WICHTIG: Dieses Skript MUSS "gesourct" werden!
# =====================================================================
#
# Warum?
# -------
# Dieses Skript lädt .env.compose-dev und exportiert alle Variablen
# in die AKTUELLE Shell-Sitzung (set -a + source).
#
# Wenn du das Skript normal ausführst, läuft es in einem eigenen
# Subprozess – und alle Variablen gehen danach verloren.
#
# ❌ FALSCH (Variablen sterben nach dem Skript):
#     ./scripts/compose-dev/up.sh
#
# ✅ RICHTIG (Variablen bleiben in deiner Shell erhalten):
#     source ./scripts/compose-dev/up.sh
#
# ✅ Kurzform:
#     . ./scripts/compose-dev/up.sh
#
# Danach funktionieren Befehle wie:
#     echo "$VOLUME"
#
# Docker setzt automatisch ein Projektpräfix (z. B. "tasks_")
# deshalb funktionieren beide Varianten:
# docker volume inspect "$VOLUME"
# docker volume inspect "tasks_${VOLUME}"
#
# docker logs -f "$APP_CONTAINER"
# docker exec -it "$DB_CONTAINER" sh
#
# =====================================================================


echo "🚀 Starte compose-dev Umgebung..."

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

# Falls colors.sh existiert, einlesen
[ -f "$SCRIPT_DIR/colors.sh" ] && source "$SCRIPT_DIR/colors.sh"

# Pfad zur ENV-Datei
ENV_FILE="$ROOT_DIR/.env.compose-dev"

# ENV-Variablen in die aktuelle Shell exportieren
if [ -f "$ENV_FILE" ]; then
    set -a
    source "$ENV_FILE"
    set +a
    echo "✅ Umgebungsvariablen aus $ENV_FILE geladen."
else
    echo "❌ Fehler: $ENV_FILE nicht gefunden!"
    exit 1
fi

echo "📦 Volume: ${VOLUME}"

# Compose starten
docker compose -f "$ROOT_DIR/docker-compose-dev.yml" --env-file "$ENV_FILE" up -d
