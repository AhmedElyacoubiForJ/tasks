# Container-Management (db-up, app-up, stop, status, logs)
db-up:
	@echo "🚀 Starte PostgreSQL Container mit $(ENV_FILE)..."
	docker run -d --name $(DB_CONTAINER) \
        --env-file $(ENV_FILE) \
        -v $(VOLUME):/var/lib/postgresql/data \
        -p $(DB_PORT):5432 \
        $(DB_IMAGE)


db-down:
	@echo "🛑 Stoppe Container: $(DB_CONTAINER)"
	docker rm -f $(DB_CONTAINER)


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

# Makefile für Container-Workflows

#PROFILE ?= container-dev

#docker-build:
#    docker build -t myimage:tasks-app .

#docker-run:
#    SPRING_PROFILES_ACTIVE=$(PROFILE) docker run myimage:tasks-app

#compose-up:
#    SPRING_PROFILES_ACTIVE=$(PROFILE) docker-compose up --build

#make docker-build
#make docker-run PROFILE=container-dev
#make compose-up PROFILE=prod


