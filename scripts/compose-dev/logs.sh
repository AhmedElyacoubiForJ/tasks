#!/bin/bash
# =====================================================================
# ⚠️ Dieses Skript MUSS gesourct werden!
# =====================================================================
#
# Nutzung:
#   logs app     → App-Logs
#   logs db      → DB-Logs
#   logs all     → App + DB Logs
#   logs env     → ENV-Variablen der Container
#   logs check   → Parser-Check + Container-ENV-Check
#
# =====================================================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
ENV_FILE="$ROOT_DIR/env/.env.compose-dev"

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
# 🧪 Funktion: Parser-Check
# ----------------------------------------
check_parser() {
    echo "🧪 Prüfe docker-compose Syntax..."
    docker compose -f "$ROOT_DIR/docker/compose-dev.yml" --env-file "$ENV_FILE" config >/dev/null

    if [ $? -eq 0 ]; then
        echo "✅ docker-compose Syntax OK"
    else
        echo "❌ Fehler in compose-dev.yml"
        return 1 2>/dev/null || exit 1
    fi
}

# ----------------------------------------
# 🧪 Funktion: Container-ENV-Check
# ----------------------------------------
check_env() {
    echo "🧪 Prüfe Container-ENV..."

    echo "   ▶ App-Container ($APP_CONTAINER):"
    docker exec "$APP_CONTAINER" printenv | grep -E "SPRING_PROFILES_ACTIVE|APP_DB_USER|APP_DB_PASSWORD" || echo "   ⚠️ Keine relevanten ENV gefunden."

    echo "   ▶ DB-Container ($DB_CONTAINER):"
    docker exec "$DB_CONTAINER" printenv | grep -E "POSTGRES_USER|POSTGRES_DB|APP_DB_USER" || echo "   ⚠️ Keine relevanten ENV gefunden."

    echo "------------------------------------------------------------"
}

# ----------------------------------------
# 🧪 Funktion: Gesamter Check
# ----------------------------------------
run_checks() {
    check_parser
    check_env
}

# ----------------------------------------
# 🔍 Optionen auswerten
# ----------------------------------------
case "$1" in
  check)
    run_checks
    ;;

  env)
    echo "🌍 Container-ENV Variablen:"
    echo ""
    echo "▶ App-Container ($APP_CONTAINER):"
    docker exec "$APP_CONTAINER" printenv | sort
    echo ""
    echo "▶ DB-Container ($DB_CONTAINER):"
    docker exec "$DB_CONTAINER" printenv | sort
    ;;

  app)
    run_checks
    echo "📄 App-Logs ($APP_CONTAINER):"
    docker logs -f "$APP_CONTAINER"
    ;;

  db)
    run_checks
    echo "🗄️ DB-Logs ($DB_CONTAINER):"
    docker logs -f "$DB_CONTAINER"
    ;;

  all)
    run_checks
    echo "📡 App + DB Logs:"
    echo "▶ App ($APP_CONTAINER)"
    docker logs -f "$APP_CONTAINER" &
    echo "▶ DB ($DB_CONTAINER)"
    docker logs -f "$DB_CONTAINER"
    ;;

  *)
    echo "❌ Unbekannte Option."
    echo ""
    echo "   Nutzung:"
    echo "     logs app     → App-Logs"
    echo "     logs db      → DB-Logs"
    echo "     logs all     → App + DB Logs"
    echo "     logs env     → ENV-Variablen der Container"
    echo "     logs check   → Parser-Check + Container-ENV-Check"
    echo ""
    ;;
esac
