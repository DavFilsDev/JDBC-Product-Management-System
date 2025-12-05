-- 1. Create the database
create DATABASE product_management_db;

-- 2. Connect to the new database (Note: This might need to be executed separately)
\c product_management_db;

-- 3. Create a new user for product management
CREATE USER product_manager_user WITH PASSWORD '123456';

-- 4. Grant privileges to the user
GRANT CONNECT ON DATABASE product_management_db TO product_manager_user;
GRANT CREATE ON DATABASE product_management_db TO product_manager_user;

-- 5. Switch to product_management_db context and create tables
-- (Note: In practice, you might need to reconnect to product_management_db)

-- Create Product table
CREATE TABLE IF NOT EXISTS product (
                                       id SERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    creation_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Create Product_category table
CREATE TABLE IF NOT EXISTS product_category (
                                                id SERIAL PRIMARY KEY,
                                                name VARCHAR(255) NOT NULL,
    product_id INT NOT NULL,
    CONSTRAINT fk_product
    FOREIGN KEY (product_id)
    REFERENCES product(id)
    ON DELETE CASCADE
    );

-- 6. Grant all privileges on tables to the user
GRANT ALL PRIVILEGES ON DATABASE product_management_db TO product_manager_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO product_manager_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO product_manager_user;
GRANT USAGE ON SCHEMA public TO product_manager_user;

-- 7. Grant future privileges (for PostgreSQL)
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT ALL ON TABLES TO product_manager_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT ALL ON SEQUENCES TO product_manager_user;

-- 8. Insert sample data for testing
INSERT INTO product (name, price) VALUES
                                      ('Laptop Dell XPS 13', 1299.99),
                                      ('iPhone 15 Pro', 1099.99),
                                      ('Samsung Galaxy S24', 899.99),
                                      ('Sony WH-1000XM5 Headphones', 349.99),
                                      ('Logitech MX Master 3S', 99.99),
                                      ('Apple MacBook Pro 16"', 2399.99),
                                      ('Google Pixel 8 Pro', 999.99),
                                      ('Microsoft Surface Laptop 5', 1199.99),
                                      ('Bose QuietComfort 45', 329.99),
                                      ('iPad Air 5th Gen', 749.99);

INSERT INTO product_category (name, product_id) VALUES
                                                    ('Electronics', 1),
                                                    ('Computers', 1),
                                                    ('Electronics', 2),
                                                    ('Mobile Phones', 2),
                                                    ('Electronics', 3),
                                                    ('Mobile Phones', 3),
                                                    ('Electronics', 4),
                                                    ('Audio', 4),
                                                    ('Electronics', 5),
                                                    ('Accessories', 5),
                                                    ('Electronics', 6),
                                                    ('Computers', 6),
                                                    ('Electronics', 7),
                                                    ('Mobile Phones', 7),
                                                    ('Electronics', 8),
                                                    ('Computers', 8),
                                                    ('Electronics', 9),
                                                    ('Audio', 9),
                                                    ('Electronics', 10),
                                                    ('Tablets', 10);