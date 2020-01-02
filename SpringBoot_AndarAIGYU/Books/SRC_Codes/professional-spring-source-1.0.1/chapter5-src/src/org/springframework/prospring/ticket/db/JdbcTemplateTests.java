package org.springframework.prospring.ticket.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultReader;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplateTests extends AbstractDatabaseTests {

    public void testQueryForInt() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        int count = jt.queryForInt("select count(*) from Genre");
        assertEquals("correct count", 6, count);
    }

    public void testQueryWithParameter() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        Object[] parameters = new Object[] {"M"};
        int count = jt.queryForInt("select count(*) from Genre where name > ?",
            parameters);
        assertEquals("correct count for > 'M'", 4, count);
    }

    public void testQueryForObjectReturningString () {
        JdbcTemplate jt = new JdbcTemplate(ds);
        Object[] parameters = new Object[] {new Integer(2)};
        Object o = jt.queryForObject("select name from Genre where id = ?",
            parameters, String.class);
        assertEquals("correct name returned", "Ballet", (String)o);
    }

    public void testQueryForList() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        Object[] parameters = new Object[] {new Integer(1)};
        List l = jt.queryForList("select id, name from Genre where id > ?",
            parameters);
        assertEquals("correct size of the list", 5, l.size());
    }

    public void testbasicUpdate() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        // remove some rows so we don't violate referential integrity
        jt.execute("delete from Seat_Status");
        jt.execute("delete from Seat_Plan_Seat");
        jt.execute("delete from Booking");
        jt.execute("delete from Price_Band");
        jt.execute("delete from Performance");
        jt.execute("delete from Price_Structure");
        jt.execute("delete from Shows");

        jt.execute("delete from Genre");
        int x = jt.update("insert into Genre (id, name) values(1, 'Opera')");
        x += jt.update("insert into Genre (id, name) values(2, 'Circus')");
        x += jt.update("insert into Genre (id, name) values(3, 'Rock Concert')");
        x += jt.update("insert into Genre (id, name) values(4, 'Symphonie')");
        System.out.println(x + " row(s) inserted.");
        assertEquals("rows inserted", 4, x);
        x = jt.update("update Genre set name = 'Pop/Rock' where id = ?",
            new Object[] {new Integer(3)});
        System.out.println(x + " row(s) updated.");
        assertEquals("rows updated", 1, x);
        x = jt.update("delete from Genre where id = 2");
        System.out.println(x + " row(s) deleted.");
        assertEquals("rows deleted", 1, x);
        List l = jt.queryForList("select id, name from Genre");
        System.out.println(l);
        assertEquals("correct size of the list", 3, l.size());
    }

    public List aPreparedStatementCreatorQuery(final int id) {
        JdbcTemplate jt = new JdbcTemplate(ds);
        final String sql = "select id, name from genre where id < ?";
        List results = jt.query(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection con)
                            throws SQLException {
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setInt(1, id);
                        return ps;
                    }
                },
                new ResultReader() {
                    List names = new ArrayList();

                    public void processRow(ResultSet rs) throws SQLException {
                        names.add(rs.getString("name"));
                    }

                    public List getResults() {
                        return names;
                    }
                }
        );
        return results;
    }

    public void testAPreparedStatementCreatorQuery() {
        List l = aPreparedStatementCreatorQuery(3);
        assertEquals("correct size of the list", 2, l.size());
    }

    public List aPreparedStatementSetterQuery(final int id) {
        JdbcTemplate jt = new JdbcTemplate(ds);
        final String sql = "select id, name from genre where id < ?";
        List results = jt.query(sql,
                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps)
                            throws SQLException {
                        ps.setInt(1, id);
                    }
                },
                new ResultReader() {
                    List names = new ArrayList();

                    public void processRow(ResultSet rs) throws SQLException {
                        names.add(rs.getString("name"));
                    }

                    public List getResults() {
                        return names;
                    }
                }
        );
        return results;
    }

    public void testAPreparedStatementSetterQuery() {
        List l = aPreparedStatementSetterQuery(4);
        assertEquals("correct size of the list", 3, l.size());
    }

    public void testQueryForRowset() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        Object[] parameters = new Object[] {new Integer(1)};
        SqlRowSet srs = jt.queryForRowSet(
            "select id, name from Genre where id > ?",
            parameters);
        int rowCount = 0;
        while (srs.next()) {
             System.out.println(srs.getString("id") + " - " + srs.getString("name"));
            rowCount++;
        }
        assertEquals("correct number of rows", 5, rowCount);
    }

}
