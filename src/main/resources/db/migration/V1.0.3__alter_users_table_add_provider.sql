ALTER TABLE app_users ADD COLUMN provider varchar(255) NOT NULL default 'LOCAL';
ALTER TABLE app_users ADD COLUMN provider_id varchar(255) UNIQUE;
ALTER TABLE app_users ALTER COLUMN password DROP NOT NULL;