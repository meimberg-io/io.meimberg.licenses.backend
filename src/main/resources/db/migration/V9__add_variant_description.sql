-- Add description column to product_variants
ALTER TABLE product_variants
ADD COLUMN description TEXT NULL AFTER name;

