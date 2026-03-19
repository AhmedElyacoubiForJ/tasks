#!/bin/bash
# =====================================================================
# 🧩 dev completion — Tab-Autocomplete für das compose-dev CLI
# =====================================================================

COMPLETION_FILE="$HOME/.dev_completion"

# Welche Shell?
if [[ "$SHELL" == *"zsh"* ]]; then
    RC_FILE="$HOME/.zshrc"
else
    RC_FILE="$HOME/.bashrc"
fi

echo "🔍 Prüfe bestehende Completion-Datei..."

# Wenn Datei existiert, aber kaputt ist → löschen
if [[ -f "$COMPLETION_FILE" ]]; then
    if ! grep -q "_dev()" "$COMPLETION_FILE"; then
        echo "⚠️ Alte oder beschädigte Completion gefunden – wird entfernt."
        rm -f "$COMPLETION_FILE"
    else
        echo "✔ Bestehende Completion ist gültig."
    fi
fi

echo "🛠️ Erzeuge neue Completion-Datei..."

cat > "$COMPLETION_FILE" << 'EOF'
# Autocomplete für 'dev'
_dev() {
    local cur prev opts
    COMPREPLY=()
    cur="${COMP_WORDS[COMP_CWORD]}"

    opts="up down build logs db doctor status restart rebuild clean-all help completion"

    case "${COMP_WORDS[1]}" in
        logs)
            COMPREPLY=( $(compgen -W "app db all env check" -- "$cur") )
            return 0
            ;;
        db)
            COMPREPLY=( $(compgen -W "shell tables users schemas exec reset info" -- "$cur") )
            return 0
            ;;
        completion)
            COMPREPLY=( $(compgen -W "install" -- "$cur") )
            return 0
            ;;
    esac

    COMPREPLY=( $(compgen -W "$opts" -- "$cur") )
    return 0
}

complete -F _dev dev
EOF

echo "📦 Completion-Datei erstellt: $COMPLETION_FILE"

echo "🔧 Trage Completion in $RC_FILE ein..."

# Alte Einträge entfernen
sed -i '/\.dev_completion/d' "$RC_FILE"

# Neue Zeile hinzufügen
echo "" >> "$RC_FILE"
echo "# compose-dev Autocomplete" >> "$RC_FILE"
echo "source ~/.dev_completion" >> "$RC_FILE"

echo "🔄 Lade Shell-Konfiguration neu..."
source "$RC_FILE"

echo ""
echo "🎉 Tab-Autocomplete installiert!"
echo "   Beispiel: tippe 'dev d<TAB>' → 'dev down'"
echo ""
