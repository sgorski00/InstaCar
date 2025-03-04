CREATE TABLE IF NOT EXISTS email_tokens (
    id bigserial NOT NULL PRIMARY KEY,
    user_id bigint NOT NULL,
    token varchar(255) NOT NULL UNIQUE,
    verified bool NOT NULL,
    CONSTRAINT fk_user_token FOREIGN KEY (user_id) REFERENCES app_users(id) ON DELETE CASCADE
);