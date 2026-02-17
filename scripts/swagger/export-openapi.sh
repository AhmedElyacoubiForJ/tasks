#!/bin/bash

# -----------------------------------------
# Export OpenAPI YAML to docs/swagger
# -----------------------------------------

OUTPUT_DIR="docs/swagger"
OUTPUT_FILE="$OUTPUT_DIR/openapi.yaml"
OPENAPI_URL="http://localhost:8080/v3/api-docs.yaml"

echo "📘 Exporting OpenAPI YAML..."

# Create directory if missing
mkdir -p "$OUTPUT_DIR"

# Download YAML
curl -s "$OPENAPI_URL" -o "$OUTPUT_FILE"

if [ $? -eq 0 ]; then
  echo "✅ OpenAPI YAML exported successfully:"
  echo "   → $OUTPUT_FILE"
else
  echo "❌ Failed to export OpenAPI YAML"
  exit 1
fi
