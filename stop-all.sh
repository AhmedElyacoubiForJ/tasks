#!/bin/bash

# Datei: stop-all.sh
# Zweck: Stoppt und entfernt die Container für App und Datenbank

echo "🛑 Stoppe Spring Boot App-Container..."
docker stop tasks-app 2>/dev/null && docker rm tasks-app 2>/dev/null

echo "🛑 Stoppe PostgreSQL DB-Container..."
docker stop tasks-db 2>/dev/null && docker rm tasks-db 2>/dev/null

echo "✅ Alle Container wurden gestoppt und entfernt."
