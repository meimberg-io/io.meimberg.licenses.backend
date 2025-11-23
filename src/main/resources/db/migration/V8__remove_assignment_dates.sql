-- Remove starts_at and ends_at columns from assignments
ALTER TABLE assignments
DROP COLUMN starts_at,
DROP COLUMN ends_at;

