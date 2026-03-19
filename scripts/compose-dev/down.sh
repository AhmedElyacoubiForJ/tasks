#!/bin/bash
# =====================================================================
# ⚠️ Dieses Skript MUSS gesourct werden!
# =====================================================================
#
# Nutzung:
#   down            → stoppt Container
#   down clean      → stoppt Container + löscht Volumes
#
# =====================================================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
ENV_FILE="$ROOT_DIR/.env.compose-dev"

# ----------------------------------------
# 🔧 ENV laden
# ----------------------------------------
if [ -f "$ENV_FILE" ]; then
    set -a
    source "$ENV_FILE"
    set +a
else
    echo "❌ Fehler: $ENV_FILE nicht gefunden!"
    return 1 2>/dev/null || exit 1
fi

# ----------------------------------------
# 🧪 Parser-Check
# ----------------------------------------
echo "🧪 Prüfe docker-compose Syntax..."
docker compose -f "$ROOT_DIR/docker-compose-dev.yml" --env-file "$ENV_FILE" config >/dev/null

if [ $? -eq 0 ]; then
    echo "✅ docker-compose Syntax OK"
else
    echo "❌ Fehler in docker-compose-dev.yml"
    return 1 2>/dev/null || exit 1
fi

# ----------------------------------------
# 🛑 Container stoppen
# ----------------------------------------
echo "🛑 Stoppe compose-dev Umgebung..."
docker compose -f "$ROOT_DIR/docker-compose-dev.yml" --env-file "$ENV_FILE" down
echo "✅ Container gestoppt."

# ----------------------------------------
# 🧹 Optional: Volumes löschen (einfach & korrekt)
# ----------------------------------------
if [ "$1" = "clean" ]; then
    echo "🧹 Entferne Volumes..."

    # Projektname ermitteln (Compose nutzt Ordnername, wenn nicht gesetzt)
    PROJECT_NAME=$(basename "$ROOT_DIR")

    # Echten Docker-Volume-Namen bauen
    DOCKER_VOLUME="${PROJECT_NAME}_${VOLUME}"

    echo "📦 Volume (ENV): $VOLUME"
    echo "📦 Volume (Docker): $DOCKER_VOLUME"

    docker volume rm "$DOCKER_VOLUME" 2>/dev/null \
        && echo "   ✔ Volume gelöscht." \
        || echo "   ⚠️ Volume bereits entfernt oder nicht vorhanden."

    echo "✅ Volumes entfernt."
fi

echo "🏁 down.sh abgeschlossen."



#echo "🛑 Stoppe compose-dev Umgebung..."
#docker compose -f docker-compose-dev.yml --env-file .env.compose-dev down