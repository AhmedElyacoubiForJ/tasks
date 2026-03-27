#!/bin/bash
# ----------------------------------------
# 🧪 Testet die lokale Docker-DB
# ----------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
ENV_FILE="$ROOT_DIR/env/.env.local-docker-db"

# ENV laden
set -a
source "$ENV_FILE"
set +a

echo "🔍 Teste Datenbank: $DB_NAME"
echo "🔍 Teste User: $APP_DB_USER"
echo "----------------------------------------"

# 1️⃣ Test: Existiert die DB?
echo "🧪 Prüfe, ob Datenbank existiert..."
docker exec "$DB_CONTAINER" psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -tAc \
  "SELECT 1 FROM pg_database WHERE datname='$DB_NAME';" | grep -q 1

if [[ $? -eq 0 ]]; then
  echo "✅ Datenbank '$DB_NAME' existiert."
else
  echo "❌ Datenbank '$DB_NAME' existiert NICHT!"
  exit 1
fi

# 2️⃣ Test: Existiert der User?
echo "🧪 Prüfe, ob User '$APP_DB_USER' existiert..."
docker exec "$DB_CONTAINER" psql -U "$POSTGRES_USER" -d "$DB_NAME" -tAc \
  "SELECT 1 FROM pg_roles WHERE rolname='$APP_DB_USER';" | grep -q 1

if [[ $? -eq 0 ]]; then
  echo "✅ User '$APP_DB_USER' existiert."
else
  echo "❌ User '$APP_DB_USER' existiert NICHT!"
  exit 1
fi

# 3️⃣ Test: Hat der User Rechte? (CREATE TABLE)
echo "🧪 Teste Schreibrechte von '$APP_DB_USER'..."

docker exec "$DB_CONTAINER" psql -U "$APP_DB_USER" -d "$DB_NAME" -tAc \
  "CREATE TABLE IF NOT EXISTS test_permissions (id SERIAL PRIMARY KEY, name TEXT);"

if [[ $? -eq 0 ]]; then
  echo "✅ User '$APP_DB_USER' hat Schreibrechte."
else
  echo "❌ User '$APP_DB_USER' hat KEINE Schreibrechte!"
  exit 1
fi

echo "----------------------------------------"
echo "🎉 Alle Tests erfolgreich!"
