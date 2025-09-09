#!/bin/bash

# Datei: start-app.sh
# Zweck: Spring Boot App-Container mit Umgebungsvariablen aus .env starten

echo "🚀 Starte Spring Boot App..."

# Prüfe, ob .env existiert
if [ ! -f .env ]; then
  echo "❌ Fehler: .env-Datei nicht gefunden."
  exit 1
fi

# Container starten
docker run -d \
  --name tasks-app \
  --env-file .env \
  -p ${APP_PORT:-8080}:8080 \
  myimage:tasks-app

echo "✅ App läuft auf Port ${APP_PORT:-8080}"
