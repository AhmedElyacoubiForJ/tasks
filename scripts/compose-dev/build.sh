#!/bin/bash
# =====================================================================
# ⚠️ WICHTIG: Dieses Skript MUSS "gesourct" werden!
# =====================================================================
#
# Warum?
# -------
# Dieses Skript lädt .env.compose-dev und exportiert alle Variablen
# in die AKTUELLE Shell-Sitzung (set -a + source).
#
# ❌ FALSCH:
#     ./scripts/compose-dev/build.sh
#
# ✅ RICHTIG:
#     source ./scripts/compose-dev/build.sh
#     . ./scripts/compose-dev/build.sh
#
# =====================================================================

echo "🔨 Baue Dev-Image..."

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

ENV_FILE="$ROOT_DIR/env/.env.compose-dev"

# ENV-Variablen exportieren
if [ -f "$ENV_FILE" ]; then
    set -a
    source "$ENV_FILE"
    set +a
    echo "✅ Umgebungsvariablen aus $ENV_FILE geladen."
else
    echo "❌ Fehler: $ENV_FILE nicht gefunden!"
    return 1 2>/dev/null || exit 1
fi

# APP_IMAGE immer dynamisch erzeugen
APP_IMAGE="${APP_NAME}:${APP_VERSION}"
echo "📦 Baue Image: ${APP_IMAGE}"

docker build \
    -t "$APP_IMAGE" \
    -f "$ROOT_DIR/Dockerfile.dev" \
    "$ROOT_DIR"

echo "🏁 Build abgeschlossen."
