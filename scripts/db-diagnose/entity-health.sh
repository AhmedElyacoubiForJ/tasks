#!/bin/bash
# 🧾 entity-health.sh → Rollen & Datenbanken
echo "🧠 Rollen & Datenbanken:"
docker exec -it postgres-dev psql -U admin -d tasks_dev_db -c \
"SELECT rolname, rolsuper, rolcreaterole, rolcreatedb FROM pg_roles;"
docker exec -it postgres-dev psql -U admin -d tasks_dev_db -c \
"SELECT datname FROM pg_database WHERE datistemplate = false;"
