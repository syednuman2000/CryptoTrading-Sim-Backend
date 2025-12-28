INSERT INTO user_login (id, username, password, role) VALUES (1, 'admin', 'password', 'ADMIN');
INSERT INTO user_login (id, username, password, role) VALUES (2, 'user', 'password', 'USER');

ALTER TABLE user_login ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM user_login);

INSERT INTO user_profile (id, user_login, email, full_name, phone_number) VALUES (1, 1, 'syed.numan@clayfin.com', 'Syed Numan', '9370873285');
INSERT INTO user_profile (id, user_login, email, full_name, phone_number) VALUES (2, 2, 'syednuman2000@gmail.com', 'Syed Numan', '9370873285');

ALTER TABLE user_profile ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM user_profile);