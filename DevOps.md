Ein **Skeleton‑Runner im Projekt‑Root**, der später mehrere Profile laden kann und jetzt erst einmal **local-docker-db** integriert, ist genau der richtige nächste Schritt. Damit legst du die Basis für:

- ein echtes Profil‑System
- zukünftige Profile wie `compose-dev`, `ci`, `k8s`, `devops`
- ein einheitliches Routing
- klare Erweiterbarkeit ohne Chaos

Ich skizziere dir jetzt ein **sauberes, minimalistisches, aber zukunftssicheres Skeleton**, das du direkt ins Projekt‑Root legen kannst.

---

## 🟩 Zielbild: `./run` im Root

Der Root‑Runner soll:

- **Profile erkennen**
- **Routing an das jeweilige Profil weitergeben**
- **Hilfe anzeigen**
- **TODO‑Profile sichtbar machen**
- **local-docker-db sofort unterstützen**

Später kannst du Profile einfach ergänzen, ohne den Root‑Runner umzubauen.

---

## 🟦 Skeleton: `./run` (Root)

Das ist ein **komplett lauffähiges Skeleton**, das du 1:1 übernehmen kannst.  
Es lädt jetzt **nur local-docker-db**, aber ist vorbereitet für weitere Profile.

```bash
#!/usr/bin/env bash
set -euo pipefail

###############################################################################
# Root Runner – universeller Einstiegspunkt für alle lokalen Profile
#
# Profile:
#   local-docker-db   → aktiv
#   compose-dev       → TODO
#   devops            → TODO
#   ci                → TODO
#   k8s               → TODO
###############################################################################

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Farben (Fallback)
RED='' GREEN='' YELLOW='' NC=''
if [ -f "$SCRIPT_DIR/scripts/utils/colors.sh" ]; then
    source "$SCRIPT_DIR/scripts/utils/colors.sh"
fi

###############################################################################
# Globale Hilfe
###############################################################################
usage() {
  echo "Usage: ./run <profile> <category> <command> [args...]"
  echo
  echo -e "${YELLOW}Aktive Profile:${NC}"
  echo "  local-docker-db     App lokal, DB in Docker (WSL)"
  echo
  echo -e "${YELLOW}Geplante Profile (TODO):${NC}"
  echo "  compose-dev         Docker Compose Umgebung"
  echo "  devops              Tools, Checks, Pipelines"
  echo "  ci                  CI-nahe lokale Umgebung"
  echo "  k8s                 Kubernetes Dev-Profile"
  echo
  echo "Beispiel:"
  echo "  ./run local-docker-db db up"
  exit 1
}

# Global --help
if [[ "${1:-}" == "--help" ]]; then
    usage
fi

###############################################################################
# Argumentprüfung
###############################################################################
if [[ $# -lt 1 ]]; then
  echo -e "${RED}❌ Kein Profil angegeben.${NC}"
  echo -e "${YELLOW}ℹ️  Hilfe:${NC} ./run --help"
  exit 1
fi

PROFILE="$1"
shift

###############################################################################
# Routing auf Profile
###############################################################################
case "$PROFILE" in

  local-docker-db)
    # Delegation an das bestehende Profil
    exec "$SCRIPT_DIR/scripts/local-docker-db/run" "$@"
    ;;

  compose-dev)
    echo -e "${YELLOW}TODO:${NC} Profil 'compose-dev' ist noch nicht implementiert."
    echo "Geplant: Docker Compose Umgebung für App + DB + Services."
    exit 1
    ;;

  devops)
    echo -e "${YELLOW}TODO:${NC} Profil 'devops' ist noch nicht implementiert."
    echo "Geplant: Tools, Checks, Pipelines, Linting, Security."
    exit 1
    ;;

  ci)
    echo -e "${YELLOW}TODO:${NC} Profil 'ci' ist noch nicht implementiert."
    echo "Geplant: CI-nahe lokale Umgebung."
    exit 1
    ;;

  k8s)
    echo -e "${YELLOW}TODO:${NC} Profil 'k8s' ist noch nicht implementiert."
    echo "Geplant: Kubernetes Dev-Profile."
    exit 1
    ;;

  *)
    echo -e "${RED}❌ Unbekanntes Profil: $PROFILE${NC}"
    echo -e "${YELLOW}ℹ️  Hilfe:${NC} ./run --help"
    exit 1
    ;;
esac
```

---

## 🟩 Was du jetzt hast

### ✔ Ein universelles Root‑Entry‑Script
`./run` ist jetzt der **offizielle Einstiegspunkt** für alle lokalen Profile.

### ✔ local-docker-db ist voll integriert
Alle bisherigen Befehle funktionieren weiterhin:

```
./run local-docker-db db up
./run local-docker-db app jar
./run local-docker-db test regression
```

### ✔ Profile sichtbar, aber noch nicht implementiert
Du siehst jetzt:

- compose-dev (TODO)
- devops (TODO)
- ci (TODO)
- k8s (TODO)

### ✔ Saubere Architektur
Du kannst später einfach neue Profile hinzufügen:

```
scripts/compose-dev/run
scripts/devops/run
scripts/ci/run
scripts/k8s/run
```

Der Root‑Runner bleibt unverändert.

---

## 🟦 Nächster sinnvoller Schritt

Bevor wir `compose-dev` oder `devops` bauen, wäre es gut zu klären:

**Wie soll das Profil-System langfristig aussehen?**

Optionen:

1. **Jedes Profil hat ein eigenes `run`‑Script**
2. **Profile teilen sich Libraries (colors, detect-env, output)**
3. **Ein zentrales Help‑System für alle Profile**
4. **Einheitliche Struktur:**
   ```
   scripts/<profile>/<category>/<command>.sh
   ```

Welche Richtung bevorzugst du?