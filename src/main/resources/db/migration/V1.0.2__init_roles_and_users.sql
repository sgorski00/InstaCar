INSERT INTO roles(name) VALUES
('ADMIN'),
('USER');

INSERT INTO app_users(username, email, password, role_id) VALUES
('admin', 'admin@instacar.pl', '$2a$10$fBC2/IWjuKQW1g9fPVPdsumYY6TYzA2t9uu6FwzziSR..niBJWhEC', 1),
('jkowalski', 'kowalski@instacar.pl', '$2a$10$fBC2/IWjuKQW1g9fPVPdsumYY6TYzA2t9uu6FwzziSR..niBJWhEC', 2);

INSERT INTO email_tokens(user_id, token, verified) VALUES
(1, '4db62ba3-7a12-488f-8337-954287957d89', true),
(2, '4f468c7b-9905-4a40-80ac-48f1c2777703', true);