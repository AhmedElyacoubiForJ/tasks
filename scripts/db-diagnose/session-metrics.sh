#!/bin/bash
# 🧠 Was das Skript tut
# Holt die Logs aus dem Container tasks-app
#
# Sucht nach dem Marker Session Metrics {
#
# Zeigt die nächsten 12 Zeilen (die Metriken)
#
# Schneidet sauber auf 13 Zeilen (inkl. Header)
#
# Man bekommt nur die relevanten Metriken, ohne Rauschen
#!/bin/bash
set -e

# Zielverzeichnis für Logs
LOG_DIR="logs"
LOG_FILE="$LOG_DIR/session-metrics.log"

# Container-Name
CONTAINER_NAME="tasks-app"

# Stelle sicher, dass logs/ existiert
mkdir -p "$LOG_DIR"

echo "📈 Extrahiere Hibernate Session Metrics aus $CONTAINER_NAME..."
echo "📝 Speichere unter $LOG_FILE"

# Extrahiere und speichere die letzten Session Metrics
docker logs "$CONTAINER_NAME" 2>&1 | grep -A 12 "Session Metrics {" | tail -n 13 > "$LOG_FILE"

echo "✅ Fertig. Inhalt:"
echo "--------------------------------------------------"
cat "$LOG_FILE"
echo "--------------------------------------------------"
