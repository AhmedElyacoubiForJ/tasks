#!/bin/bash

# Datei: backup-db.sh
# Zweck: Backup des Docker-Volumes pgdata in ein tar.gz-Archiv

BACKUP_DIR="./backup"
TIMESTAMP=$(date +"%Y-%m-%d_%H-%M")
BACKUP_FILE="${BACKUP_DIR}/pgdata-backup-${TIMESTAMP}.tar.gz"

mkdir -p "$BACKUP_DIR"

echo "📦 Erstelle Backup von Volume 'pgdata'..."

docker run --rm \
  -v pgdata:/data \
  -v "$PWD/$BACKUP_DIR":/backup \
  alpine \
  tar czf "/backup/pgdata-backup-${TIMESTAMP}.tar.gz" /data

echo "✅ Backup gespeichert unter: $BACKUP_FILE"
