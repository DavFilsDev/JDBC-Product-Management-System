-- Create the database
create DATABASE product_management_db;

-- Connect to the new database
\c product_management_db;

-- Create a new user for product management
CREATE USER product_manager_user WITH PASSWORD '123456';

-- Grant privileges to the user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO product_manager_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO product_manager_user;