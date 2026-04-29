#!/usr/bin/env bash
set -e

###############################################################
# UNIVERSAL SPRING BOOT RUN SCRIPT (local-postgres)
###############################################################

ROOT_DIR="$(cd "$(dirname "$0")/../.." && pwd)"
PROFILE="local-postgres"

ENV_FILE="$ROOT_DIR/env/.env.local-postgres"

###############################################################
# ENV laden + dynamische Key=Value → JAVA_PROPS erzeugen
###############################################################
JAVA_PROPS=""

if [[ -f "$ENV_FILE" ]]; then
  echo "🧩 Lade Environment: $ENV_FILE"

  while IFS='=' read -r key value; do
    # Kommentare und leere Zeilen überspringen
    [[ "$key" =~ ^#.*$ || -z "$key" ]] && continue

    # Trim
    key=$(echo "$key" | xargs)
    value=$(echo "$value" | xargs)

    # Shell-ENV setzen
    export "$key"="$value"

    # Java -D Parameter erzeugen
    JAVA_PROPS="$JAVA_PROPS -D${key}=${value}"
  done < "$ENV_FILE"

else
  echo "⚠️  Environment-Datei nicht gefunden: $ENV_FILE"
fi

###############################################################
# WSL-Erkennung
###############################################################
IS_WSL=false
if grep -qi "microsoft" /proc/version 2>/dev/null; then
  IS_WSL=true
fi

###############################################################
# Windows Java + Maven Wrapper für WSL
###############################################################
if [[ "$IS_WSL" == true ]]; then
  JAVA_WIN="/mnt/c/Program Files/OpenJDK/jdk-21/bin/java.exe"
  MVNW_WIN="$ROOT_DIR/mvnw.cmd"
fi

###############################################################
# JAR-Pfad
###############################################################
JAR_FILE="$ROOT_DIR/target/tasks-0.0.1-SNAPSHOT.jar"

###############################################################
# Startmodus
###############################################################
MODE="$1"

case "$MODE" in

  #############################################################
  # JAR-MODUS (universell)
  #############################################################
  jar)
    echo "▶ Starte local-postgres über JAR..."

    # Falls JAR fehlt → bauen
    if [[ ! -f "$JAR_FILE" ]]; then
      echo "⚠️  JAR nicht gefunden → baue Projekt..."
      if [[ "$IS_WSL" == true ]]; then
        cmd.exe /C "$(wslpath -w "$ROOT_DIR/mvnw.cmd") clean package -DskipTests"
      else
        "$ROOT_DIR/mvnw" clean package -DskipTests
      fi
    fi

    ###########################################################
    # WSL → Windows java.exe (mit dynamischen JAVA_PROPS)
    ###########################################################
    if [[ "$IS_WSL" == true ]]; then
      echo "🌐 WSL erkannt → Verwende Windows java.exe"

      JAR_FILE_WIN=$(wslpath -w "$JAR_FILE")

      # WICHTIG: JAVA_PROPS wird direkt expandiert
      "$JAVA_WIN" \
        -Dspring.profiles.active="$PROFILE" \
        $JAVA_PROPS \
        -jar "$JAR_FILE_WIN"
      exit $?
    fi

    ###########################################################
    # Git Bash / Linux / macOS → normale ENV-Vererbung
    ###########################################################
    echo "🟦 Git Bash/Linux/macOS erkannt → Verwende java"
    SPRING_PROFILES_ACTIVE="$PROFILE" java $JAVA_PROPS -jar "$JAR_FILE"
    ;;

  #############################################################
  # MAVEN-MODUS
  #############################################################
  mvn)
    echo "▶ Starte local-postgres über Maven Wrapper..."

    if [[ "$IS_WSL" == true ]]; then
      echo "❌ mvn-Modus unter WSL nicht unterstützt."
      echo "   Bitte JAR-Modus verwenden."
      exit 1
    fi

    SPRING_PROFILES_ACTIVE="$PROFILE" \
      "$ROOT_DIR/mvnw" -f "$ROOT_DIR/pom.xml" spring-boot:run
    ;;

  #############################################################
  # UNBEKANNTER MODUS
  #############################################################
  *)
    echo "❌ Unbekannter Modus: $MODE"
    echo "Verwendung:"
    echo "  ./app-run-universal.sh jar"
    echo "  ./app-run-universal.sh mvn"
    exit 1
    ;;
esac
