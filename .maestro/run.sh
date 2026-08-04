#!/usr/bin/env bash
# Runs Maestro flows with credentials loaded from .maestro/.env (git-ignored).
#
# Usage:
#   .maestro/run.sh .maestro/home/            # a folder
#   .maestro/run.sh .maestro/home/home_screen-renders.yaml   # one flow
#   .maestro/run.sh --include-tags=smoke .maestro/           # pass extra maestro args
#
# This Maestro version has no --env-file, so each KEY=VALUE line in
# .maestro/.env is passed as a separate `-e` argument (never collapsed into
# one string — that corrupts values, e.g. the email field getting the password).

set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ENV_FILE="$ROOT/.maestro/.env"

ENVARGS=()
if [[ -f "$ENV_FILE" ]]; then
  while IFS= read -r line; do
    [[ "$line" =~ ^[[:space:]]*# || -z "${line// }" ]] && continue
    ENVARGS+=(-e "$line")
  done < "$ENV_FILE"
else
  echo "WARN: $ENV_FILE not found — authenticated flows will fail without EMAIL/PASSWORD." >&2
fi

DEVICE="${MAESTRO_DEVICE:-emulator-5554}"
exec maestro --device "$DEVICE" test "${ENVARGS[@]}" "$@"
