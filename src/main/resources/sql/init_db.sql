-- Create the database
create DATABASE product_management_db;

-- Connect to the new database
\c product_management_db;

-- Create a new user for product management
create user product_manager_user with PASSWORD '123456';

-- Grant privileges to the user
grant all privileges on all tables in schema public to product_manager_user;
alter default privileges in schema public grant all on TABLES TO product_manager_user;