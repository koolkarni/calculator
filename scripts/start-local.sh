#!/bin/bash
cd "$(dirname "$0")/.."

echo "Stopping and cleaning old containers..."
docker compose down --volumes

echo "Starting containers with build..."
docker compose up --build -d