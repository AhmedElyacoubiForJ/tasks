#!/bin/bash

# Basis-Verzeichnis der Tests
TEST_DIR="src/test/java"

echo "📦 Scanne alle Testklassen unterhalb von: $TEST_DIR"
echo "🔍 Filter: */api/*"
echo "----------------------------------------------"

# Alle Testklassen finden, die irgendwo unter /api/ liegen
find "$TEST_DIR" \
  -type f \
  -name "*Test*.java" \
  | grep "/api/" \
  | sort

echo "----------------------------------------------"
echo "✅ Fertig. Insgesamt gefundene Testklassen:"
find "$TEST_DIR" -type f -name "*Test.java" | grep "/api/" | wc -l
