#!/usr/bin/env bash

# ==========================================================
# 🚀 Run Script
# ==========================================================
# What it does:
# 1️⃣ Builds the app
# 2️⃣ Starts docker services
# 3️⃣ Displays status & helpful instructions
#
# Usage:
#    ./run.sh
# ==========================================================

set -e

# ==========================================================
# 🌳 Environment
# ==========================================================
export $(grep -v '^#' .env | xargs)

# ==========================================================
# 🐳 Build
# ==========================================================
echo "🔨 Building app docker image..."
docker compose build

# ==========================================================
# 🏁 Start Services
# ==========================================================
echo "🚀 Starting services..."
docker compose up -d

# ==========================================================
# ✅ Done
# ==========================================================
echo "🌟 Services started successfully!"
echo "👉 App available at http://localhost:${SPRING_SERVER_PORT}/swagger-ui/index.html"
echo "👉 Actuator Console at http://localhost:${SPRING_SERVER_PORT}/actuator"
echo "👉 Database available at ${SPRING_DATASOURCE_HOST}:${SPRING_DATASOURCE_PORT}"
echo
echo "💡 To view logs: docker compose logs -f"
echo "💡 To stop services: docker compose down"
echo "💡 To run tests: mvn test"
