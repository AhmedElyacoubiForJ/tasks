#!/bin/bash
# ----------------------------------------
# 🔄 Setzt die Dev-Datenbank zurück (Container & Volume)
# ----------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
ENV_FILE="$ROOT_DIR/db-config/container/dev/.env.dev"

if [[ ! -f "$ENV_FILE" ]]; then
  echo "❌ Konfigurationsdatei nicht gefunden: $ENV_FILE"
  exit 1
fi

set -a
source "$ENV_FILE"
set +a

echo "🧹 Entferne Container & Volume..."
docker rm -f "$DB_CONTAINER" 2>/dev/null
docker volume rm "$VOLUME" 2>/dev/null

echo "✅ Datenbank zurückgesetzt."
