#!/bin/bash
# scripts/compose-dev/up.sh
echo "🚀 Starte compose-dev Umgebung..."
docker compose -f docker-compose-dev.yml --env-file .env.compose-dev up -d