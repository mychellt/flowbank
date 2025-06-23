#!/usr/bin/env bash

# ==========================================================
# 🛑 Shutdown Script
# ==========================================================
# What it does:
# 1️⃣ Stops and removes all running containers.
# 2️⃣ Displays status and helpful tips.
#
# Usage:
#    chmod +x shutdown.sh
#    ./shutdown.sh
# ==========================================================

set -e

echo "🛑 Stopping services..."
docker compose down

echo "✅ Services stopped."
echo "💡 To restart: ./run.sh"
