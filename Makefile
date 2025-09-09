# Datei: Makefile

# Variablen
DB_CONTAINER=tasks-db
APP_CONTAINER=tasks-app
DB_IMAGE=postgres:alpine
APP_IMAGE=myimage:tasks-app
DB_PORT=5432
APP_PORT=8080
VOLUME=pgdata
ENV_FILE=.env
BACKUP_DIR=backup
TIMESTAMP=$(shell date +"%Y-%m-%d_%H-%M")
BACKUP_FILE=$(BACKUP_DIR)/pgdata-backup-$(TIMESTAMP).tar.gz

# Selbstvorstellung des Systems
about:
	@echo ""
	@echo "📘 ABOUT – Der Projekt im Überblick"
	@echo ""
	@echo "🤖 Dieses System ist dein persönlicher Dev-Copilot:"
	@echo ""
	@echo "📦 Funktionen:"
	@echo "  • Container starten und stoppen (make db-up, make app-up, make stop)"
	@echo "  • Backup & Restore (make backup, make restore)"
	@echo "  • Build & Release (make build, make release)"
	@echo "  • Healthchecks & Tests (make test)"
	@echo "  • SQL-Initialisierung (make init-db)"
	@echo "  • Logging & Status (make logs, make logs-db, make status)"
	@echo "  • Aufräumen & Deployment (make clean, make deploy)"
	@echo ""
	@echo "🧠 Ziel: Klarheit, Kontrolle und Wiederholbarkeit in deinem Dev-Workflow"
	@echo "💡 Für alle Befehle: make help"
	@echo ""


# 📘 Hilfe anzeigen
help:
	@echo ""
	@echo "🛠️  Verfügbare Befehle:"
	@echo "  make db-up       # Startet die Datenbank"
	@echo "  make app-up      # Startet die App"
	@echo "  make stop        # Stoppt beide Container"
	@echo "  make backup      # Erstellt ein Backup"
	@echo "  make restore     # Stellt ein Backup wieder her"
	@echo "  make status      # Zeigt laufende Container"
	@echo "  make logs        # Zeigt Logs der App"
	@echo "  make logs-db     # Zeigt Logs der Datenbank"
	@echo "  make build       # Baut das App-Image neu"
	@echo "  make test        # Testet App & DB"
	@echo "  make init-db     # Führt SQL-Initialisierung aus"
	@echo "  make deploy      # Simuliert Deployment"
	@echo "  make clean       # Entfernt Backups & alte Container"
	@echo "  make release     # Baut & taggt neues Release"
	@echo "  make release-all VERSION=x.y.z  # Baut & taggt neues Release + Git-Tag"
	@echo "  make help        # Zeigt diese Hilfe"
	@echo ""


# Startet die Datenbank
db-up:
	@echo "🚀 Starte PostgreSQL Container..."
	docker run -d \
        --name $(DB_CONTAINER) \
        --env-file $(ENV_FILE) \
        -v $(VOLUME):/var/lib/postgresql/data \
        -p $(DB_PORT):5432 \
        $(DB_IMAGE)

# Startet die App
app-up:
	@echo "🚀 Starte Spring Boot App Container..."
	docker run -d \
    	--name $(APP_CONTAINER) \
        --env-file $(ENV_FILE) \
        -p $(APP_PORT):8080 \
        $(APP_IMAGE)

# Stoppt und entfernt beide Container
stop:
	@echo "🛑 Stoppe App-Container..."
	-docker rm -f $(APP_CONTAINER)
	@echo "🛑 Stoppe DB-Container..."
	-docker rm -f $(DB_CONTAINER)
	@echo "✅ Alle Container wurden gestoppt und entfernt."

# Zeigt laufende Container
status:
	@echo "📊 Aktuelle Container:"
	docker ps

# Zeigt Logs der App
logs:
	@echo "📜 Logs der App:"
	docker logs $(APP_CONTAINER)

# Zeigt Logs der DB
logs-db:
	@echo "📜 Logs der Datenbank:"
	docker logs $(DB_CONTAINER)


# Baut das App-Image neu
build:
	@echo "🔧 Baue App-Image neu..."
	docker build -t $(APP_IMAGE) .

# 🧪 Führt einen einfachen Verbindungstest aus
# Man kann später make test erweitern mit echten Integrationstests oder sogar JUnit-Tests aus der App
test:
	@echo ""
	@echo "🧪 Teste Verbindung zur App und Datenbank..."
	@echo ""
	@echo "🌐 Teste App-Endpunkt:"
	@if curl -s http://localhost:$(APP_PORT)/actuator/health | grep -q '"status":"UP"'; then \
        echo "✅ App ist erreichbar"; \
    else \
        echo "❌ App nicht erreichbar"; \
    fi
	@echo ""
	@echo "🗃️  Teste Datenbankverbindung:"
	@docker exec -i $(DB_CONTAINER) psql -U postgres -d tasks_db -c "\dt" > /tmp/dbtest.log 2>&1 && \
        echo "✅ Datenbank ist erreichbar" && \
        cat /tmp/dbtest.log || \
        echo "❌ Datenbank nicht erreichbar"
	@echo ""

# Backup des Datenbank-Volumes
backup:
	@echo "📦 Erstelle Backup von Volume '$(VOLUME)'..."
	mkdir -p $(BACKUP_DIR)
	docker run --rm \
        -v $(VOLUME):/data \
        -v $(PWD)/$(BACKUP_DIR):/backup \
        alpine \
        tar czf /backup/pgdata-backup-$(TIMESTAMP).tar.gz /data
    @echo "✅ Backup gespeichert unter: $(BACKUP_FILE)"

# 🔁 Stellt ein Backup wieder her
# 🧪 Verwendung
# make restore FILE=pgdata-backup-2025-09-09_13-00.tar.gz
# make test
restore:
	@echo "♻️  Stelle Backup wieder her..."
	@if [ -z "$$FILE" ]; then \
        echo "❌ Bitte gib den Backup-Dateinamen an: make restore FILE=pgdata-backup-YYYY-MM-DD_HH-MM.tar.gz"; \
        exit 1; \
    fi
	docker run --rm \
        -v $(VOLUME):/data \
        -v $(PWD)/$(BACKUP_DIR):/backup \
        alpine \
        tar xzf /backup/$$FILE -C /

# Simuliert ein Deployment
# Image bauen, taggen, push
# ➡️ man kann später echte Deployments einbauen, z.B. scp, rsync, docker push, etc.
deploy:
	@echo "🚀 Starte Deployment..."
	@echo "📦 Baue App-Image..."
	#docker build -t $(APP_IMAGE) .
	@echo "📤 (Simuliert) Push auf Server oder Registry..."
	@echo "✅ Deployment abgeschlossen (Demo-Modus)"

# Führt ein SQL-Skript aus
init-db:
	@echo "🧱 Führe SQL-Initialisierung aus..."
	@if [ ! -f init.sql ]; then \
        echo "❌ Datei 'init.sql' nicht gefunden"; \
        exit 1; \
    fi
	docker cp init.sql $(DB_CONTAINER):/init.sql
	docker exec -i $(DB_CONTAINER) psql -U postgres -d tasks_db -f /init.sql
	@echo "✅ SQL-Skript ausgeführt"

# Aufräumen: Löscht alte Backups, Entfernt gestoppte Container, Entfernt ungenutzte Docker-Images
clean:
	@echo "🧹 Räume Projektverzeichnis auf..."
	@echo "🗑️  Entferne alte Backups..."
	-rm -rf $(BACKUP_DIR)/*
	@echo "🐳 Entferne gestoppte Container..."
	-docker container prune -f
	@echo "📦 Entferne ungenutzte Images..."
	-docker image prune -f
	@echo "✅ Bereinigung abgeschlossen"

# Projekt auf die Bühne bringen
release:
	@echo "🚀 Starte Release-Prozess..."
	@echo "🔧 Baue neues App-Image..."
	docker build -t $(APP_IMAGE):latest .
	@echo "🏷️  Tagge Image mit Zeitstempel..."
	docker tag $(APP_IMAGE):latest $(APP_IMAGE):$(TIMESTAMP)
	@echo "📤 (Demo) Push zu Registry..."
	@echo "✅ Release abgeschlossen: $(APP_IMAGE):$(TIMESTAMP)"

version:
	@echo ""
	@echo "📦 Versionsinfo:"
	@echo "🕒 Zeitstempel: $(TIMESTAMP)"
	@echo "🔖 Git-Tag: $$(git describe --tags --abbrev=0 2>/dev/null || echo 'Kein Tag gefunden')"
	@echo "🔧 Letzter Commit: $$(git rev-parse --short HEAD)"
	@echo "🐳 Docker-Image: $(APP_IMAGE)"
	@echo ""

# 🧪 Verwendung
# make release VERSION=1.0.0
# ➡️ Ergebnis:
# Docker-Image myimage:tasks-app:1.0.0 wird gebaut und getaggt
# Git-Tag v1.0.0 wird erstellt und zu origin gepusht
# Man bekommt eine saubere, nachvollziehbare Versionierung
release-all:
	@if [ -z "$(VERSION)" ]; then \
        echo "❌ Bitte gib eine Versionsnummer an: make release VERSION=1.0.0"; \
        exit 1; \
    fi
	@echo ""
	@echo "🚀 Starte Release-Prozess für Version $(VERSION)..."
	@echo "🔧 Baue neues App-Image..."
	docker build -t $(APP_IMAGE):latest .
	@echo "🏷️  Tagge Image mit Version $(VERSION)..."
	docker tag $(APP_IMAGE):latest $(APP_IMAGE):$(VERSION)
	@echo "🔖 Setze Git-Tag v$(VERSION)..."
	git tag -a v$(VERSION) -m "Release v$(VERSION)"
	git push origin v$(VERSION)
	@echo "📤 (Demo) Push zu Registry..."
	@echo "✅ Release abgeschlossen: $(APP_IMAGE):$(VERSION) + Git-Tag v$(VERSION)"
	@echo ""



# make watch für Live-Logs
# make migrate für Flyway-DB-Migrationen
# make test-db
# make test-app
# JSON-Parsing für komplexere Healthchecks
# make version
# make changelog
# make version-check
# make release
# make release-all
# make rollback VERSION=...
# make publish
# Vorlage für CHANGELOG.md
# make changelog-Target
# Einbau eines wait-for-db.sh, das die App verzögert startet.