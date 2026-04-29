#!/usr/bin/env bash
set -e

###############################################################
# UNIVERSAL SPRING BOOT RUN SCRIPT (local-demo)
# -------------------------------------------------------------
# Ziele:
#  - Ein einziges Script für ALLE Plattformen:
#       • Linux
#       • macOS
#       • Git Bash (Windows)
#       • WSL (Ubuntu in IntelliJ)
#  - Startet Spring Boot über:
#       • JAR (universell & stabil)
#       • Maven Wrapper (nur außerhalb von WSL)
#
# Besonderheiten für local-demo:
#  - KEINE externe DB → H2 embedded
#  - KEINE .env-Datei nötig
#  - KEINE Docker-Kommandos
###############################################################

###############################################################
# 1) BASISPFAD ERMITTELN
###############################################################
ROOT_DIR="$(cd "$(dirname "$0")/../.." && pwd)"
PROFILE="local-demo"

###############################################################
# 2) ERKENNEN, OB WIR IN WSL SIND
###############################################################
IS_WSL=false
if grep -qi "microsoft" /proc/version 2>/dev/null; then
  IS_WSL=true
fi

###############################################################
# 3) WINDOWS-PFAD ZU JAVA & MVNW (nur WSL)
###############################################################
if [[ "$IS_WSL" == true ]]; then
  JAVA_WIN="/mnt/c/Program Files/OpenJDK/jdk-21/bin/java.exe"
  MVNW_WIN="$ROOT_DIR/mvnw.cmd"
fi

###############################################################
# 4) JAR-PFAD
###############################################################
JAR_FILE="$ROOT_DIR/target/tasks-0.0.1-SNAPSHOT.jar"

###############################################################
# 5) STARTMODUS ERMITTELN
###############################################################
MODE="$1"

case "$MODE" in

###############################################################
# 6) JAR-MODUS (universell & stabil)
###############################################################
  jar)
    echo "▶ Starte local-demo über JAR..."

    # Falls JAR fehlt → bauen
    if [[ ! -f "$JAR_FILE" ]]; then
      echo "⚠️  JAR nicht gefunden → baue Projekt..."
      if [[ "$IS_WSL" == true ]]; then
        cmd.exe /C "$(wslpath -w "$ROOT_DIR/mvnw.cmd") clean package -DskipTests"
      else
        "$ROOT_DIR/mvnw" clean package -DskipTests
      fi
    fi

    if [[ "$IS_WSL" == true ]]; then
      echo "🌐 WSL erkannt → Verwende Windows java.exe"

      JAR_FILE_WIN=$(wslpath -w "$JAR_FILE")

      "$JAVA_WIN" \
        -Dspring.profiles.active="$PROFILE" \
        -jar "$JAR_FILE_WIN"

    else
      echo "🟦 Git Bash/Linux/macOS erkannt → Verwende java"
      SPRING_PROFILES_ACTIVE="$PROFILE" java -jar "$JAR_FILE"
    fi
    ;;

###############################################################
# 7) MAVEN-MODUS
###############################################################
  mvn)
    echo "▶ Starte local-demo über Maven Wrapper..."

    if [[ "$IS_WSL" == true ]]; then
      echo "❌ mvn-Modus unter WSL nicht unterstützt."
      echo "   Bitte JAR-Modus verwenden."
      exit 1
    fi

    echo "🟦 Git Bash/Linux/macOS erkannt → Verwende ./mvnw"
    #SPRING_PROFILES_ACTIVE="$PROFILE" "$ROOT_DIR/mvnw" spring-boot:run
    SPRING_PROFILES_ACTIVE="$PROFILE" "$ROOT_DIR/mvnw" -f "$ROOT_DIR/pom.xml" spring-boot:run
    ;;

###############################################################
# 8) UNBEKANNTER MODUS
###############################################################
  *)
    echo "❌ Unbekannter Modus: $MODE"
    echo "Verwendung:"
    echo "  ./app-run-universal.sh jar"
    echo "  ./app-run-universal.sh mvn"
    exit 1
    ;;
esac
