#!/bin/bash
set -e

# Create Order Service DB
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE ecommerce_order;
    CREATE USER ecommerce_order WITH ENCRYPTED PASSWORD 'ecommerce_order';
    GRANT ALL PRIVILEGES ON DATABASE ecommerce_order TO ecommerce_order;

     -- Grant privileges on schema
     \c ecommerce_order
     GRANT ALL PRIVILEGES ON SCHEMA public TO ecommerce_order;
EOSQL

# Create Notification Service DB
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE ecommerce_notification;
    CREATE USER ecommerce_notification WITH ENCRYPTED PASSWORD 'ecommerce_notification';
    GRANT ALL PRIVILEGES ON DATABASE ecommerce_notification TO ecommerce_notification;

     -- Grant privileges on schema
      \c ecommerce_notification
      GRANT ALL PRIVILEGES ON SCHEMA public TO ecommerce_notification;
EOSQL

# Create Integration Service DB
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE ecommerce_integration;
    CREATE USER ecommerce_integration WITH ENCRYPTED PASSWORD 'ecommerce_integration';
    GRANT ALL PRIVILEGES ON DATABASE ecommerce_integration TO ecommerce_integration;

    -- Grant privileges on schema
    \c ecommerce_integration
    GRANT ALL PRIVILEGES ON SCHEMA public TO ecommerce_integration;
EOSQL
