#!/bin/bash
# scripts/local-docker-db/db-up.sh
# ----------------------------------------
# 🚀 Startet die containerisierte Postgres-DB
# ----------------------------------------

echo "🚀 Starte containerisierte Postgres..."

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

ENV_FILE="$ROOT_DIR/env/.env.local-docker-db"
INIT_SCRIPT="$ROOT_DIR/docker/postgres/init-dev-db.sh"

# ----------------------------------------
# 📦 ENV laden & prüfen
# ----------------------------------------

if [[ ! -f "$ENV_FILE" ]]; then
  echo "❌ ENV-Datei nicht gefunden: $ENV_FILE"
  exit 1
else
  echo "✅ ENV-Datei gefunden: $ENV_FILE"
fi

set -a
source "$ENV_FILE"
set +a

# ----------------------------------------
# 🧹 Alte Container & Volumes entfernen
# ----------------------------------------

echo "🧹 Entferne alte Container & Volumes (falls vorhanden)..."
docker rm -f "$DB_CONTAINER" 2>/dev/null
docker volume rm "$VOLUME" 2>/dev/null

# ----------------------------------------
# 🧱 Starte PostgreSQL-Container
# ----------------------------------------

echo "🐳 Starte PostgreSQL-Container: $DB_CONTAINER"

docker run --name "$DB_CONTAINER" \
  --env-file "$ENV_FILE" \
  -e POSTGRES_DB="$POSTGRES_DB" \
  -p "$DB_PORT":5432 \
  -v "$INIT_SCRIPT":/docker-entrypoint-initdb.d/init-dev-db.sh \
  -v "$VOLUME":/var/lib/postgresql \
  -d "$DB_IMAGE"
#-v "$VOLUME":/var/lib/postgresql/data \
# ----------------------------------------
# ⏳ Warten bis DB bereit ist
# ----------------------------------------

echo "⏳ Warte bis PostgreSQL bereit ist..."

until docker exec "$DB_CONTAINER" pg_isready -U "$POSTGRES_USER" -d "$POSTGRES_DB" >/dev/null 2>&1; do
  echo "🕒 PostgreSQL noch nicht bereit..."
  sleep 2
done

echo "✅ PostgreSQL ist bereit!"
