ALTER TABLE vehicles ADD CONSTRAINT unique_vin UNIQUE(vin);
ALTER TABLE vehicles ADD CONSTRAINT unique_license_plate UNIQUE(license_plate);
ALTER TABLE vehicles ALTER COLUMN rent_status SET NOT NULL;
ALTER TABLE vehicles ADD CONSTRAINT check_price_positive CHECK (price > 0);