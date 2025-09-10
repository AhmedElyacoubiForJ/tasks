# Backup, Restore, init-db
backup:
	@echo "📦 Erstelle Backup..."
	mkdir -p $(BACKUP_DIR)
	docker run --rm -v $(VOLUME):/data -v $(PWD)/$(BACKUP_DIR):/backup alpine tar czf /backup/pgdata-backup-$(TIMESTAMP).tar.gz /data

restore:
	@echo "♻️  Stelle Backup wieder her..."
	@if [ -z "$$FILE" ]; then \
        echo "❌ Bitte gib FILE an: make restore FILE=pgdata-backup-YYYY-MM-DD_HH-MM.tar.gz"; \
        exit 1; \
    fi
	docker run --rm -v $(VOLUME):/data -v $(PWD)/$(BACKUP_DIR):/backup alpine tar xzf /backup/$$FILE -C /

init-db:
	@echo "🧱 Führe SQL-Initialisierung aus..."
	@if [ ! -f init.sql ]; then \
        echo "❌ Datei 'init.sql' fehlt"; \
        exit 1; \
    fi
	docker cp init.sql $(DB_CONTAINER):/init.sql
	docker exec -i $(DB_CONTAINER) psql -U postgres -d tasks_db -f /init.sql
