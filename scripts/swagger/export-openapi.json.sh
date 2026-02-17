#!/bin/bash

# -----------------------------------------
# Export OpenAPI JSON to docs/swagger
# -----------------------------------------

OUTPUT_DIR="docs/swagger"
OUTPUT_FILE="$OUTPUT_DIR/openapi.json"
OPENAPI_URL="http://localhost:8080/v3/api-docs"

echo "📘 Exporting OpenAPI JSON..."

# Create directory if missing
mkdir -p "$OUTPUT_DIR"

# Download JSON
curl -s "$OPENAPI_URL" -o "$OUTPUT_FILE"

if [ $? -eq 0 ]; then
  echo "✅ OpenAPI JSON exported successfully:"
  echo "   → $OUTPUT_FILE"
else
  echo "❌ Failed to export OpenAPI JSON"
  exit 1
fi
