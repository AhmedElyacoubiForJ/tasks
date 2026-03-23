#!/bin/bash
# ----------------------------------------
# 🚀 Startet die App mit PostgreSQL-Konfiguration (local-prod Profil)
# ----------------------------------------

# Pfad zur ENV-Datei mit PostgreSQL-Zugangsdaten und Spring-Konfiguration
#ENV_FILE="db-config/local/prod/.env"
ENV_FILE="env/.env.local-prod"

# Prüfe, ob die ENV-Datei existiert
if [[ ! -f "$ENV_FILE" ]]; then
  echo "❌ ENV-Datei nicht gefunden: $ENV_FILE"
  exit 1
fi

# Lade alle Variablen aus der Datei und exportiere sie automatisch
# Vorteil: robust gegenüber Sonderzeichen, Leerzeichen, Kommentaren
set -a
source "$ENV_FILE"
set +a

# Starte die Spring Boot App mit dem Profil 'local-prod'
# Dieses Profil erwartet PostgreSQL und nutzt die geladenen ENV-Werte
SPRING_PROFILES_ACTIVE=local-prod ./mvnw spring-boot:run
