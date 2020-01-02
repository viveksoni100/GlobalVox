-- loads data into the boxoffice app database (normally called 'spring')
-- for MySQL 4.1 or later

#
# deleting the current data
#
delete from Seat_Status;
delete from Seat_Plan_Seat;
delete from Booking;
delete from Price_Band;
delete from Performance;
delete from Price_Structure;
delete from Shows;
delete from Genre;
delete from Seating_Plan;
delete from Seat_Class;
update Seat set Left_Seat_id = null, Right_Seat_id = null;
delete from Seat;

#
# Inserting genres
#
insert into genre (id, name) values (1, 'Rock');
insert into genre (id, name) values (2, 'Ballet');
insert into genre (id, name) values (3, 'Theatre');
insert into genre (id, name) values (4, 'Classical');
insert into genre (id, name) values (5, 'Musical');
insert into genre (id, name) values (6, 'Opera');

#
# Inserting seating plans
#
insert into seating_plan (id, name) values (1, 'Standard Opera Seating');

#
# Inserting shows
#
insert into shows (id, name, Genre_id, Seating_plan_id) values (1, 'Romeo and Juliet', 3, 1);
insert into shows (id, name, Genre_id, Seating_plan_id) values (2, 'Waiting for Godot', 3, 1);
insert into shows (id, name, Genre_id, Seating_plan_id) values (3, 'Giselle', 2, 1);
insert into shows (id, name, Genre_id, Seating_plan_id) values (4, 'Tristan und Isolde', 6, 1);
insert into shows (id, name, Genre_id, Seating_plan_id) values (5, 'La Traviata', 6, 1);
insert into shows (id, name, Genre_id, Seating_plan_id) values (6, 'Miss Saigon', 5, 1);
insert into shows (id, name, Genre_id, Seating_plan_id) values (7, 'Cats', 5, 1);

#
# Inserting seat classes
#
insert into seat_class (id, code, description) values (1, 'AA', 'Premium Reserve');
insert into seat_class (id, code, description) values (2, 'A', 'A Reserve');

#
# Inserting price structures
#
insert into price_structure (id, name) values (1, 'Standard opera pricing');

#
# Inserting price bands
#
insert into price_band (id, Price_Structure_id, Seat_Class_id, price) values (1, 1, 1, 195);
insert into price_band (id, Price_Structure_id, Seat_Class_id, price) values (2, 1, 2, 160);

#
# Inserting performances
#
insert into performance (id, date_and_time, Show_id, Price_Structure_id) values (1, '2005-10-12 19:30:00', 1, 1);
insert into performance (id, date_and_time, Show_id, Price_Structure_id) values (2, '2005-09-14 19:30:00', 1, 1);

#
# Inserting seats
#
insert into seat (id, name) values (1, 'A1');
insert into seat (id, name) values (2, 'A2');
insert into seat (id, name) values (3, 'A3');
insert into seat (id, name) values (4, 'A4');
update seat set right_seat_id = 2 where id = 1;
update seat set left_seat_id = 1 where id = 2;
update seat set right_seat_id = 3 where id = 2;
update seat set left_seat_id = 2 where id = 3;
update seat set right_seat_id = 4 where id = 3;
update seat set left_seat_id = 3 where id = 4;

insert into seat (id, name) values (5, 'B1');
insert into seat (id, name) values (6, 'B2');
insert into seat (id, name) values (7, 'B3');
insert into seat (id, name) values (8, 'B4');
update seat set right_seat_id = 6 where id = 5;
update seat set left_seat_id = 5 where id = 6;
update seat set right_seat_id = 7 where id = 6;
update seat set left_seat_id = 6 where id = 7;
update seat set right_seat_id = 8 where id = 7;
update seat set left_seat_id = 7 where id = 8;

insert into seat (id, name) values (9, 'C1');
insert into seat (id, name) values (10, 'C2');
update seat set right_seat_id = 10 where id = 9;
update seat set left_seat_id = 9 where id = 10;

#
# Inserting seating plans to seats map
#
insert into seat_plan_seat (Seat_id, Seating_Plan_id, Seat_Class_id) values (1, 1, 1);
insert into seat_plan_seat (Seat_id, Seating_Plan_id, Seat_Class_id) values (2, 1, 1);
insert into seat_plan_seat (Seat_id, Seating_Plan_id, Seat_Class_id) values (3, 1, 2);
insert into seat_plan_seat (Seat_id, Seating_Plan_id, Seat_Class_id) values (4, 1, 2);
insert into seat_plan_seat (Seat_id, Seating_Plan_id, Seat_Class_id) values (5, 1, 1);
insert into seat_plan_seat (Seat_id, Seating_Plan_id, Seat_Class_id) values (6, 1, 1);
insert into seat_plan_seat (Seat_id, Seating_Plan_id, Seat_Class_id) values (7, 1, 2);
insert into seat_plan_seat (Seat_id, Seating_Plan_id, Seat_Class_id) values (8, 1, 2);
insert into seat_plan_seat (Seat_id, Seating_Plan_id, Seat_Class_id) values (9, 1, 1);
insert into seat_plan_seat (Seat_id, Seating_Plan_id, Seat_Class_id) values (10, 1, 1);

#
# Inserting the seat status
#
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (1, 1, 1, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (2, 1, 1, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (3, 2, 1, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (4, 2, 1, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (5, 1, 1, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (6, 1, 1, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (7, 2, 1, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (8, 2, 1, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (9, 1, 1, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (10, 1, 1, null);

insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (1, 1, 2, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (2, 1, 2, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (3, 2, 2, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (4, 2, 2, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (5, 1, 2, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (6, 1, 2, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (7, 2, 2, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (8, 2, 2, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (9, 1, 2, null);
insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (10, 1, 2, null);