#!/bin/bash
# =====================================================================
# ⚠️ Dieses Skript MUSS gesourct werden!
# =====================================================================
#
# Nutzung:
#   db shell          → psql öffnen
#   db tables         → Tabellen anzeigen
#   db users          → User anzeigen
#   db schemas        → Schemas anzeigen
#   db exec "<SQL>"   → SQL direkt ausführen
#   db reset          → Datenbank leeren (DROP + CREATE)
#   db info           → DB-ENV anzeigen
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
# 🔍 Hilfsfunktion: psql im Container ausführen
# ----------------------------------------
psql_exec() {
    docker exec -it "$DB_CONTAINER" psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "$1"
}

# ----------------------------------------
# 🔍 Hilfsfunktion: psql interaktiv
# ----------------------------------------
psql_shell() {
    docker exec -it "$DB_CONTAINER" psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"
}

# ----------------------------------------
# 🔍 Optionen auswerten
# ----------------------------------------
case "$1" in

  shell)
    echo "🐚 Öffne psql-Shell..."
    psql_shell
    ;;

  tables)
    echo "📋 Tabellen in $POSTGRES_DB:"
    psql_exec "\dt"
    ;;

  users)
    echo "👥 Datenbank-User:"
    psql_exec "SELECT usename FROM pg_user;"
    ;;

  schemas)
    echo "📂 Schemas:"
    psql_exec "SELECT schema_name FROM information_schema.schemata;"
    ;;

  exec)
    shift
    echo "▶ Führe SQL aus:"
    echo "$*"
    psql_exec "$*"
    ;;

  reset)
    echo "⚠️ Leere Datenbank $POSTGRES_DB..."
    psql_exec "DROP SCHEMA public CASCADE;"
    psql_exec "CREATE SCHEMA public;"
    echo "✔ Datenbank zurückgesetzt."
    ;;

  info)
    echo "ℹ️ DB-Informationen:"
    echo "   Host:       $DB_CONTAINER"
    echo "   User:       $POSTGRES_USER"
    echo "   Database:   $POSTGRES_DB"
    echo "   Volume:     $VOLUME"
    echo "   Docker Vol: $(basename "$ROOT_DIR")_${VOLUME}"
    ;;

  *)
    echo "❌ Unbekannte Option."
    echo ""
    echo "   Nutzung:"
    echo "     db shell          → psql öffnen"
    echo "     db tables         → Tabellen anzeigen"
    echo "     db users          → User anzeigen"
    echo "     db schemas        → Schemas anzeigen"
    echo "     db exec \"SQL\"     → SQL direkt ausführen"
    echo "     db reset          → Datenbank leeren"
    echo "     db info           → DB-ENV anzeigen"
    echo ""
    ;;
esac
