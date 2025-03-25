#!/bin/sh
# Проверка доступности PostgreSQL

host="$1"
port="$2"
shift 2
cmd="$@"

echo "Waiting for PostgreSQL at $host:$port..."

until PGPASSWORD=password psql -h "$host" -p "$port" -U postgres -c '\q' 2>/dev/null; do
  echo "Waiting..."
  sleep 2
done

echo "PostgreSQL is ready - executing command: $cmd"
exec $cmd