/*****************************************************************
WARNING: Start label "left" in association relationship from Seat
         to Seat is treated as a comment.
******************************************************************/

CREATE TABLE Booking (
  id INTEGER NOT NULL,
  date_made DATE,
  reserved_until DATE,
  price DECIMAL(15, 2) NOT NULL,
  REF_Purchase_id INTEGER,
  PRIMARY KEY(id));

CREATE TABLE Genre (
  id INTEGER NOT NULL,
  name VARCHAR(50),
  PRIMARY KEY(id));

CREATE TABLE Performance (
  id INTEGER NOT NULL,
  date_and_time DATETIME,
  REF_Price_Structure_id INTEGER,
  REF_Show_id INTEGER,
  PRIMARY KEY(id));

CREATE TABLE Price_Band (
  id INTEGER NOT NULL,
  price DECIMAL(15, 2),
  REF_Price_Structure_id INTEGER,
  REF_Seat_Class_id INTEGER,
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
  REF_Registered_User_id INTEGER,
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
  name VARCHAR NOT NULL,
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
  REF_Seat_id INTEGER NOT NULL,
  REF_Seating_Plan_id INTEGER NOT NULL,
  REF_Seat_Class_id INTEGER NOT NULL,
  PRIMARY KEY(REF_Seat_Class_id, REF_Seating_Plan_id, REF_Seat_id));

CREATE TABLE Seat_Status (
  REF_Performance_id INTEGER NOT NULL,
  REF_Seat_id INTEGER NOT NULL,
  REF_Booking_id INTEGER,
  REF_Price_Band_id INTEGER,
  PRIMARY KEY(REF_Seat_id, REF_Performance_id));

CREATE TABLE Seating_Plan (
  id INTEGER NOT NULL,
  name VARCHAR(50),
  PRIMARY KEY(id));

CREATE TABLE Show (
  id INTEGER NOT NULL,
  name VARCHAR(50),
  REF_Genre_id INTEGER,
  REF_Seating_Plan_id INTEGER,
  PRIMARY KEY(id));

ALTER TABLE Booking
  ADD CONSTRAINT REF_Purchase
  FOREIGN KEY (REF_Purchase_id)
  REFERENCES Purchase (id)
  ON DELETE CASCADE;

ALTER TABLE Performance
  ADD CONSTRAINT REF_Price_Structure
  FOREIGN KEY (REF_Price_Structure_id)
  REFERENCES Price_Structure (id)

ALTER TABLE Performance
  ADD CONSTRAINT REF_Show
  FOREIGN KEY (REF_Show_id)
  REFERENCES Show (id)

ALTER TABLE Price_Band
  ADD CONSTRAINT REF_Price_Structure
  FOREIGN KEY (REF_Price_Structure_id)
  REFERENCES Price_Structure (id)

ALTER TABLE Price_Band
  ADD CONSTRAINT REF_Seat_Class
  FOREIGN KEY (REF_Seat_Class_id)
  REFERENCES Seat_Class (id)

ALTER TABLE Purchase
  ADD CONSTRAINT REF_Registered_User
  FOREIGN KEY (REF_Registered_User_id)
  REFERENCES Registered_User (id)

ALTER TABLE Seat
  ADD CONSTRAINT left_Seat
  FOREIGN KEY (left_Seat_id)
  REFERENCES Seat (id)

ALTER TABLE Seat
  ADD CONSTRAINT right_Seat
  FOREIGN KEY (right_Seat_id)
  REFERENCES Seat (id)

ALTER TABLE Seat_Plan_Seat
  ADD CONSTRAINT REF_Seat
  FOREIGN KEY (REF_Seat_id)
  REFERENCES Seat (id)
  ON DELETE CASCADE;

ALTER TABLE Seat_Plan_Seat
  ADD CONSTRAINT REF_Seat_Class
  FOREIGN KEY (REF_Seat_Class_id)
  REFERENCES Seat_Class (id)
  ON DELETE CASCADE;

ALTER TABLE Seat_Plan_Seat
  ADD CONSTRAINT REF_Seating_Plan
  FOREIGN KEY (REF_Seating_Plan_id)
  REFERENCES Seating_Plan (id)
  ON DELETE CASCADE;

ALTER TABLE Seat_Status
  ADD CONSTRAINT REF_Booking
  FOREIGN KEY (REF_Booking_id)
  REFERENCES Booking (id)

ALTER TABLE Seat_Status
  ADD CONSTRAINT REF_Performance
  FOREIGN KEY (REF_Performance_id)
  REFERENCES Performance (id)
  ON DELETE CASCADE;

ALTER TABLE Seat_Status
  ADD CONSTRAINT REF_Price_Band
  FOREIGN KEY (REF_Price_Band_id)
  REFERENCES Price_Band (id)

ALTER TABLE Seat_Status
  ADD CONSTRAINT REF_Seat
  FOREIGN KEY (REF_Seat_id)
  REFERENCES Seat (id)
  ON DELETE CASCADE;

ALTER TABLE Show
  ADD CONSTRAINT REF_Genre
  FOREIGN KEY (REF_Genre_id)
  REFERENCES Genre (id)

ALTER TABLE Show
  ADD CONSTRAINT REF_Seating_Plan
  FOREIGN KEY (REF_Seating_Plan_id)
  REFERENCES Seating_Plan (id)

