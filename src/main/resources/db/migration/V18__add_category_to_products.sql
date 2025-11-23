ALTER TABLE products
ADD COLUMN category_id CHAR(36) NULL;

ALTER TABLE products
ADD CONSTRAINT fk_product_category
FOREIGN KEY (category_id) REFERENCES product_categories (id) ON DELETE RESTRICT;

