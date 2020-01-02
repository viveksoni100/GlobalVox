package org.springframework.prospring.ticket.db;

import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.prospring.ticket.domain.Genre;

import java.util.Map;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

public class StoredProcedureTests extends AbstractDatabaseTests {

    protected void setUp() throws Exception {
        ds = new SingleConnectionDataSource();
        ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        ds.setUrl("jdbc:oracle:thin:@fiji:1521:my10g");
        ds.setUsername("spring");
        ds.setPassword("t1cket");
        loadTestData();
    }

    public void testCallReserveSeat() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        jt.execute("delete from seat_status");
        CallReserveSeat proc = new CallReserveSeat(ds);
        int res = proc.execute(1, 2, new BigDecimal("44.12"),
            new java.util.Date(System.currentTimeMillis() + 864000000L));
        System.out.println(">>" + res);
    }

    public void testCallReserveSeats() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        jt.execute("delete from seat_status");
        Integer[] numarr = {new Integer(2), new Integer(3)};
        CallReserveSeats proc = new CallReserveSeats(ds);
        Map res = proc.execute(new Integer(1),
                numarr,
                new BigDecimal("22.44"),
                new java.sql.Date(System.currentTimeMillis() + 864000000L));
        System.out.println(">>" + res);
    }

    public void testGetGenresCall() {
        GetGenresCall proc = new GetGenresCall(ds);
        Map res = proc.executeGetGenre();
        System.out.println(">>" + res);
        List genre = (List)res.get("genre");
        for (int i = 0; i < genre.size(); i++) {
            Genre g = (Genre)genre.get(i);
            System.out.println(">>>" + g.getName());
        }
    }

}
