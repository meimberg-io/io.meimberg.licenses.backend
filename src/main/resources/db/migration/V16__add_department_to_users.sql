-- Add department_id column to users table
ALTER TABLE users ADD COLUMN department_id CHAR(36) NULL;

-- Create foreign key constraint
ALTER TABLE users ADD CONSTRAINT fk_user_department FOREIGN KEY (department_id) REFERENCES departments (id) ON DELETE RESTRICT;

-- Create index for department_id
CREATE INDEX idx_user_department ON users (department_id);

-- Note: department_id is initially nullable to allow migration of existing data
-- After migrating existing users, you may want to make it NOT NULL

