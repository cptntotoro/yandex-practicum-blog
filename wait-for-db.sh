#!/bin/sh

host="$1"
port="$2"
shift 2
cmd="$@"

# Используем psql для проверки доступности БД
until PGPASSWORD=password psql -h "$host" -p "$port" -U postgres -d test_db -c '\q' 2>/dev/null; do
  echo "Waiting for PostgreSQL at $host:$port..."
  sleep 2
done

echo "PostgreSQL is ready - executing command"
exec $cmd