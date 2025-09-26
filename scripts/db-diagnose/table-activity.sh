#!/bin/bash
# 🧾 table-activity.sh → Tabellenaktivität
echo "📊 Tabellenaktivität (Top Inserts):"
docker exec -it postgres-dev psql -U admin -d tasks_dev_db -c \
"SELECT relname, seq_scan, idx_scan, n_tup_ins, n_tup_upd, n_tup_del FROM pg_stat_user_tables ORDER BY n_tup_ins DESC LIMIT 10;"
