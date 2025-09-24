#!/bin/bash

echo "🚀 Starte containerisierte Dev-Umgebung..."

ENV_FILE="./db-config/container/dev/.env.dev"
INIT_SCRIPT="./db-config/container/dev/init-dev-db.sh"
VOLUME="pgdata-dev"
DB_CONTAINER="postgres-dev"
APP_CONTAINER="tasks-app"
LOG_FILE="./logs/dev-start.log"

# Container entfernen, falls vorhanden
docker rm -f $DB_CONTAINER $APP_CONTAINER 2>/dev/null
docker volume rm $VOLUME 2>/dev/null

# DB-Container starten
docker run --name $DB_CONTAINER \
  -p 5432:5432 \
  -e SPRING_PROFILES_ACTIVE="$SPRING_PROFILES_ACTIVE" \
  --env-file $ENV_FILE \
  -v $(pwd)/$INIT_SCRIPT:/docker-entrypoint-initdb.d/init-dev-db.sh \
  -v $VOLUME:/var/lib/postgresql/data \
  -d postgres:alpine

echo "⏳ Warte bis PostgreSQL bereit ist..."
until docker exec $DB_CONTAINER pg_isready -U dev_user -d tasks_dev_db; do
  echo "🕒 PostgreSQL noch nicht bereit..."
  sleep 2
done

echo "✅ PostgreSQL ist bereit — starte App-Container..."
docker run -d --name $APP_CONTAINER \
  --env-file $ENV_FILE \
  -p 8080:8080 \
  myimage:tasks-app

# Logging
mkdir -p ./logs
echo "[$(date '+%Y-%m-%d %H:%M:%S')] Dev-Container gestartet" >> $LOG_FILE
docker ps --filter "name=$DB_CONTAINER" --filter "name=$APP_CONTAINER" >> $LOG_FILE
echo "📄 Log gespeichert unter $LOG_FILE"

echo ""
echo "🩺 Prüfe App-Health über Actuator..."

echo "⏳ Warte auf App-Health..."

for i in {1..10}; do
  HEALTH=$(curl -s http://localhost:8080/actuator/health | grep '"status":"UP"')
  if [[ -n "$HEALTH" ]]; then
    echo "✅ App ist gesund"
    break
  else
    echo "🕒 Versuch $i: App noch nicht bereit..."
    sleep 3
  fi
done

if [[ -z "$HEALTH" ]]; then
  echo "⚠️ App-Health konnte nicht bestätigt werden"
fi


echo ""
read -p "❓ Möchten Sie direkt in die Datenbank springen? [y/N]: " open_db
if [[ "$open_db" == "y" || "$open_db" == "Y" ]]; then
  echo "🧠 Öffne PostgreSQL-Konsole..."
  docker exec -it postgres-dev psql -U dev_user -d tasks_dev_db
else
  echo "🏁 Setup abgeschlossen. Viel Erfolg beim Entwickeln!"
fi
