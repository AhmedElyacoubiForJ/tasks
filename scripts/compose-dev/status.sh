#!/bin/bash
# =====================================================================
# 📊 dev status — Übersicht über laufende Container (stabil & farbig)
# =====================================================================

GREEN="\e[32m"
RED="\e[31m"
YELLOW="\e[33m"
BLUE="\e[34m"
RESET="\e[0m"

echo -e "${BLUE}📊 Container Status:${RESET}"

# Container filtern (tasks-app, postgres-dev)
containers=$(docker ps --format "{{.Names}}|{{.Status}}|{{.Ports}}" | grep -E "tasks|dev")

if [[ -z "$containers" ]]; then
    echo -e "   ${YELLOW}Keine laufenden compose-dev Container gefunden.${RESET}"
else
    echo "$containers" | while IFS="|" read -r name status ports; do
        if [[ "$status" == *"Up"* ]]; then
            color="$GREEN"
        elif [[ "$status" == *"Exited"* ]]; then
            color="$RED"
        else
            color="$YELLOW"
        fi

        printf "   ${color}%-15s | %-25s | %s${RESET}\n" "$name" "$status" "$ports"
    done
fi

echo ""
echo -e "${BLUE}🩺 Healthchecks:${RESET}"

# IDs aller relevanten Container holen
container_ids=$(docker ps -q | xargs docker inspect -f '{{.Name}} {{.Id}}' 2>/dev/null | grep -E "tasks|dev" | awk '{print $2}')

if [[ -z "$container_ids" ]]; then
    echo -e "   ${YELLOW}Keine Container mit Healthchecks gefunden.${RESET}"
    exit 0
fi

for cid in $container_ids; do
    name=$(docker inspect -f '{{.Name}}' "$cid" | sed 's/\///')
    health=$(docker inspect -f '{{.State.Health.Status}}' "$cid" 2>/dev/null)

    if [[ -z "$health" ]]; then
        echo -e "   ${YELLOW}$name | no healthcheck${RESET}"
    elif [[ "$health" == "healthy" ]]; then
        echo -e "   ${GREEN}$name | $health${RESET}"
    elif [[ "$health" == "unhealthy"* ]]; then
        echo -e "   ${RED}$name | $health${RESET}"
    else
        echo -e "   ${YELLOW}$name | $health${RESET}"
    fi
done
