# Container-Management (db-up, app-up, stop, status, logs)
db-up:
	@echo "🚀 Starte PostgreSQL Container..."
	docker run -d --name $(DB_CONTAINER) --env-file $(ENV_FILE) -v $(VOLUME):/var/lib/postgresql/data -p $(DB_PORT):5432 $(DB_IMAGE)

app-up:
	@echo "🚀 Starte Spring Boot App Container..."
	docker run -d --name $(APP_CONTAINER) --env-file $(ENV_FILE) -p $(APP_PORT):8080 $(APP_IMAGE)

stop:
	@echo "🛑 Stoppe Container..."
	-docker rm -f $(APP_CONTAINER)
	-docker rm -f $(DB_CONTAINER)

status:
	@echo "📊 Aktuelle Container:"
	docker ps

logs:
	@echo "📜 Logs der App:"
	docker logs $(APP_CONTAINER)

logs-db:
	@echo "📜 Logs der Datenbank:"
	docker logs $(DB_CONTAINER)
