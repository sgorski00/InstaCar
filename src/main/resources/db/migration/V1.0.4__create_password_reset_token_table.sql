CREATE TABLE IF NOT EXISTS password_tokens (
    id bigserial NOT NULL PRIMARY KEY,
    user_id bigint NOT NULL,
    token varchar(255) NOT NULL UNIQUE,
    is_used bool NOT NULL,
    CONSTRAINT fk_user_pwd_token FOREIGN KEY (user_id) REFERENCES app_users(id) ON DELETE CASCADE
);