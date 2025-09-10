# build, test, clean, deploy
build:
	@echo "🔧 Baue App-Image..."
	docker build -t $(APP_IMAGE) .

test:
	@echo "🧪 Teste App & DB..."
	@if curl -s http://localhost:$(APP_PORT)/actuator/health | grep -q '"status":"UP"'; then \
        echo "✅ App ist erreichbar"; \
    else \
        echo "❌ App nicht erreichbar"; \
    fi
	@docker exec -i $(DB_CONTAINER) psql -U postgres -d tasks_db -c "\dt" > /tmp/dbtest.log 2>&1 && \
        echo "✅ DB erreichbar" && cat /tmp/dbtest.log || echo "❌ DB nicht erreichbar"

clean:
	@echo "🧹 Bereinige Projekt..."
	-rm -rf $(BACKUP_DIR)/*
	-docker container prune -f
	-docker image prune -f

deploy:
	@echo "🚀 Simuliere Deployment..."
	@echo "📤 Push zu Registry (Demo)"

deploy-prod:
	@echo "🚀 Starte produktives Deployment..."
	@echo "📦 Baue Image..."
	docker build -t $(APP_IMAGE):prod .
	@echo "🏷️  Tagge Image als 'prod'..."
	docker tag $(APP_IMAGE):prod $(APP_IMAGE):$(TIMESTAMP)
	@echo "📤 Push zu Registry..."
	docker push $(APP_IMAGE):prod
	docker push $(APP_IMAGE):$(TIMESTAMP)
	@echo "✅ Deployment abgeschlossen: $(APP_IMAGE):prod + $(TIMESTAMP)"

