-- Remove capacity and attributes columns from product_variants
ALTER TABLE product_variants
DROP COLUMN capacity,
DROP COLUMN attributes;

