@echo off
cd /d %~dp0..

echo Stopping and removing previous containers...
docker compose down --volumes

echo Building and starting containers...
docker compose up --build -d