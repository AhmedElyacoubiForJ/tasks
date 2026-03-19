#!/bin/bash
# =====================================================================
# 🩺 dev doctor — Diagnose der compose-dev Umgebung (erweitert)
# =====================================================================

GREEN="\e[32m"
RED="\e[31m"
YELLOW="\e[33m"
BLUE="\e[34m"
RESET="\e[0m"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
ENV_FILE="$ROOT_DIR/.env.compose-dev"

echo -e "${BLUE}🔍 Starte Diagnose...${RESET}"

# ----------------------------------------
# Docker Daemon
# ----------------------------------------
if ! docker info >/dev/null 2>&1; then
    echo -e "${RED}❌ Docker Daemon läuft nicht!${RESET}"
    exit 1
else
    echo -e "${GREEN}✔ Docker Daemon läuft${RESET}"
fi

# ----------------------------------------
# Docker Compose
# ----------------------------------------
if ! docker compose version >/dev/null 2>&1; then
    echo -e "${RED}❌ Docker Compose nicht installiert!${RESET}"
    exit 1
else
    echo -e "${GREEN}✔ Docker Compose verfügbar${RESET}"
fi

# ----------------------------------------
# ENV Check
# ----------------------------------------
if [ ! -f "$ENV_FILE" ]; then
    echo -e "${RED}❌ ENV fehlt: $ENV_FILE${RESET}"
    exit 1
else
    echo -e "${GREEN}✔ ENV gefunden: $ENV_FILE${RESET}"
fi

set -a
source "$ENV_FILE"
set +a

# ----------------------------------------
# ENV Validierung
# ----------------------------------------
REQUIRED_VARS=("POSTGRES_USER" "POSTGRES_PASSWORD" "POSTGRES_DB" "VOLUME")

echo -e "${BLUE}🔎 Prüfe ENV Variablen...${RESET}"
for var in "${REQUIRED_VARS[@]}"; do
  if [ -z "${!var}" ]; then
    echo -e "   ${RED}❌ $var fehlt${RESET}"
  else
    echo -e "   ${GREEN}✔ $var=${!var}${RESET}"
  fi
done

# ----------------------------------------
# Compose Syntax
# ----------------------------------------
echo -e "${BLUE}🔎 Prüfe docker-compose Syntax...${RESET}"
if docker compose -f "$ROOT_DIR/docker-compose-dev.yml" --env-file "$ENV_FILE" config >/dev/null; then
    echo -e "${GREEN}✔ Compose Syntax OK${RESET}"
else
    echo -e "${RED}❌ Fehler in docker-compose-dev.yml${RESET}"
    exit 1
fi

# ----------------------------------------
# Container Existenz
# ----------------------------------------
echo -e "${BLUE}📦 Container Check:${RESET}"
CONTAINERS=("tasks-app" "postgres-dev")

for c in "${CONTAINERS[@]}"; do
  if docker ps -a --format '{{.Names}}' | grep -q "^$c$"; then
    echo -e "   ${GREEN}✔ $c existiert${RESET}"
  else
    echo -e "   ${RED}❌ $c fehlt${RESET}"
  fi
done

# ----------------------------------------
# Healthchecks
# ----------------------------------------
echo -e "${BLUE}🩺 Healthchecks:${RESET}"
docker inspect --format '{{.Name}} | {{.State.Health.Status}}' $(docker ps -q --filter "name=tasks") \
  2>/dev/null | sed 's/\///' | while IFS= read -r line; do
    if [[ "$line" == *"healthy"* ]]; then
      echo -e "   ${GREEN}${line}${RESET}"
    elif [[ "$line" == *"unhealthy"* ]]; then
      echo -e "   ${RED}${line}${RESET}"
    else
      echo -e "   ${YELLOW}${line}${RESET}"
    fi
  done

# ----------------------------------------
# Volume Check
# ----------------------------------------
PROJECT_NAME=$(basename "$ROOT_DIR")
DOCKER_VOLUME="${PROJECT_NAME}_${VOLUME}"

echo -e "${BLUE}🗄️ Volume Check:${RESET}"
if docker volume inspect "$DOCKER_VOLUME" >/dev/null 2>&1; then
    echo -e "   ${GREEN}✔ Volume vorhanden: $DOCKER_VOLUME${RESET}"
else
    echo -e "   ${RED}❌ Volume fehlt: $DOCKER_VOLUME${RESET}"
fi

# ----------------------------------------
# Port Check
# ----------------------------------------
echo -e "${BLUE}🌐 Port Check:${RESET}"
PORTS=("8080" "5432")

for p in "${PORTS[@]}"; do
    if ss -tulpn | grep -q ":$p "; then
        echo -e "   ${YELLOW}⚠️ Port $p ist belegt${RESET}"
    else
        echo -e "   ${GREEN}✔ Port $p ist frei${RESET}"
    fi
done

echo -e "${GREEN}🏁 Diagnose abgeschlossen.${RESET}"
