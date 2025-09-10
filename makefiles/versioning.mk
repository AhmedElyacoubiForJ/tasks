# version, release, release-all, changelog, rollback, publish
version:
	@echo "📦 Versionsinfo:"
	@echo "🕒 Zeitstempel: $(TIMESTAMP)"
	@echo "🔖 Git-Tag: $$(git describe --tags --abbrev=0 2>/dev/null || echo 'Kein Tag gefunden')"
	@echo "🔧 Commit: $$(git rev-parse --short HEAD)"
	@echo "🐳 Docker-Image: $(APP_IMAGE)"

release:
	@echo "🚀 Starte Release..."
	docker build -t $(APP_IMAGE):latest .
	docker tag $(APP_IMAGE):latest $(APP_IMAGE):$(TIMESTAMP)

release-all-1:
	@if [ -z "$(VERSION)" ]; then \
        echo "❌ make release-all VERSION=x.y.z"; exit 1; \
    fi
	docker build -t $(APP_IMAGE):latest .
	docker tag $(APP_IMAGE):latest $(APP_IMAGE):$(VERSION)
	git tag -a v$(VERSION) -m "Release v$(VERSION)"
	git push origin v$(VERSION)

changelog:
	@if [ -z "$(FROM)" ] || [ -z "$(TO)" ]; then \
        echo "❌ make changelog FROM=x TO=y"; exit 1; \
    fi
	git log $(FROM)..$(TO) --oneline --no-merges

rollback:
	@if [ -z "$(VERSION)" ]; then \
        echo "❌ make rollback VERSION=x.y.z"; exit 1; \
    fi
	git checkout $(VERSION)
	@echo "💡 Du bist im 'detached HEAD'"

publish:
	@if [ -z "$(VERSION)" ]; then \
        echo "❌ make publish VERSION=x.y.z"; exit 1; \
    fi
	docker push $(APP_IMAGE):$(VERSION)
	git push origin v$(VERSION)

tag-latest:
	@echo "🏷️  Setze Tag 'latest' auf aktuellen Commit..."
	git tag -f latest
	git push origin latest --force
	@echo "✅ Tag 'latest' wurde aktualisiert"

release-notes:
	@if [ -z "$(FROM)" ] || [ -z "$(TO)" ]; then \
        echo "❌ Bitte gib beide Versionen an: make release-notes FROM=v0.1.0 TO=v0.2.0"; \
        exit 1; \
    fi
	@echo "📝 Release Notes von $(FROM) → $(TO):"
	@git log $(FROM)..$(TO) --pretty=format:"- %s"
# 🔧 Verwendung:
# export GITHUB_TOKEN=ghp_...
# make release-github VERSION=1.0.0
# Du bekommst ein GitHub Release mit Tag v1.0.0 und Beschreibung.
release-github:
	@if [ -z "$(VERSION)" ]; then \
        echo "❌ Bitte gib eine Versionsnummer an: make release-github VERSION=1.0.0"; \
        exit 1; \
    fi
	@if [ -z "$(GITHUB_TOKEN)" ]; then \
        echo "❌ GITHUB_TOKEN fehlt. Setze ihn mit: export GITHUB_TOKEN=..."; \
        exit 1; \
    fi
	@echo "🚀 Erstelle GitHub Release für Version $(VERSION)..."
	curl -s -X POST https://api.github.com/repos/<USER>/<REPO>/releases \
        -H "Authorization: token $(GITHUB_TOKEN)" \
        -H "Content-Type: application/json" \
		-d '{"tag_name": "v$(VERSION)", "name": "Release v$(VERSION)", "body": "Siehe CHANGELOG.md für Details.", "draft": false, "prerelease": false}'
	@echo "✅ GitHub Release erstellt: v$(VERSION)"

# 🧪 Verwendung
# make changelog-md FROM=v0.1.0 TO=v0.2.0-dev
changelog-md:
	@if grep -q "## [$(TO)]" CHANGELOG.md; then \
        echo "⚠️ CHANGELOG.md enthält bereits Eintrag für $(TO). Abbruch."; \
        exit 1; \
    fi
	@echo "## [$(TO)] – $(shell date +%Y-%m-%d)" >> CHANGELOG.md
	@git log $(FROM)..$(TO) --oneline --no-merges | sed 's/^/- /' >> CHANGELOG.md


changelog-preview:
	@git log $(FROM)..$(TO) --pretty=format:"- %s"

# 📘 Beispiel: Verwendung
# ➡️ Ergebnis:
# Docker-Image myimage:tasks-app:1.0.0 gebaut
# Git-Tag v1.0.0 gesetzt und gepusht
# CHANGELOG.md automatisch aktualisiert mit allen Änderungen seit dem letzten Tag
release-all:
	@if [ -z "$(VERSION)" ]; then \
        echo "❌ Bitte gib eine Versionsnummer an: make release-all VERSION=1.0.0"; \
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
	@echo "📤 Push zu Registry..."
	docker push $(APP_IMAGE):$(VERSION)
	@echo "📝 Aktualisiere CHANGELOG.md..."
	@make changelog-md FROM=$$(git describe --tags --abbrev=0 HEAD^) TO=v$(VERSION)
	@echo "✅ Release abgeschlossen: $(APP_IMAGE):$(VERSION) + Git-Tag v$(VERSION)"
	@echo ""





