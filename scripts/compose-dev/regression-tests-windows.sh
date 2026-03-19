#!/bin/bash

echo "🧪 Starte Regression Tests (Windows)..."

echo "⏳ Warte auf API (http://localhost:8080/actuator/health)..."

until curl -s http://localhost:8080/actuator/health | grep -q '"status":"UP"'; do
  sleep 2
done

echo "✔ API ist erreichbar und gesund."

./mvnw -Dtest=TaskApiFullTestSuite test

echo "✔ Regression Tests abgeschlossen."
