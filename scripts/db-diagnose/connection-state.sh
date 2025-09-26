#!/bin/bash
# 🧾 connection-state.sh → Aktive Verbindungen
echo "🔌 Aktive Verbindungen:"
docker exec -it postgres-dev psql -U admin -d tasks_dev_db -c \
"SELECT pid, usename, datname, client_addr, state, query FROM pg_stat_activity WHERE state != 'idle';"
