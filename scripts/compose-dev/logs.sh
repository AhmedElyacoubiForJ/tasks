#!/bin/bash
set -e

echo "📜 Starte ENV-Diagnose für compose-dev Umgebung..."

# ----------------------------------------
# 🔍 Schritt 1: Parser-Check (docker compose config)
# ----------------------------------------
echo "🔍 Prüfe, ob der Compose-Parser alle ENV-Variablen kennt..."

# Versuche docker-compose config mit explizitem env-file
docker compose -f docker-compose-dev.yml --env-file .env.compose-dev config > /dev/null

if [ $? -eq 0 ]; then
  echo "✅ Compose-Parser erkennt alle ENV-Variablen."
else
  echo "⚠️ Compose-Parser hat Probleme mit ENV-Auflösung. Prüfe .env.compose-dev!"
fi

# ----------------------------------------
# 🔍 Schritt 2: Container-ENV prüfen
# ----------------------------------------
echo "🔍 Prüfe, ob tasks-app Container die ENV-Variablen geladen hat..."

# Liste der kritischen Variablen
VARS=("SPRING_PROFILES_ACTIVE" "APP_DB_USER" "APP_DB_PASSWORD")

for VAR in "${VARS[@]}"; do
  VALUE=$(docker exec tasks-app printenv "$VAR" 2>/dev/null || echo "<nicht gefunden>")
  echo "🔧 $VAR = $VALUE"
done

# ----------------------------------------
# 📜 Schritt 3: Logs anzeigen
# ----------------------------------------
#echo "📜 Zeige Logs über Compose (Service: app)..."
#docker compose -f docker-compose-dev.yml --env-file .env.compose-dev logs app

#echo "📜 Alternativ: direkte Container-Logs (Container: tasks-app)..."
#docker logs tasks-app

echo "📜 Versuche Logs über Compose (Service: app)..."
COMPOSE_LOGS=$(docker compose -f docker-compose-dev.yml --env-file .env.compose-dev logs app 2>/dev/null)

if [ -z "$COMPOSE_LOGS" ]; then
  echo "⚠️ Keine Logs über Compose gefunden. Wechsle zu direkten Container-Logs..."
  echo "📜 Alternativ: direkte Container-Logs (Container: tasks-app)..."
  docker logs -f tasks-app
else
  echo "$COMPOSE_LOGS"
fi

