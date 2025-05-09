ALTER TABLE rents ADD COLUMN additional_info text;
ALTER TABLE rents ADD COLUMN rent_status varchar(50) not null default 'finished';