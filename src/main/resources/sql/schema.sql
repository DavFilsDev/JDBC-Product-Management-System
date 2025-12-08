-- Create Product table
create table if not exists product (
        id serial primary key,
        name varchar(255) not null,
        price numeric(10,2) not null check (price >= 0),
        creation_datetime timestamp default current_timestamp
    );

-- Create Product_category table
create table IF not exists product_category (
        id serial primary key,
        name varchar(255) not null,
        product_id int not null,
        constraint fk_product foreign key (product_id) references product(id) on delete cascade
    );