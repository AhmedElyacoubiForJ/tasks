#!/bin/bash
# =====================================================================
# 🧨 dev clean-all — entfernt Container, Images, Volumes, Networks, Cache
# =====================================================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
ENV_FILE="$ROOT_DIR/.env.compose-dev"

echo "⚠️ WARNUNG: Dies löscht ALLE compose-dev Ressourcen!"
echo "   Container, Images, Volumes, Networks, Build-Cache"
echo ""
read -p "Fortfahren? (yes/no): " confirm

if [ "$confirm" != "yes" ]; then
    echo "❌ Abgebrochen."
    exit 0
fi

# ENV laden
set -a
source "$ENV_FILE"
set +a

PROJECT_NAME=$(basename "$ROOT_DIR")
DOCKER_VOLUME="${PROJECT_NAME}_${VOLUME}"

echo "🛑 Stoppe Container..."
docker compose -f "$ROOT_DIR/docker-compose-dev.yml" --env-file "$ENV_FILE" down --rmi all --volumes --remove-orphans

echo "🧹 Entferne Volume: $DOCKER_VOLUME"
docker volume rm "$DOCKER_VOLUME" 2>/dev/null

echo "🧹 Entferne Build-Cache..."
docker builder prune -f

echo "🧹 Entferne ungenutzte Ressourcen..."
docker system prune -f

echo "✔ Alles bereinigt."
