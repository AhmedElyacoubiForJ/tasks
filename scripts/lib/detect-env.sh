#!/usr/bin/env bash

###############################################
# Debug-Modus aktivieren:
#   export DEBUG=true
#   source ./scripts/lib/detect-env.sh
###############################################

debug() {
  if [[ "${DEBUG:-false}" == "true" ]]; then
    echo "[detect-env] $1"
  fi
}

debug "Starte Environment Detection..."
debug "OSTYPE = $OSTYPE"
debug "uname -s = $(uname -s 2>/dev/null || echo 'N/A')"

# Standardwerte
IS_WSL="false"
IS_GITBASH="false"
IS_LINUX="false"
IS_MAC="false"
IS_WINDOWS="false"
ENV_NAME="unknown"

###############################################
# 1) WSL erkennen
###############################################
if [[ -f /proc/version ]]; then
  if grep -qi "microsoft" /proc/version 2>/dev/null; then
    IS_WSL="true"
    ENV_NAME="wsl"
    debug "WSL erkannt über /proc/version"
  else
    debug "WSL NICHT erkannt über /proc/version"
  fi
else
  debug "/proc/version existiert nicht (Git Bash oder Windows)"
fi

###############################################
# 2) Git Bash erkennen
###############################################
if [[ "$OSTYPE" == "msys"* ]] || [[ "$OSTYPE" == "cygwin"* ]]; then
  IS_GITBASH="true"
  IS_WINDOWS="true"
  ENV_NAME="gitbash"
  debug "Git Bash erkannt über OSTYPE"
else
  debug "Git Bash NICHT erkannt über OSTYPE"
fi

###############################################
# 3) macOS erkennen
###############################################
if [[ "$OSTYPE" == "darwin"* ]]; then
  IS_MAC="true"
  ENV_NAME="mac"
  debug "macOS erkannt"
else
  debug "macOS NICHT erkannt"
fi

###############################################
# 4) Linux (native, nicht WSL)
###############################################
if [[ "$OSTYPE" == "linux"* ]] && [[ "$IS_WSL" == "false" ]]; then
  IS_LINUX="true"
  ENV_NAME="linux"
  debug "Native Linux erkannt"
else
  debug "Native Linux NICHT erkannt"
fi

###############################################
# 5) Windows CMD/PowerShell (Fallback)
###############################################
case "$(uname -s)" in
  MINGW*|MSYS*|CYGWIN*)
    IS_WINDOWS="true"
    if [[ "$ENV_NAME" == "unknown" ]]; then
      ENV_NAME="windows"
    fi
    debug "Windows (CMD/PowerShell) erkannt über uname"
    ;;
  *)
    debug "Windows NICHT erkannt über uname"
    ;;
esac

debug "Erkennung abgeschlossen:"
debug "  ENV_NAME   = $ENV_NAME"
debug "  IS_WSL     = $IS_WSL"
debug "  IS_GITBASH = $IS_GITBASH"
debug "  IS_LINUX   = $IS_LINUX"
debug "  IS_MAC     = $IS_MAC"
debug "  IS_WINDOWS = $IS_WINDOWS"
