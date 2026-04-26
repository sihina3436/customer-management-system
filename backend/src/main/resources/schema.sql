

CREATE TABLE country (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    iso_code VARCHAR(10),
    UNIQUE KEY uq_country_name (name)
);

CREATE TABLE city (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    country_id BIGINT NOT NULL,
    FOREIGN KEY (country_id) REFERENCES country(id),
    UNIQUE KEY uq_city_country (name, country_id)
);

CREATE TABLE customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    nic VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_customer_nic (nic)
);

CREATE TABLE phone_number (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    phone VARCHAR(50),
    CONSTRAINT fk_phone_customer FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
);

CREATE TABLE address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city_id BIGINT,
    country_id BIGINT,
    CONSTRAINT fk_address_customer FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
    CONSTRAINT fk_address_city FOREIGN KEY (city_id) REFERENCES city(id),
    CONSTRAINT fk_address_country FOREIGN KEY (country_id) REFERENCES country(id)
);


CREATE TABLE family_relation (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   customer_id BIGINT NOT NULL,
   family_member_id BIGINT NOT NULL,
   relation_type VARCHAR(100),
   CONSTRAINT fk_family_customer FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
   CONSTRAINT fk_family_member FOREIGN KEY (family_member_id) REFERENCES customer(id) ON DELETE CASCADE,
   UNIQUE KEY uq_family_relation (customer_id, family_member_id)
);