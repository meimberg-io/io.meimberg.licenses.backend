-- Remove REVOKED status from assignments table
-- Convert any existing REVOKED assignments to be deleted (they will be removed)
-- Update ENUM to only include ACTIVE

-- First, delete any REVOKED assignments
DELETE FROM assignments WHERE status = 'REVOKED';

-- Modify the ENUM to remove REVOKED
ALTER TABLE assignments MODIFY COLUMN status ENUM('ACTIVE') NOT NULL DEFAULT 'ACTIVE';

