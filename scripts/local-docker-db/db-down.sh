#!/bin/bash
# ----------------------------------------
# 🛑 Stoppt und entfernt die lokale Docker-DB
# ----------------------------------------

echo "🛑 Stoppe und entferne PostgreSQL-Container..."

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
ENV_FILE="$ROOT_DIR/env/.env.local-docker-db"

# ENV laden
if [[ ! -f "$ENV_FILE" ]]; then
  echo "❌ ENV-Datei nicht gefunden: $ENV_FILE"
  exit 1
fi

set -a
source "$ENV_FILE"
set +a

# ----------------------------------------
# 🛑 Container stoppen & entfernen
# ----------------------------------------

if docker ps -a --format '{{.Names}}' | grep -q "^${DB_CONTAINER}$"; then
  echo "🛑 Stoppe Container: $DB_CONTAINER"
  docker rm -f "$DB_CONTAINER" >/dev/null 2>&1
  echo "🧹 Container entfernt."
else
  echo "ℹ️ Kein laufender Container gefunden: $DB_CONTAINER"
fi

# ----------------------------------------
# 🧹 Volume optional löschen
# ----------------------------------------

if [[ "$1" == "--purge" ]]; then
  echo "🧨 Lösche Volume: $VOLUME"
  docker volume rm "$VOLUME" >/dev/null 2>&1
  echo "🧹 Volume entfernt."
else
  echo "ℹ️ Volume bleibt erhalten. Zum Löschen: ./db-down.sh --purge"
fi

echo "✅ DB heruntergefahren."
