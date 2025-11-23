-- Seed Products
INSERT INTO products (id, `key`, name, description)
VALUES
  (UUID(), 'ACME_SUITE', 'ACME Suite', 'All-in-one ACME tools'),
  (UUID(), 'WIDGET_PRO', 'Widget Pro', 'Professional widget toolkit')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Capture product ids for deterministic variant seeding
-- Note: MySQL variables are session-scoped
SET @acme_id = (SELECT id FROM products WHERE `key` = 'ACME_SUITE' LIMIT 1);
SET @widget_id = (SELECT id FROM products WHERE `key` = 'WIDGET_PRO' LIMIT 1);

-- Seed Variants
INSERT INTO product_variants (id, product_id, `key`, name, capacity, attributes)
VALUES
  (UUID(), @acme_id, 'STD', 'Standard', 100, JSON_OBJECT('features', JSON_ARRAY('basic', 'support'))),
  (UUID(), @acme_id, 'ENT', 'Enterprise', NULL, JSON_OBJECT('features', JSON_ARRAY('sso', 'priority_support'))),
  (UUID(), @widget_id, 'BASIC', 'Basic', 50, NULL),
  (UUID(), @widget_id, 'ULTIMATE', 'Ultimate', NULL, JSON_OBJECT('notes', 'Unlimited seats'))
ON DUPLICATE KEY UPDATE name = VALUES(name), capacity = VALUES(capacity), attributes = VALUES(attributes);


