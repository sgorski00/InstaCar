CREATE TABLE IF NOT EXISTS vehicles (
    id bigserial NOT NULL PRIMARY KEY,
    model_id bigint NOT NULL,
    license_plate varchar(255) NOT NULL,
    vin varchar(255) NOT NULL,
    production_year int,
    engine varchar(50),
    mileage int,
    color varchar(50),
    rent_status varchar(50),
    price float,
    image_url varchar(255),
    vehicle_type varchar(50) NOT NULL,
    CONSTRAINT fk_vehicle_model FOREIGN KEY (model_id) REFERENCES car_models (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS city_cars (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    trunk_capacity FLOAT,
    CONSTRAINT fk_city_car_vehicle FOREIGN KEY (id) REFERENCES vehicles (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sport_cars (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    horse_power INT,
    acceleration FLOAT,
    top_speed INT,
    CONSTRAINT fk_sport_car_vehicle FOREIGN KEY (id) REFERENCES vehicles (id) ON DELETE CASCADE
);