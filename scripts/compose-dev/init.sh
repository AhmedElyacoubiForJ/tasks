#!/bin/bash
# =====================================================================
# ⚙️ dev init — installiert das compose-dev CLI dauerhaft und fehlerfrei
# =====================================================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
DEV_SCRIPT="$ROOT_DIR/scripts/compose-dev/dev"

# Shell erkennen
if [[ "$SHELL" == *"zsh"* ]]; then
    RC_FILE="$HOME/.zshrc"
else
    RC_FILE="$HOME/.bashrc"
fi

echo "🔍 Erkenne absoluten Pfad..."
echo "   Projektpfad: $ROOT_DIR"
echo "   Shell-Konfiguration: $RC_FILE"

# ----------------------------------------
# Alias vorbereiten
# ----------------------------------------
ALIAS_CMD="alias dev=\"$DEV_SCRIPT\""

echo "🔧 Bereinige alte oder kaputte Alias-Einträge..."
sed -i '/alias dev=/d' "$RC_FILE"

echo "➕ Füge neuen Alias hinzu..."
echo "" >> "$RC_FILE"
echo "# compose-dev CLI" >> "$RC_FILE"
echo "$ALIAS_CMD" >> "$RC_FILE"

# ----------------------------------------
# Rechte setzen
# ----------------------------------------
echo "🔐 Setze Ausführungsrechte..."
chmod +x "$DEV_SCRIPT"

# ----------------------------------------
# Shell neu laden
# ----------------------------------------
echo "🔄 Lade Shell-Konfiguration neu..."
# Wichtig: Nur für aktuelle Shell, nicht für Eltern-Shell
source "$RC_FILE"

echo ""
echo "🎉 Installation abgeschlossen!"
echo "Der Befehl 'dev' ist jetzt verfügbar."
echo ""
echo "Teste z.B.:"
echo "   dev status"
echo "   dev up"
echo ""
echo "Falls es nicht sofort geht, öffne ein neues Terminal."
