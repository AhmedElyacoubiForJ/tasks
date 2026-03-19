#!/bin/bash
# =====================================================================
# 🏗️ dev rebuild — build + up
# =====================================================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "🏗️ Baue Images neu..."
source "$SCRIPT_DIR/build.sh"

echo "🚀 Starte compose-dev..."
source "$SCRIPT_DIR/up.sh"

echo "✔ Rebuild abgeschlossen."
