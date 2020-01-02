package org.springframework.prospring.ticket.db;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.incrementer.HsqlMaxValueIncrementer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RdbmsOperationsTests  extends AbstractDatabaseTests  {

    public void testGetPerformance() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        jt.execute("insert into Performance (id, date_and_time)" +
            " values(3, {d '2005-01-31'})");
        PerformanceQuery performanceQuery = new PerformanceQuery(ds);
        List perfList = performanceQuery.execute(3);
        assertEquals("list contains one entry", 1, perfList.size());
    }

    public void testSqlUpdate() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        jt.execute("insert into Performance (id, date_and_time)" +
            " values(3, {d '2005-01-31'})");
        SqlUpdate updatePerformance = new SqlUpdate();
        updatePerformance.setDataSource(ds);
        updatePerformance.setSql("update Performance set date_and_time = ? where id = ?");
        updatePerformance.declareParameter(
                new SqlParameter("date_and_time", Types.TIMESTAMP));
        updatePerformance.declareParameter(new SqlParameter("id", Types.INTEGER));
        updatePerformance.compile();
        Object[] parameters = new Object[]{new Timestamp(System.currentTimeMillis()),
            new Integer(3)};
        int count = updatePerformance.update(parameters);
        assertEquals("update count", 1, count);
    }

    public void testInsertWithSqlUpdate() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        jt.execute("delete from Performance");
        SqlUpdate insertPerformance = new SqlUpdate();
        insertPerformance.setDataSource(ds);
        insertPerformance.setSql(
                "insert into Performance (id, date_and_time) values(?, ?)");
        insertPerformance.declareParameter(new SqlParameter("id", Types.INTEGER));
        insertPerformance.declareParameter(
                new SqlParameter("date_and_time", Types.TIMESTAMP));
        insertPerformance.compile();
        Object[] parameters = new Object[]{new Integer(1),
            new Timestamp(System.currentTimeMillis())};
        int count = insertPerformance.update(parameters);
        parameters = new Object[]{new Integer(2),
            new Timestamp(System.currentTimeMillis())};
        count = count + insertPerformance.update(parameters);
        assertEquals("insert count", 2, count);
    }

    public void testUpdateBookigs() {
        // Not supported by HSQL
        Map context = new HashMap(1);
        context.put("increase", new BigDecimal(10.00));
        final String sql = "select id, price from Booking where date_made > ?";
        UpdateBookings query = new UpdateBookings(ds, sql);
        java.util.Date dateMade = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateMade = df.parse("2004-06-01");
        } catch (ParseException e) {
            throw new InvalidDataAccessApiUsageException(e.getMessage(), e);
        }
        Object[] parameters = new Object[]{new java.sql.Date(dateMade.getTime())};
        query.execute(parameters, context);
    }

    public void testHsqlGenerateKeys() {
        HsqlMaxValueIncrementer incr =
        new HsqlMaxValueIncrementer(ds, "booking_seq", "value");
        Long key = new Long(incr.nextLongValue());
        Date dateMade = new Date();
        Date reservedUntil = new Date(dateMade.getTime() + (5L * 24L * 60L * 60L * 1000L));
        BigDecimal price = new BigDecimal("13.72");
        SqlUpdate su = new SqlUpdate();
        su.setDataSource(ds);
        su.setSql(
                "insert into booking(id, date_made, reserved_until, price) " +
                        "values (?, ?, ?, ?)");
        su.declareParameter(new SqlParameter(Types.INTEGER));
        su.declareParameter(new SqlParameter(Types.TIMESTAMP));
        su.declareParameter(new SqlParameter(Types.TIMESTAMP));
        su.declareParameter(new SqlParameter(Types.DECIMAL));
        su.compile();

        Object[] parameters = new Object[]{key, dateMade, reservedUntil, price};
        su.update(parameters);
        System.out.println("New key: " + key);
    }

    public void testMySqlGenerateKeys() {
        Date dateMade = new Date();
        Date reservedUntil = new Date(dateMade.getTime() + (5L * 24L * 60L * 60L * 1000L));
        BigDecimal price = new BigDecimal("13.72");
        SqlUpdate su = new SqlUpdate();
        su.setDataSource(ds);
        su.setSql(
                "insert into booking(date_made, reserved_until, price) " +
                        "values (?, ?, ?)");
        su.declareParameter(new SqlParameter(Types.TIMESTAMP));
        su.declareParameter(new SqlParameter(Types.TIMESTAMP));
        su.declareParameter(new SqlParameter(Types.DECIMAL));
        su.compile();

        Object[] parameters = new Object[]{dateMade, reservedUntil, price};
        KeyHolder keyHolder = new GeneratedKeyHolder();
        su.update(parameters, keyHolder);
        long key = keyHolder.getKey().longValue();
        System.out.println("New key: " + key);
    }

}
