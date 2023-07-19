CREATE TABLE carts(
    id SERIAL PRIMARY KEY NOT NULL,
    total_items INTEGER NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

CREATE TABLE items(
     id SERIAL PRIMARY KEY NOT NULL,
     name VARCHAR(255) NOT NULL,
     quantity INTEGER NOT NULL,
     price DOUBLE PRECISION NOT NULL,
     cart_id INTEGER,
        CONSTRAINT fk_id_cart FOREIGN KEY (cart_id)
        REFERENCES carts(id)
        ON DELETE CASCADE
);