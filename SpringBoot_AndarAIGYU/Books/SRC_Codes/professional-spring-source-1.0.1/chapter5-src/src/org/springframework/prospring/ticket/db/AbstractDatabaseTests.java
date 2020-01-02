package org.springframework.prospring.ticket.db;

import junit.framework.TestCase;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractDatabaseTests extends TestCase {
    protected SingleConnectionDataSource ds;

    protected void setUp() throws Exception {
        super.setUp();
        ds = new SingleConnectionDataSource();
//        ds.setDriverClassName("org.hsqldb.jdbcDriver");
//        ds.setUrl("jdbc:hsqldb:hsql://localhost:9001");
//        ds.setUsername("sa");
//        ds.setPassword("");
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/spring");
        ds.setUsername("spring");
        ds.setPassword("t1cket");
//        ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//        ds.setUrl("jdbc:oracle:thin:@fiji:1521:my10g");
//        ds.setUsername("spring");
//        ds.setPassword("t1cket");
        loadTestData();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        ds.destroy();
    }

    protected void loadTestData() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        jt.execute("delete from Seat_Status");
        jt.execute("delete from Seat_Plan_Seat");
        jt.execute("delete from Booking");
        jt.execute("delete from Price_Band");
        jt.execute("delete from Performance");
        jt.execute("delete from Price_Structure");
        jt.execute("delete from Shows");
        jt.execute("delete from Genre");
        jt.execute("delete from Seating_Plan");
        jt.execute("delete from Seat_Class");
        jt.execute("update Seat set left_Seat_id = null, right_Seat_id = null");
        jt.execute("delete from Seat");
        jt.execute("insert into genre (id, name) values (1, 'Rock')");
        jt.execute("insert into genre (id, name) values (2, 'Ballet')");
        jt.execute("insert into genre (id, name) values (3, 'Theatre')");
        jt.execute("insert into genre (id, name) values (4, 'Classical')");
        jt.execute("insert into genre (id, name) values (5, 'Musical')");
        jt.execute("insert into genre (id, name) values (6, 'Opera')");
        jt.execute("insert into seating_plan (id, name) values (1, 'Standard Opera Seating')");
        jt.execute("insert into shows (id, name, Genre_id, Seating_plan_id) values (1, 'Romeo and Juliet', 3, 1)");
        jt.execute("insert into shows (id, name, Genre_id, Seating_plan_id) values (2, 'Waiting for Godot', 3, 1)");
        jt.execute("insert into shows (id, name, Genre_id, Seating_plan_id) values (3, 'Giselle', 2, 1)");
        jt.execute("insert into shows (id, name, Genre_id, Seating_plan_id) values (4, 'Tristan und Isolde', 6, 1)");
        jt.execute("insert into shows (id, name, Genre_id, Seating_plan_id) values (5, 'La Traviata', 6, 1)");
        jt.execute("insert into shows (id, name, Genre_id, Seating_plan_id) values (6, 'Miss Saigon', 5, 1)");
        jt.execute("insert into shows (id, name, Genre_id, Seating_plan_id) values (7, 'Cats', 5, 1)");
        jt.execute("insert into seat_class (id, code, description) values (1, 'AA', 'Premium Reserve')");
        jt.execute("insert into seat_class (id, code, description) values (2, 'A', 'A Reserve')");
        jt.execute("insert into price_structure (id, name) values (1, 'Standard opera pricing')");
        jt.execute("insert into price_band (id, Price_Structure_id, Seat_Class_id, price) values (1, 1, 1, 195)");
        jt.execute("insert into price_band (id, Price_Structure_id, Seat_Class_id, price) values (2, 1, 2, 160)");
        jt.execute("insert into performance (id, date_and_time, Show_id, Price_Structure_id) values (1, {ts '2004-11-12 19:30:00'}, 1, 1)");
        jt.execute("insert into performance (id, date_and_time, Show_id, Price_Structure_id) values (2, {ts '2005-01-14 19:30:00'}, 1, 1)");
        jt.execute("insert into seat (id, name, row_or_box, row_pos) values (1, 'Seat 1-1', 1, 1)");
        jt.execute("insert into seat (id, name, row_or_box, row_pos) values (2, 'Seat 1-2', 1, 2)");
        jt.execute("insert into seat (id, name, row_or_box, row_pos) values (3, 'Seat 2-1', 2, 1)");
        jt.execute("insert into seat (id, name, row_or_box, row_pos) values (4, 'Seat 2-2', 2, 2)");
        jt.execute("insert into seat (id, name, row_or_box, row_pos) values (5, 'Seat 2-3', 2, 3)");
        jt.execute("insert into seat (id, name, row_or_box, row_pos) values (6, 'Seat 2-4', 2, 4)");
        jt.execute("update seat set right_Seat_id = 2 where id = 1");
        jt.execute("update seat set left_Seat_id = 1 where id = 2");
        jt.execute("update seat set right_Seat_id = 4 where id = 3");
        jt.execute("update seat set left_Seat_id = 3, right_Seat_id = 5 where id = 4");
        jt.execute("update seat set left_Seat_id = 4, right_Seat_id = 6 where id = 5");
        jt.execute("update seat set left_Seat_id = 5 where id = 6");
        jt.execute("insert into Seat_Plan_Seat (Seat_id, Seat_Class_id, Seating_Plan_id) values(1, 1, 1)");
        jt.execute("insert into Seat_Plan_Seat (Seat_id, Seat_Class_id, Seating_Plan_id) values(2, 1, 1)");
        jt.execute("insert into booking (id, date_made, price) values (1, {d '2004-11-14'}, 122.45)");
        jt.execute("insert into booking (id, date_made, price) values (2, {d '2004-11-16'}, 445.25)");
        jt.execute("insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (1, 1, 1, 1)");
        jt.execute("insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (2, 1, 1, null)");
        jt.execute("insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (1, 1, 2, 2)");
        jt.execute("insert into seat_status (Seat_id, Price_Band_id, Performance_id, Booking_id) values (2, 1, 2, 2)");

    }
}
