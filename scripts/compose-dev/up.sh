#!/bin/bash
echo "🚀 Starte compose-dev Umgebung..."
docker compose -f docker-compose-dev.yml --env-file .env.compose-dev up -d