#!/usr/bin/env bash
set -euo pipefail

# scripts/local-docker-db/app-run.sh
###############################################
# App Runner Script
# Startet die Spring Boot App mit dem Profil
# "local-docker-db" – wahlweise über Maven
# oder über das fertige JAR.
###############################################

# Verzeichnis des Skripts (robust, egal von wo gestartet)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

PROFILE="local-docker-db"
ENV_FILE="$ROOT_DIR/env/.env.${PROFILE}"
JAR_FILE="$ROOT_DIR/target/tasks-0.0.1-SNAPSHOT.jar"
MVNW="$ROOT_DIR/mvnw"

###############################################
# 0) Hilfe anzeigen
###############################################
usage() {
  echo "Usage: $0 [mvn|jar]"
  echo
  echo "  mvn   Startet die App über Maven Wrapper"
  echo "  jar   Startet die App über das gebaute JAR"
  exit 1
}

if [[ $# -lt 1 ]]; then
  usage
fi

MODE="$1"

echo "=============================================="
echo "🚀 Starte Tasks App (Profil: $PROFILE)"
echo "=============================================="
echo

###############################################
# 1) ENV Variablen laden
###############################################

if [[ ! -f "$ENV_FILE" ]]; then
  echo "❌ ENV-Datei nicht gefunden: $ENV_FILE"
  exit 1
fi

echo "▶ Lade ENV Variablen aus $ENV_FILE"
set -a
source "$ENV_FILE"
set +a
echo "✔ ENV Variablen geladen"
echo

###############################################
# 2) Startmodus auswählen
###############################################

case "$MODE" in

  mvn)
    echo "▶ Starte über Maven Wrapper..."
    SPRING_PROFILES_ACTIVE="$PROFILE" "$MVNW" spring-boot:run
    ;;

  jar)
    echo "▶ JAR-Modus gewählt"

    # Prüfen, ob das JAR existiert
    if [[ ! -f "$JAR_FILE" ]]; then
      echo "⚠️  JAR nicht gefunden: $JAR_FILE"
      echo "▶ Baue Projekt (clean & package)..."

      SPRING_PROFILES_ACTIVE="$PROFILE" "$MVNW" clean package -DskipTests

      echo "✔ Build abgeschlossen"
      echo
    fi

    echo "▶ Starte über JAR..."
    SPRING_PROFILES_ACTIVE="$PROFILE" \
      java -jar "$JAR_FILE"
    ;;

  *)
    usage
    ;;
esac
