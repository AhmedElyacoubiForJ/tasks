#!/usr/bin/env bash
set -e

###############################################
# UNIVERSAL SPRING BOOT RUN SCRIPT
# ---------------------------------------------
# Ziele:
#  - Ein Script für ALLE Plattformen
#  - Funktioniert unter:
#       • Linux
#       • macOS
#       • Git Bash (Windows)
#       • WSL (Ubuntu in IntelliJ)
#  - Startet Spring Boot über:
#       • JAR
#       • Maven Wrapper
#  - Lädt ENV-Dateien
#  - Übergibt ENV-Variablen korrekt an Windows-Java/Maven
#  - Löst alle WSL/Windows-Interop-Probleme
###############################################

###############################################
# 1) BASISPFAD ERMITTELN
###############################################
ROOT_DIR="$(cd "$(dirname "$0")/../.." && pwd)"
ENV_DIR="$ROOT_DIR/env"
PROFILE="local-docker-db"
ENV_FILE="$ENV_DIR/.env.$PROFILE"

###############################################
# 2) ERKENNEN, OB WIR IN WSL SIND
###############################################
IS_WSL=false
if grep -qi "microsoft" /proc/version 2>/dev/null; then
  IS_WSL=true
fi

###############################################
# 3) WINDOWS-PFAD ZU JAVA & MVNW (nur WSL)
###############################################
if [[ "$IS_WSL" == true ]]; then
  JAVA_WIN="/mnt/c/Program Files/OpenJDK/jdk-21/bin/java.exe"
  MVNW_WIN="$ROOT_DIR/mvnw.cmd"
fi

###############################################
# 4) ENV-VARIABLEN LADEN
###############################################
echo "▶ Lade ENV Variablen aus $ENV_FILE"
set -o allexport
source "$ENV_FILE"
set +o allexport
echo "✔ ENV Variablen geladen"

###############################################
# 5) JAR-PFAD
###############################################
JAR_FILE="$ROOT_DIR/target/tasks-0.0.1-SNAPSHOT.jar"

###############################################
# 6) FUNKTION: ENV → JVM -D PARAMETER
###############################################
build_jvm_env_args() {
  JVM_ENV_ARGS=()
  while IFS='=' read -r key value; do
    if grep -q "^$key=" "$ENV_FILE"; then
      JVM_ENV_ARGS+=("-D${key}=${value}")
    fi
  done < <(env)
}

###############################################
# 7) STARTMODUS ERMITTELN
###############################################
MODE="$1"

case "$MODE" in

###############################################
# 8) JAR-MODUS
###############################################
  jar)
    echo "▶ Starte über JAR..."

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

      # WSL → Windows Pfad
      JAR_FILE_WIN=$(wslpath -w "$JAR_FILE")

      # ENV → -D Parameter
      build_jvm_env_args

      # Start
      "$JAVA_WIN" \
        -Dspring.profiles.active="$PROFILE" \
        "${JVM_ENV_ARGS[@]}" \
        -jar "$JAR_FILE_WIN"

    else
      echo "🟦 Git Bash/Linux erkannt → Verwende java"
      SPRING_PROFILES_ACTIVE="$PROFILE" java -jar "$JAR_FILE"
    fi
    ;;

###############################################
# 9) MAVEN-MODUS
###############################################
mvn)
  echo "▶ Starte über Maven Wrapper..."

  if [[ "$IS_WSL" == true ]]; then
    echo "❌ mvn-Modus unter WSL nicht unterstützt."
    echo "   Grund: Windows mvnw.cmd kann ENV-Variablen aus WSL nicht zuverlässig übernehmen."
    echo "   Bitte entweder:"
    echo "     • mvn in Git Bash (Windows) ausführen, oder"
    echo "     • den JAR-Modus verwenden."
    exit 1
  fi

  echo "🟦 Git Bash/Linux erkannt → Verwende ./mvnw"
  SPRING_PROFILES_ACTIVE="$PROFILE" "$ROOT_DIR/mvnw" spring-boot:run
  ;;

#mvn)
#  echo "▶ Starte über Maven Wrapper..."
#
#  if [[ "$IS_WSL" == true ]]; then
#    echo "🌐 WSL erkannt → Verwende Windows mvnw.cmd über cmd.exe"
#
#    MVNW_WIN_PATH=$(wslpath -w "$MVNW_WIN")
#    MVNW_WIN_ESCAPED=$(echo "$MVNW_WIN_PATH" | sed 's/\\/\\\\/g')
#
#    # ENV → Windows SET-Befehle
#    ENV_SET_CMDS=""
#    while IFS='=' read -r key value; do
#      if grep -q "^$key=" "$ENV_FILE"; then
#        ENV_SET_CMDS+="set $key=$value && "
#      fi
#    done < <(env)
#
#    # Profil setzen
#    ENV_SET_CMDS+="set SPRING_PROFILES_ACTIVE=$PROFILE && "
#
#    # Finaler Windows-Befehl
#    CMD="${ENV_SET_CMDS}${MVNW_WIN_ESCAPED} spring-boot:run"
#
#    # Start über cmd.exe
#    cmd.exe /C "$CMD"
#
#  else
#    echo "🟦 Git Bash/Linux erkannt → Verwende ./mvnw"
#    SPRING_PROFILES_ACTIVE="$PROFILE" "$ROOT_DIR/mvnw" spring-boot:run
#  fi
#  ;;

#mvn)
#  echo "▶ Starte über Maven Wrapper..."
#
#  if [[ "$IS_WSL" == true ]]; then
#    echo "🌐 WSL erkannt → Verwende Windows mvnw.cmd über cmd.exe"
#
#    MVNW_WIN_PATH=$(wslpath -w "$MVNW_WIN")
#    MVNW_WIN_ESCAPED=$(echo "$MVNW_WIN_PATH" | sed 's/\\/\\\\/g')
#
#    # ENV → -D Parameter
#    build_jvm_env_args
#
#    # JVM Argumente (Profil + ENV)
#    JVM_ARGS="-Dspring.profiles.active=$PROFILE ${JVM_ENV_ARGS[*]}"
#
#    # WICHTIG: KEINE verschachtelten Quotes!
#    CMD="$MVNW_WIN_ESCAPED spring-boot:run -Dspring-boot.run.jvmArguments=$JVM_ARGS"
#
#    # Start über cmd.exe
#    cmd.exe /C "$CMD"
#
#  else
#    echo "🟦 Git Bash/Linux erkannt → Verwende ./mvnw"
#    JVM_ARGS="-Dspring.profiles.active=$PROFILE ${JVM_ENV_ARGS[*]}"
#    "$ROOT_DIR/mvnw" spring-boot:run -Dspring-boot.run.jvmArguments="$JVM_ARGS"
#  fi
#  ;;




###############################################
# 10) UNBEKANNTER MODUS
###############################################
  *)
    echo "❌ Unbekannter Modus: $MODE"
    echo "Verwendung:"
    echo "  ./app-run-universal.sh jar"
    echo "  ./app-run-universal.sh mvn"
    exit 1
    ;;
esac