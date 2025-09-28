# Haupt-Makefile mit Includes & Routing
# 🔧 Container-Management (db-up, app-up, stop, status, logs)

# 🗃️ Datenbankpflege (backup, restore, init-db)

# 🧪 Entwicklungs-Tools (build, test, clean, deploy, deploy-prod)

# 🏷️ Versions-Workflow (version, release, release-all, rollback, changelog, release-notes, tag-latest, publish)

#include makefiles/containers.mk
#include makefiles/db.mk
#include makefiles/dev.mk
#include makefiles/versioning.mk

# Variablen
#DB_CONTAINER ?= tasks-db
#APP_CONTAINER=tasks-app
#DB_IMAGE ?= postgres:alpine
#APP_IMAGE=myimage:tasks-app
#DB_PORT ?= 5432
#APP_PORT=8080
#VOLUME ?= pgdata
#ENV_FILE ?= .env.container-dev
#BACKUP_DIR=backup
#TIMESTAMP=$(shell date +"%Y-%m-%d_%H-%M")
#BACKUP_FILE=$(BACKUP_DIR)/pgdata-backup-$(TIMESTAMP).tar.gz

#.DEFAULT_GOAL := help

#help:
#	@echo ""
#	@echo "🛠️ Verfügbare Befehle für tasks:"
#	@echo "  make db-up, app-up, stop, status, logs, logs-db"
#	@echo "  make backup, restore FILE=..., init-db"
#	@echo "  make build, test, clean, deploy"
#	@echo "  make version, release, release-all VERSION=..., changelog FROM=... TO=..., rollback VERSION=..., publish VERSION=..."
#	@echo "  make tag-latest                 # Setzt Git-Tag 'latest' auf aktuellen Commit"
#	@echo "  make release-notes FROM=x TO=y  # Generiert kompakte Release Notes"
#	@echo "  make deploy-prod                # Führt produktives Deployment durch"
#	@echo ""

# Makefile für containerisierte Dev-Umgebung
#include makefiles/Makefile.local
#.PHONY: run-dev stop-dev restart-dev logs db-reset status


# 🧩 Haupt-Makefile: Umgebungseinbindung

include Makefile.docker
include Makefile.compose

.DEFAULT_GOAL := help-all

#help:
#	@echo "📦 Hauptübersicht:"
#	@echo "  make help-docker     # Docker-Umgebung (manuell)"
#	@echo "  make help-compose    # Compose-Umgebung"
#	@echo "  make help-all        # Alle Hilfetexte kombiniert"

# help-all: help-docker help-compose
help-all:
	@echo "📦 Übersicht aller Makefile-Kommandos:"
	@echo ""
	@echo "🐳 Docker Targets:"
	@$(MAKE) --no-print-directory help-docker
	@echo ""
	@echo "🐙 Compose Targets:"
	@$(MAKE) --no-print-directory help-compose

#	@echo ""
#	@echo "📚 Weitere Module:"
#	@$(MAKE) help-db || true
#	@$(MAKE) help-test || true



