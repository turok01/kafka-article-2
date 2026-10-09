CREATE TABLE IF NOT EXISTS inventory (
    id BIGSERIAL PRIMARY KEY,
    product_name VARCHAR(256) NOT NULL UNIQUE,
    quantity INT NOT NULL CHECK (quantity >= 0)
);