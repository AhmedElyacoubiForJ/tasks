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

# ----------------------------------------
# 🐳 Docker Dev Workflow
# ----------------------------------------Umgebung

ROOT := $(shell dirname $(realpath $(lastword $(MAKEFILE_LIST))))
SCRIPTS := $(ROOT)/scripts/docker

dev:
	bash $(SCRIPTS)/dev.sh

stop:
	bash $(SCRIPTS)/stop.sh

restart:
	bash $(SCRIPTS)/restart.sh

logs:
	bash $(SCRIPTS)/logs.sh

status:
	- bash $(SCRIPTS)/status.sh

init-db:
	bash $(SCRIPTS)/init-db.sh

reset-db:
	bash $(SCRIPTS)/reset-db.sh

health:
	bash $(SCRIPTS)/health.sh

check-env:
	bash $(SCRIPTS)/check-env.sh

verify:
	@echo "🔍 Starte vollständige Umgebungprüfung..."
	@bash $(SCRIPTS)/check-env.sh
	@bash $(SCRIPTS)/status.sh || true
	@bash $(SCRIPTS)/health.sh || true


