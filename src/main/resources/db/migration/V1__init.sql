-- Create categories table
CREATE TABLE categories
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    version     BIGINT
);

-- Create products table
CREATE TABLE products
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL,
    description VARCHAR(255),
    price       DECIMAL(10, 2) NOT NULL,
    quantity    INT            NOT NULL,
    version     BIGINT,
    category_id BIGINT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id)
);

-- Insert initial categories
INSERT INTO categories (name, description)
VALUES ('Electronics', 'Devices and gadgets'),
       ('Books', 'Fiction and non-fiction'),
       ('Home & Kitchen', 'Appliances and decor'),
       ('Toys', 'Fun for kids and adults'),
       ('Clothing', 'Men and women clothing');

-- Insert initial products
INSERT INTO products (name, description, price, quantity, category_id)
VALUES ('Laptop', 'High-performance laptop', 1200.00, 10, 1),
       ('Smartphone', 'Latest model smartphone', 800.00, 20, 1),
       ('Novel', 'Bestselling novel', 15.99, 50, 2),
       ('Blender', 'Powerful kitchen blender', 89.99, 15, 3),
       ('Action Figure', 'Collectible action figure', 24.99, 30, 4),
       ('T-shirt', 'Cotton t-shirt', 12.99, 100, 5),
       ('Coffee Maker', 'Automatic drip coffee maker', 59.99, 25, 3),
       ('Board Game', 'Family board game', 39.99, 40, 4);


