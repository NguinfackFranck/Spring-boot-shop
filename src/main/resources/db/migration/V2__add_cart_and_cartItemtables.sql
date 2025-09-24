CREATE TABLE cart (
                      id BINARY (16) PRIMARY KEY DEFAULT (uuid_to_bin(uuid())), -- Generates a new UUID on insert if none is provided
                      date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL -- Better to use TIMESTAMP for full date+time
);
CREATE TABLE cart_item (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           cart_id BINARY(16) NOT NULL,
                           product_id BIGINT NOT NULL,
                           quantity INT NOT NULL DEFAULT 1,

    -- Define foreign key constraints
                           CONSTRAINT fk_cart_item_cart
                               FOREIGN KEY (cart_id)
                                   REFERENCES cart(id)
                                   ON DELETE CASCADE, -- If a cart is deleted, all its items are deleted too

                           CONSTRAINT fk_cart_item_product
                               FOREIGN KEY (product_id)
                                   REFERENCES products(id)
                                   ON DELETE CASCADE, -- If a product is deleted, all cart items for it are deleted

    -- Prevent the same product from being added multiple times to the same cart
                           CONSTRAINT uc_cart_product UNIQUE (cart_id, product_id)
);