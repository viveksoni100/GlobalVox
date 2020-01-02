-- creates the table structure in the boxoffice database (normally called 'spring')
-- tested with PostgreSQL 8.0 or later

CREATE TABLE Booking (
    id SERIAL,
    date_made TIMESTAMP,
    reserved_until TIMESTAMP,
    price DECIMAL(15, 2) NOT NULL,
    seat_count INTEGER NOT NULL,
    Purchase_id INTEGER,
    PRIMARY KEY(id)
);

CREATE TABLE Genre (
    id SERIAL,
    name VARCHAR(50),
    PRIMARY KEY(id)
);

CREATE TABLE Performance (
    id SERIAL,
    date_and_time TIMESTAMP,
    Price_Structure_id INTEGER,
    Show_id INTEGER,
    PRIMARY KEY(id)
);

CREATE TABLE Price_Band (
    id SERIAL,
    price DECIMAL(15, 2),
    Price_Structure_id INTEGER,
    Seat_Class_id INTEGER,
    PRIMARY KEY(id)
);

CREATE TABLE Price_Structure (
    id SERIAL,
    name VARCHAR(80),
    PRIMARY KEY(id)
);

CREATE TABLE Purchase (
    id SERIAL,
    authorization_code VARCHAR(32) NOT NULL,
    purchase_date TIMESTAMP NOT NULL,
    customer_name VARCHAR(55) NOT NULL,
    encrypted_card_number VARCHAR(19) NOT NULL,
    collected_by_customer BOOLEAN NOT NULL,
    email VARCHAR(100) NOT NULL,
    card_country VARCHAR(40),
    card_city VARCHAR(40) NOT NULL,
    card_street VARCHAR(40) NOT NULL,
    card_postcode VARCHAR(10) NOT NULL,
    delivery_country VARCHAR(40),
    delivery_city VARCHAR(40),
    delivery_street VARCHAR(40),
    delivery_postcode VARCHAR(10),
    Registered_User_id INTEGER,
    PRIMARY KEY(id)
);

CREATE TABLE Registered_User (
    id SERIAL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(40) NOT NULL,
    card_street VARCHAR(40) NOT NULL,
    card_line2 VARCHAR(40),
    card_city VARCHAR(40) NOT NULL,
    card_postcode VARCHAR(10) NOT NULL,
    delivery_street VARCHAR(40),
    delivery_line2 VARCHAR(40),
    delivery_city VARCHAR(40),
    delivery_postcode VARCHAR(10),
    PRIMARY KEY(id)
);

CREATE TABLE Seat (
    id SERIAL,
    name VARCHAR(50) NOT NULL,
    left_Seat_id INTEGER,
    right_Seat_id INTEGER,
    PRIMARY KEY(id)
);


CREATE TABLE Seat_Class (
    id SERIAL,
    code VARCHAR(5),
    description VARCHAR(200),
    PRIMARY KEY(id)
);

CREATE TABLE Seat_Plan_Seat (
    Seat_id INTEGER NOT NULL,
    Seating_Plan_id INTEGER NOT NULL,
    Seat_Class_id INTEGER NOT NULL,
    PRIMARY KEY(Seat_Class_id, Seating_Plan_id, Seat_id)
);

CREATE TABLE Seat_Status (
    Performance_id INTEGER NOT NULL,
    Seat_id INTEGER NOT NULL,
    Booking_id INTEGER,
    Price_Band_id INTEGER,
    PRIMARY KEY(Seat_id, Performance_id)
);

CREATE TABLE Seating_Plan (
    id SERIAL,
    name VARCHAR(50),
    PRIMARY KEY(id)
);

CREATE TABLE Shows (
    id SERIAL,
    name VARCHAR(50),
    Genre_id INTEGER,
    Seating_Plan_id INTEGER,
    PRIMARY KEY(id)
);

ALTER TABLE Booking
    ADD CONSTRAINT Purchase
    FOREIGN KEY (Purchase_id)
    REFERENCES Purchase (id)
    ON DELETE CASCADE;

ALTER TABLE Performance
    ADD CONSTRAINT Price_Structure
    FOREIGN KEY (Price_Structure_id)
    REFERENCES Price_Structure (id);

ALTER TABLE Performance
    ADD CONSTRAINT Show
    FOREIGN KEY (Show_id)
    REFERENCES Shows (id);

ALTER TABLE Price_Band
    ADD CONSTRAINT Price_Structure2
    FOREIGN KEY (Price_Structure_id)
    REFERENCES Price_Structure (id);

ALTER TABLE Price_Band
    ADD CONSTRAINT Seat_Class
    FOREIGN KEY (Seat_Class_id)
    REFERENCES Seat_Class (id);

ALTER TABLE Purchase
    ADD CONSTRAINT Registered_User
    FOREIGN KEY (Registered_User_id)
    REFERENCES Registered_User (id);

ALTER TABLE Seat
    ADD CONSTRAINT left_Seat
    FOREIGN KEY (left_Seat_id)
    REFERENCES Seat (id);

ALTER TABLE Seat
    ADD CONSTRAINT right_Seat
    FOREIGN KEY (right_Seat_id)
    REFERENCES Seat (id);

ALTER TABLE Seat_Plan_Seat
    ADD CONSTRAINT Seat
    FOREIGN KEY (Seat_id)
    REFERENCES Seat (id)
    ON DELETE CASCADE;

ALTER TABLE Seat_Plan_Seat
    ADD CONSTRAINT Seat_Class2
    FOREIGN KEY (Seat_Class_id)
    REFERENCES Seat_Class (id)
    ON DELETE CASCADE;

ALTER TABLE Seat_Plan_Seat
    ADD CONSTRAINT Seating_Plan
    FOREIGN KEY (Seating_Plan_id)
    REFERENCES Seating_Plan (id)
    ON DELETE CASCADE;

ALTER TABLE Seat_Status
    ADD CONSTRAINT Booking
    FOREIGN KEY (Booking_id)
    REFERENCES Booking (id);

ALTER TABLE Seat_Status
    ADD CONSTRAINT Performance
    FOREIGN KEY (Performance_id)
    REFERENCES Performance (id)
    ON DELETE CASCADE;

ALTER TABLE Seat_Status
    ADD CONSTRAINT Price_Band
    FOREIGN KEY (Price_Band_id)
    REFERENCES Price_Band (id);

ALTER TABLE Seat_Status
    ADD CONSTRAINT Seat2
    FOREIGN KEY (Seat_id)
    REFERENCES Seat (id)
    ON DELETE CASCADE;

ALTER TABLE Shows
    ADD CONSTRAINT Genre
    FOREIGN KEY (Genre_id)
    REFERENCES Genre (id);

ALTER TABLE Shows
    ADD CONSTRAINT Seating_Plan2
    FOREIGN KEY (Seating_Plan_id)
    REFERENCES Seating_Plan (id);

GRANT EXECUTE ON FUNCTION database_size(name) TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_database_size(oid) TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_dir_ls(text, bool) TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_file_length(text) TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_file_read(text, int8, int8) TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_file_rename(text, text, text) TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_file_rename(text, text) TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_file_stat(text) TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_file_unlink(text) TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_file_write(text, text, bool) TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_logdir_ls() TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_postmaster_starttime() TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_relation_size(oid) TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_reload_conf() TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_size_pretty(int8) TO GROUP spring;
GRANT EXECUTE ON FUNCTION pg_tablespace_size(oid) TO GROUP spring;
GRANT EXECUTE ON FUNCTION plpgsql_call_handler() TO GROUP spring;
GRANT EXECUTE ON FUNCTION plpgsql_validator(oid) TO GROUP spring;
GRANT EXECUTE ON FUNCTION relation_size(text) TO GROUP spring;
GRANT ALL ON TABLE booking_id_seq TO GROUP spring;
GRANT ALL ON TABLE genre_id_seq TO GROUP spring;
GRANT ALL ON TABLE performance_id_seq TO GROUP spring;
GRANT ALL ON TABLE price_band_id_seq TO GROUP spring;
GRANT ALL ON TABLE price_structure_id_seq TO GROUP spring;
GRANT ALL ON TABLE purchase_id_seq TO GROUP spring;
GRANT ALL ON TABLE registered_user_id_seq TO GROUP spring;
GRANT ALL ON TABLE seat_class_id_seq TO GROUP spring;
GRANT ALL ON TABLE seat_id_seq TO GROUP spring;
GRANT ALL ON TABLE seating_plan_id_seq TO GROUP spring;
GRANT ALL ON TABLE shows_id_seq TO GROUP spring;
GRANT ALL ON TABLE booking TO GROUP spring;
GRANT ALL ON TABLE genre TO GROUP spring;
GRANT ALL ON TABLE performance TO GROUP spring;
GRANT ALL ON TABLE price_band TO GROUP spring;
GRANT ALL ON TABLE price_structure TO GROUP spring;
GRANT ALL ON TABLE purchase TO GROUP spring;
GRANT ALL ON TABLE registered_user TO GROUP spring;
GRANT ALL ON TABLE seat TO GROUP spring;
GRANT ALL ON TABLE seat_class TO GROUP spring;
GRANT ALL ON TABLE seat_plan_seat TO GROUP spring;
GRANT ALL ON TABLE seat_status TO GROUP spring;
GRANT ALL ON TABLE seating_plan TO GROUP spring;
GRANT ALL ON TABLE shows TO GROUP spring;
GRANT ALL ON TABLE pg_logdir_ls TO GROUP spring;
