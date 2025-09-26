#!/bin/bash
# 🧾 index-usage.sh → Index-Effizienz
echo "🔍 Index-Nutzung:"
docker exec -it postgres-dev psql -U admin -d tasks_dev_db -c \
"SELECT relname, idx_scan, seq_scan, CASE WHEN seq_scan > 0 THEN ROUND(idx_scan::numeric / seq_scan, 2) ELSE NULL END AS idx_effizienz FROM pg_stat_user_tables ORDER BY idx_effizienz DESC NULLS LAST;"
