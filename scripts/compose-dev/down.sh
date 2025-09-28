#!/bin/bash
echo "🛑 Stoppe compose-dev Umgebung..."
docker compose -f docker-compose-dev.yml --env-file .env.compose-dev down