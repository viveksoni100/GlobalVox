#
# TICKET DATABASE for Oracle 9i or later
#

/* un-comment when re-creating the database
DROP TABLE Seat_Plan_Seat CASCADE CONSTRAINTS;
DROP TABLE Seat_Status CASCADE CONSTRAINTS;
DROP TABLE Seat CASCADE CONSTRAINTS;
DROP TABLE Performance CASCADE CONSTRAINTS;
DROP TABLE Booking CASCADE CONSTRAINTS;
DROP TABLE Shows CASCADE CONSTRAINTS;
DROP TABLE Seating_Plan CASCADE CONSTRAINTS;
DROP TABLE Genre CASCADE CONSTRAINTS;
DROP TABLE Price_Band CASCADE CONSTRAINTS;
DROP TABLE Price_Structure CASCADE CONSTRAINTS;
DROP TABLE Purchase CASCADE CONSTRAINTS;
DROP TABLE Registered_User CASCADE CONSTRAINTS;
DROP TABLE Seat_Class CASCADE CONSTRAINTS;
*/

CREATE TABLE Booking (
  id INTEGER NOT NULL,
  date_made DATE,
  reserved_until TIMESTAMP,
  price DECIMAL(15, 2) NOT NULL,
  Purchase_id INTEGER,
  PRIMARY KEY(id));

CREATE TABLE Genre (
  id INTEGER NOT NULL,
  name VARCHAR(50),
  PRIMARY KEY(id));

CREATE TABLE Performance (
  id INTEGER NOT NULL,
  date_and_time TIMESTAMP,
  Price_Structure_id INTEGER,
  Show_id INTEGER,
  PRIMARY KEY(id));

CREATE TABLE Price_Band (
  id INTEGER NOT NULL,
  price DECIMAL(15, 2),
  Price_Structure_id INTEGER,
  Seat_Class_id INTEGER,
  PRIMARY KEY(id));

CREATE TABLE Price_Structure (
  id INTEGER NOT NULL,
  name VARCHAR(80),
  PRIMARY KEY(id));

CREATE TABLE Purchase (
  id INTEGER NOT NULL,
  authorization_code VARCHAR(32) NOT NULL,
  purchase_date DATE NOT NULL,
  email VARCHAR(100) NOT NULL,
  card_street VARCHAR(40) NOT NULL,
  card_line2 VARCHAR(40),
  card_city VARCHAR(40) NOT NULL,
  card_postcode VARCHAR(10) NOT NULL,
  delivery_street VARCHAR(40),
  delivery_line2 VARCHAR(40),
  delivery_city VARCHAR(40),
  delivery_postcode VARCHAR(10),
  Registered_User_id INTEGER,
  PRIMARY KEY(id));

CREATE TABLE Registered_User (
  id INTEGER NOT NULL,
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
  PRIMARY KEY(id));

CREATE TABLE Seat (
  id INTEGER NOT NULL,
  name VARCHAR(50) NOT NULL,
  row_or_box NUMERIC,
  row_pos NUMERIC,
  left_Seat_id INTEGER,
  right_Seat_id INTEGER,
  PRIMARY KEY(id));

CREATE TABLE Seat_Class (
  id INTEGER NOT NULL,
  code VARCHAR(5),
  description VARCHAR(200),
  PRIMARY KEY(id));

CREATE TABLE Seat_Plan_Seat (
  Seat_id INTEGER NOT NULL,
  Seating_Plan_id INTEGER NOT NULL,
  Seat_Class_id INTEGER NOT NULL,
  PRIMARY KEY(Seat_Class_id, Seating_Plan_id, Seat_id));

CREATE TABLE Seat_Status (
  Performance_id INTEGER NOT NULL,
  Seat_id INTEGER NOT NULL,
  Booking_id INTEGER,
  Price_Band_id INTEGER,
  PRIMARY KEY(Seat_id, Performance_id));

CREATE TABLE Seating_Plan (
  id INTEGER NOT NULL,
  name VARCHAR(50),
  PRIMARY KEY(id));

CREATE TABLE Shows (
  id INTEGER NOT NULL,
  name VARCHAR(50),
  Genre_id INTEGER,
  Seating_Plan_id INTEGER,
  PRIMARY KEY(id));

CREATE INDEX ix_Price_Structure
  ON Performance(Price_Structure_id);
ALTER TABLE Performance
  ADD CONSTRAINT fk_Price_Structure
  FOREIGN KEY (Price_Structure_id)
  REFERENCES Price_Structure (id);

CREATE INDEX ix_Show
  ON Performance(Show_id);
ALTER TABLE Performance
  ADD CONSTRAINT fk_Shows
  FOREIGN KEY (Show_id)
  REFERENCES Shows(id);

CREATE INDEX ix_Price_Structure2
  ON Price_Band(Price_Structure_id);
ALTER TABLE Price_Band
  ADD CONSTRAINT fk_Price_Structure2
  FOREIGN KEY (Price_Structure_id)
  REFERENCES Price_Structure (id);

CREATE INDEX ix_Seat_Class
  ON Price_Band(Seat_Class_id);
ALTER TABLE Price_Band
  ADD CONSTRAINT fk_Seat_Class
  FOREIGN KEY (Seat_Class_id)
  REFERENCES Seat_Class (id);

CREATE INDEX ix_Registered_User
  ON Purchase(Registered_User_id);
ALTER TABLE Purchase
  ADD CONSTRAINT fk_Registered_User
  FOREIGN KEY (Registered_User_id)
  REFERENCES Registered_User (id);

CREATE INDEX ix_left_Seat
  ON Seat(left_Seat_id);
ALTER TABLE Seat
  ADD CONSTRAINT fk_left_Seat
  FOREIGN KEY (left_Seat_id)
  REFERENCES Seat (id);

CREATE INDEX ix_right_Seat
  ON Seat(right_Seat_id);
ALTER TABLE Seat
  ADD CONSTRAINT fk_right_Seat
  FOREIGN KEY (right_Seat_id)
  REFERENCES Seat (id);

CREATE INDEX ix_Seat
  ON Seat_Plan_Seat(Seat_id);
ALTER TABLE Seat_Plan_Seat
  ADD CONSTRAINT fk_Seat
  FOREIGN KEY (Seat_id)
  REFERENCES Seat (id)
  ON DELETE CASCADE;

CREATE INDEX ix_Seat_Class2
  ON Seat_Plan_Seat(Seat_Class_id);
ALTER TABLE Seat_Plan_Seat
  ADD CONSTRAINT fk_Seat_Class2
  FOREIGN KEY (Seat_Class_id)
  REFERENCES Seat_Class (id)
  ON DELETE CASCADE;

CREATE INDEX ix_Seating_Plan
  ON Seat_Plan_Seat(Seating_Plan_id);
ALTER TABLE Seat_Plan_Seat
  ADD CONSTRAINT fk_Seating_Plan
  FOREIGN KEY (Seating_Plan_id)
  REFERENCES Seating_Plan (id)
  ON DELETE CASCADE;

CREATE INDEX ix_Booking
  ON Seat_Status(Booking_id);
ALTER TABLE Seat_Status
  ADD CONSTRAINT fk_Booking
  FOREIGN KEY (Booking_id)
  REFERENCES Booking (id);

CREATE INDEX ix_Performance
  ON Seat_Status(Performance_id);
ALTER TABLE Seat_Status
  ADD CONSTRAINT fk_Performance
  FOREIGN KEY (Performance_id)
  REFERENCES Performance (id)
  ON DELETE CASCADE;

CREATE INDEX ix_Price_Band
  ON Seat_Status(Price_Band_id);
ALTER TABLE Seat_Status
  ADD CONSTRAINT fk_Price_Band
  FOREIGN KEY (Price_Band_id)
  REFERENCES Price_Band (id);

CREATE INDEX ix_Seat2
  ON Seat_Status(Seat_id);
ALTER TABLE Seat_Status
  ADD CONSTRAINT fk_Seat2
  FOREIGN KEY (Seat_id)
  REFERENCES Seat (id)
  ON DELETE CASCADE;

CREATE INDEX ix_Genre
  ON Shows(Genre_id);
ALTER TABLE Shows
  ADD CONSTRAINT fk_Genre
  FOREIGN KEY (Genre_id)
  REFERENCES Genre (id);

CREATE INDEX ix_Seating_Plan2
  ON Shows(Seating_Plan_id);
ALTER TABLE Shows
  ADD CONSTRAINT fk_Seating_Plan2
  FOREIGN KEY (Seating_Plan_id)
  REFERENCES Seating_Plan (id);

CREATE INDEX ix_Purchase
  ON Booking(Purchase_id);
ALTER TABLE Booking
  ADD CONSTRAINT fk_Purchase
  FOREIGN KEY (Purchase_id)
  REFERENCES Purchase (id);

