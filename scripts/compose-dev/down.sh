#!/bin/bash
set -e
echo "🚀 Stop Compose-Dev-Umgebung..."
docker compose -f docker-compose-dev.yml --env-file ./db-config/compose/dev/.env.dev down -v

#dev-down.sh: docker-compose -f docker-compose-dev.yml down -v
 #
 #dev-logs.sh: docker-compose -f docker-compose-dev.yml logs -f
 #
 #dev-health.sh: prüft docker inspect auf Health-Status
 #
 #dev-restart.sh: docker-compose -f docker-compose-dev.yml restart app