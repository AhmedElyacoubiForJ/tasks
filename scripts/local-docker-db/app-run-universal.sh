#!/usr/bin/env bash
set -e

ROOT_DIR="$(cd "$(dirname "$0")/../.." && pwd)"
PROFILE="local-docker-db"

###############################################################
# WSL-Erkennung
###############################################################
IS_WSL=false
if grep -qi "microsoft" /proc/version 2>/dev/null; then
  IS_WSL=true
fi

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

  jar)
    echo "▶ Starte local-demo über JAR..."

    if [[ ! -f "$JAR_FILE" ]]; then
      echo "⚠️  JAR nicht gefunden → baue Projekt..."
      if [[ "$IS_WSL" == true ]]; then
        cmd.exe /C "$(wslpath -w "$ROOT_DIR/mvnw.cmd") clean package -DskipTests"
      else
        "$ROOT_DIR/mvnw" clean package -DskipTests
      fi
    fi

    if [[ "$IS_WSL" == true ]]; then
      JAR_FILE_WIN=$(wslpath -w "$JAR_FILE")
      "$JAVA_WIN" -Dspring.profiles.active="$PROFILE" -jar "$JAR_FILE_WIN"
    else
      SPRING_PROFILES_ACTIVE="$PROFILE" java -jar "$JAR_FILE"
    fi
    ;;

  mvn)
    echo "▶ Starte local-demo über Maven Wrapper..."

    if [[ "$IS_WSL" == true ]]; then
      echo "❌ mvn-Modus unter WSL nicht unterstützt."
      exit 1
    fi

    echo "🟦 Git Bash/Linux/macOS erkannt → Verwende ./mvnw"
    SPRING_PROFILES_ACTIVE="$PROFILE" "$ROOT_DIR/mvnw" -f "$ROOT_DIR/pom.xml" spring-boot:run
    ;;

  *)
    echo "❌ Unbekannter Modus: $MODE"
    echo "Verwendung:"
    echo "  ./app-run-universal.sh jar"
    echo "  ./app-run-universal.sh mvn"
    exit 1
    ;;
esac


# TODO
# ./mvnw clean install -DskipTests

# Pro-Tipp:
#Wenn du auch das Kompilieren der Tests überspringen willst (um noch mehr Zeit zu sparen), kannst du diesen Befehl nutzen:
#./mvnw clean install -Dmaven.test.skip=true