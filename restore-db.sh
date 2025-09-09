#!/bin/bash

# Datei: restore-db.sh
# Zweck: Wiederherstellung eines pgdata-Backups in das Volume

BACKUP_FILE=$1

if [ -z "$BACKUP_FILE" ]; then
  echo "❌ Bitte gib den Pfad zur Backup-Datei an."
  echo "➡️ Beispiel: ./restore-db.sh ./backup/pgdata-backup-2025-09-08_02-21.tar.gz"
  exit 1
fi

echo "♻️ Stelle Backup wieder her in Volume 'pgdata'..."

docker run --rm \
  -v pgdata:/data \
  -v "$PWD":/backup \
  alpine \
  sh -c "rm -rf /data/* && tar xzf /backup/$(basename $BACKUP_FILE) -C /"

echo "✅ Wiederherstellung abgeschlossen."
