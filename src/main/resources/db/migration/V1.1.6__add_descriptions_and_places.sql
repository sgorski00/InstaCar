ALTER TABLE vehicles ADD COLUMN description text NOT NULL default 'No description yet.';

CREATE TABLE cities (
    id bigserial NOT NULL PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE
);

ALTER TABLE rents ADD COLUMN pickup_city_id bigint;
ALTER TABLE rents ADD COLUMN return_city_id bigint;

ALTER TABLE rents ADD CONSTRAINT fk_rent_pickup_city FOREIGN KEY (pickup_city_id) REFERENCES cities (id) ON DELETE RESTRICT;
ALTER TABLE rents ADD CONSTRAINT fk_rent_return_city FOREIGN KEY (return_city_id) REFERENCES cities (id) ON DELETE RESTRICT;