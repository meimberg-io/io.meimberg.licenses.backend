INSERT INTO users (id, email, display_name)
VALUES
  (UUID(), 'alice@example.com', 'Alice Example'),
  (UUID(), 'bob@example.com', 'Bob Example'),
  (UUID(), 'carol@example.com', 'Carol Example')
ON DUPLICATE KEY UPDATE display_name = VALUES(display_name);


