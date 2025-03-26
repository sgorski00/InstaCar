CREATE TABLE IF NOT EXISTS car_models(
    id bigserial NOT NULL PRIMARY KEY,
    brand varchar(255) NOT NULL,
    model_name varchar(255) NOT NULL,
    car_type varchar(255) NOT NULL,
    seats int,
    doors int,
    fuel_type varchar(255),
    transmission varchar(255)
);