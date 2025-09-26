#!/bin/bash
set -e
echo "🚀 Starte Compose-Dev-Umgebung..."
docker compose -f docker-compose-dev.yml --env-file ./db-config/compose/dev/.env.dev up --build
