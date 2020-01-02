/******************************************************
Oracle version of the DDL
******************************************************/

CREATE TABLE Booking (
  id INTEGER NOT NULL,
  date_made DATE,
  reserved_until DATE,
  price DECIMAL(15, 2) NOT NULL,
  seat_count INTEGER NOT NULL,
  Purchase_id INTEGER,
  PRIMARY KEY(id));

CREATE TABLE Genre (
  id INTEGER NOT NULL,
  name VARCHAR(50),
  PRIMARY KEY(id));

CREATE TABLE Performance (
  id INTEGER NOT NULL,
  date_and_time DATE,
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
  row_or_box NUMBER,
  row_pos NUMBER,
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

CREATE TABLE Show (
  id INTEGER NOT NULL,
  name VARCHAR(50),
  Genre_id INTEGER,
  Seating_Plan_id INTEGER,
  PRIMARY KEY(id));

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
  REFERENCES Show (id);

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

ALTER TABLE Show
  ADD CONSTRAINT Genre
  FOREIGN KEY (Genre_id)
  REFERENCES Genre (id);

ALTER TABLE Show
  ADD CONSTRAINT Seating_Plan2
  FOREIGN KEY (Seating_Plan_id)
  REFERENCES Seating_Plan (id);
