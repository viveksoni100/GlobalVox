/* un-comment when re-creating the sequences */
--DROP SEQUENCE Booking_seq;

CREATE SEQUENCE Booking_seq
INCREMENT BY 1
START WITH 10 ;

CREATE OR REPLACE PROCEDURE reserve_seat(in_performance_id in number,
             in_seat in number,
             in_price number,
             in_reserved_until date,
             out_new_booking_id out number)
is
begin
  -- Get a new pk for the booking table
  select booking_seq.nextval into out_new_booking_id from dual;

  -- Create a new booking
  insert into booking(id, date_made, price, reserved_until)
     values (out_new_booking_id, sysdate, in_price, in_reserved_until);

  update seat_status set Booking_id = out_new_booking_id
    where Seat_id = in_seat
    and in_performance_id = in_performance_id;

end;
/
show errors

CREATE OR REPLACE TYPE numbers AS TABLE OF NUMBER;
/

CREATE OR REPLACE PROCEDURE reserve_seats(in_performance_id in number,
            in_seats in numbers,
            in_price in number,
            in_reserved_until in date,
            out_new_booking_id out number)
is
begin
  -- Get a new pk for the booking table
  select booking_seq.nextval into out_new_booking_id from dual;

  -- Create a new booking
  insert into booking(id, date_made, price, reserved_until)
     values (out_new_booking_id, sysdate, in_price, in_reserved_until);

  for i in 1..in_seats.count loop
  insert into seat_status (Seat_id, Performance_id)
       values(in_seats(i), in_performance_id);
  update seat_status set Booking_id = out_new_booking_id
                where Seat_id = in_seats(i)
                and Performance_id = in_performance_id;
  end loop;
end;
/
show errors

CREATE OR REPLACE PROCEDURE get_genres (
  refcur OUT SYS_REFCURSOR,
  rundate OUT DATE)
IS
  refsql VARCHAR(255);
BEGIN
  refsql := 'select id, name from genre';
  OPEN refcur FOR refsql;
  SELECT sysdate INTO rundate FROM DUAL;
END;
/
show errors
