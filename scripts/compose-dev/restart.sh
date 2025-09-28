#!/bin/bash
echo "🔄 Starte compose-dev Umgebung neu..."
bash ./scripts/compose-dev/down.sh
bash ./scripts/compose-dev/up.sh
