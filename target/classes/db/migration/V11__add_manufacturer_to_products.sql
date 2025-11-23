-- Add manufacturer_id foreign key to products
ALTER TABLE products
ADD COLUMN manufacturer_id CHAR(36) NULL AFTER description,
ADD CONSTRAINT fk_product_manufacturer FOREIGN KEY (manufacturer_id) REFERENCES manufacturers (id) ON DELETE SET NULL,
ADD KEY idx_product_manufacturer (manufacturer_id);

