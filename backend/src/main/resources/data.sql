
INSERT INTO country (name, iso_code) VALUES ('Sri Lanka','LKA') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO country (name, iso_code) VALUES ('United States','USA') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO country (name, iso_code) VALUES ('India','IND') ON DUPLICATE KEY UPDATE name=name;

INSERT INTO city (name, country_id) VALUES ('Colombo', (SELECT id FROM country WHERE name='Sri Lanka'))ON DUPLICATE KEY UPDATE name=name;
INSERT INTO city (name, country_id) VALUES ('Kandy', (SELECT id FROM country WHERE name='Sri Lanka'))ON DUPLICATE KEY UPDATE name=name;
INSERT INTO city (name, country_id) VALUES ('New York', (SELECT id FROM country WHERE name='United States'))ON DUPLICATE KEY UPDATE name=name;