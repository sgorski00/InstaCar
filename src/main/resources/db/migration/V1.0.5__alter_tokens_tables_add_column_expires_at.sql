ALTER TABLE email_tokens ADD COLUMN expires_at timestamp NOT NULL default '2025-03-12 00:00:00';
ALTER TABLE password_tokens ADD COLUMN expires_at timestamp NOT NULL default '2025-03-12 00:00:00';