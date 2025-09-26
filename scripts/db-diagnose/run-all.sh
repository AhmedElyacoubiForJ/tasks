#!/bin/bash
# 🧾 run-all.sh → Alles auf einmal
echo "🚀 Starte vollständige DB-Diagnose:"
scripts/db-diagnose/table-activity.sh
scripts/db-diagnose/index-usage.sh
scripts/db-diagnose/connection-state.sh
scripts/db-diagnose/entity-health.sh
scripts/db-diagnose/session-metrics.sh
