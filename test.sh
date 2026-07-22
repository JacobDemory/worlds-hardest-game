#!/usr/bin/env bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"
TEST_BUILD_DIR="$PROJECT_ROOT/build/test-classes"

rm -rf "$TEST_BUILD_DIR"
mkdir -p "$TEST_BUILD_DIR"

javac -encoding UTF-8 \
  -d "$TEST_BUILD_DIR" \
  "$PROJECT_ROOT"/src/game/*.java \
  "$PROJECT_ROOT"/src/test/java/game/*.java

java -ea -cp "$TEST_BUILD_DIR" game.CollisionSmokeTest
