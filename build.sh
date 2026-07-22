#!/usr/bin/env bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"
BUILD_DIR="$PROJECT_ROOT/build/classes"
DIST_DIR="$PROJECT_ROOT/dist"

rm -rf "$PROJECT_ROOT/build" "$DIST_DIR"
mkdir -p "$BUILD_DIR" "$DIST_DIR"

javac -encoding UTF-8 -d "$BUILD_DIR" "$PROJECT_ROOT"/src/game/*.java
jar --create \
  --file "$DIST_DIR/worlds-hardest-game.jar" \
  --main-class game.WorldsHardestGame \
  -C "$BUILD_DIR" .

echo "Built $DIST_DIR/worlds-hardest-game.jar"
