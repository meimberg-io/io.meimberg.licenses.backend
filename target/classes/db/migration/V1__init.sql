-- Users
CREATE TABLE IF NOT EXISTS users (
  id CHAR(36) NOT NULL,
  email VARCHAR(320) NOT NULL,
  display_name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  version INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_users_email (email)
);

-- Products
CREATE TABLE IF NOT EXISTS products (
  id CHAR(36) NOT NULL,
  `key` VARCHAR(100) NOT NULL,
  name VARCHAR(255) NOT NULL,
  description TEXT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  version INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_products_key (`key`)
);

-- Product Variants
CREATE TABLE IF NOT EXISTS product_variants (
  id CHAR(36) NOT NULL,
  product_id CHAR(36) NOT NULL,
  `key` VARCHAR(100) NOT NULL,
  name VARCHAR(255) NOT NULL,
  capacity INT NULL,
  attributes JSON NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  version INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  CONSTRAINT fk_variant_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE RESTRICT,
  UNIQUE KEY uk_variant_product_key (product_id, `key`),
  KEY idx_variant_product (product_id)
);

-- Assignments
CREATE TABLE IF NOT EXISTS assignments (
  id CHAR(36) NOT NULL,
  user_id CHAR(36) NOT NULL,
  product_variant_id CHAR(36) NOT NULL,
  status ENUM('ACTIVE','REVOKED') NOT NULL DEFAULT 'ACTIVE',
  starts_at TIMESTAMP NULL,
  ends_at TIMESTAMP NULL,
  note VARCHAR(1000) NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  version INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  CONSTRAINT fk_assignment_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT,
  CONSTRAINT fk_assignment_variant FOREIGN KEY (product_variant_id) REFERENCES product_variants (id) ON DELETE RESTRICT,
  KEY idx_assignment_user (user_id),
  KEY idx_assignment_variant (product_variant_id),
  KEY idx_assignment_status (status)
);


