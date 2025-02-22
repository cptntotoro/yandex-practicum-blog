#!/bin/bash

set -e

echo "=== BUILDING CONTAINERS ==="
docker-compose build

echo "=== RUNNING TESTS ==="
# Запускаем тесты в foreground режиме (без -d)
docker-compose --profile test up --abort-on-container-exit test

echo "=== STARTING PRODUCTION ==="
docker-compose --profile prod up -d

echo "=== SYSTEM READY ==="