CREATE TABLE IF NOT EXISTS rents (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    rent_date DATE,
    return_date DATE,
    total_cost FLOAT,
    CONSTRAINT fk_rent_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles (id) ON DELETE RESTRICT,
    CONSTRAINT fk_rent_user FOREIGN KEY (user_id) REFERENCES app_users (id) ON DELETE CASCADE
);