CREATE TABLE products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    article VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    category VARCHAR(255),
    price NUMERIC(19,2) NOT NULL,
    quantity INTEGER NOT NULL,
    quantity_updated_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);