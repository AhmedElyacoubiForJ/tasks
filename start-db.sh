#!/bin/bash

# Datei: start-db.sh
# Zweck: PostgreSQL-Container mit Umgebungsvariablen aus .env starten

echo "📦 Starte PostgreSQL-Datenbank..."

# Prüfe, ob .env existiert
if [ ! -f .env ]; then
  echo "❌ Fehler: .env-Datei nicht gefunden."
  exit 1
fi

# Container starten
docker run -d \
  --name tasks-db \
  --env-file .env \
  -v pgdata:/var/lib/postgresql/data \
  -p ${DB_PORT:-5432}:5432 \
  postgres:alpine

echo "✅ PostgreSQL läuft auf Port ${DB_PORT:-5432}"
