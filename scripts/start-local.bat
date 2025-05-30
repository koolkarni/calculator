@echo off
cd /d %~dp0..

echo Stopping Redis and Jaeger...
docker compose down --volumes --remove-orphans

echo Starting Redis and Jaeger...
docker compose up -d redis jaeger