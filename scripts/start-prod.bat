@echo off
cd /d %~dp0..

echo Stopping and cleaning up old prod containers...
docker compose -f docker-compose.prod.yml down --volumes

echo Building and starting production containers...
docker compose -f docker-compose.prod.yml up --build -d
