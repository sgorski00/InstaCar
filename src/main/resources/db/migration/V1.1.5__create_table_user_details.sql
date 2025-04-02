CREATE TABLE IF NOT EXISTS user_details (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone_number VARCHAR(50),
    address VARCHAR(255),
    city VARCHAR(50),
    postal_code VARCHAR(10),
    CONSTRAINT fk_user_details_user FOREIGN KEY (user_id) REFERENCES app_users (id) ON DELETE CASCADE
);