#!/usr/bin/env bash
set -e

###############################################################
# UNIVERSAL SPRING BOOT RUN SCRIPT
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
#  - Lädt ENV-Dateien
#  - Übergibt ENV-Variablen korrekt an Java
#  - Vermeidet alle WSL/Windows-Interop-Probleme
#
# WICHTIG:
#  Der mvn‑Modus ist unter WSL deaktiviert.
#  Grund: mvnw.cmd läuft unter Windows/cmd.exe und kann ENV‑Variablen
#  aus WSL NICHT zuverlässig übernehmen (Whitespace, CRLF, zerstörte URLs).
#  Der JAR‑Modus ist dagegen 100% stabil und plattformübergreifend.
###############################################################

###############################################################
# 1) BASISPFAD ERMITTELN
###############################################################
ROOT_DIR="$(cd "$(dirname "$0")/../.." && pwd)"
ENV_DIR="$ROOT_DIR/env"
PROFILE="local-docker-db"
ENV_FILE="$ENV_DIR/.env.$PROFILE"

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
# 4) ENV-VARIABLEN LADEN
###############################################################
echo "▶ Lade ENV Variablen aus $ENV_FILE"
set -o allexport
source "$ENV_FILE"
set +o allexport
echo "✔ ENV Variablen geladen"

###############################################################
# 5) JAR-PFAD
###############################################################
JAR_FILE="$ROOT_DIR/target/tasks-0.0.1-SNAPSHOT.jar"

###############################################################
# 6) FUNKTION: ENV → JVM -D PARAMETER
#    (Nur für JAR-Modus nötig)
###############################################################
build_jvm_env_args() {
  JVM_ENV_ARGS=()
  while IFS='=' read -r key value; do
    if grep -q "^$key=" "$ENV_FILE"; then
      JVM_ENV_ARGS+=("-D${key}=${value}")
    fi
  done < <(env)
}

###############################################################
# 7) STARTMODUS ERMITTELN
###############################################################
MODE="$1"

case "$MODE" in

###############################################################
# 8) JAR-MODUS (universell & stabil)
###############################################################
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

      # Start über Windows-Java.exe
      "$JAVA_WIN" \
        -Dspring.profiles.active="$PROFILE" \
        "${JVM_ENV_ARGS[@]}" \
        -jar "$JAR_FILE_WIN"

    else
      echo "🟦 Git Bash/Linux erkannt → Verwende java"
      SPRING_PROFILES_ACTIVE="$PROFILE" java -jar "$JAR_FILE"
    fi
    ;;

###############################################################
# 9) MAVEN-MODUS
# -------------------------------------------------------------
# WICHTIG:
#  mvn unter WSL ist deaktiviert.
#
#  Grund:
#   - mvnw.cmd läuft unter Windows/cmd.exe
#   - ENV aus WSL werden NICHT zuverlässig übernommen
#   - cmd.exe fügt Whitespaces ein → JDBC-URLs werden zerstört
#   - DevTools macht Restart → ENV gehen erneut verloren
#   - Ergebnis: kaputte DB-URLs, falsche Ports, falsche Credentials
#
#  Lösung:
#   - mvn NUR unter Git Bash / Linux erlauben
#   - unter WSL → JAR-Modus verwenden
###############################################################
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

###############################################################
# 10) UNBEKANNTER MODUS
###############################################################
  *)
    echo "❌ Unbekannter Modus: $MODE"
    echo "Verwendung:"
    echo "  ./app-run-universal.sh jar"
    echo "  ./app-run-universal.sh mvn"
    exit 1
    ;;
esac
