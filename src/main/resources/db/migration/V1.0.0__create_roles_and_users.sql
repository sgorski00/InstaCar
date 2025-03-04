CREATE TABLE IF NOT EXISTS roles (
    id bigserial NOT NULL PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS app_users (
    id bigserial NOT NULL PRIMARY KEY,
    username varchar(255) NOT NULL UNIQUE,
    email varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    role_id bigint NOT NULL,
    CONSTRAINT FK_user_role FOREIGN KEY (role_id) REFERENCES roles(id)
);