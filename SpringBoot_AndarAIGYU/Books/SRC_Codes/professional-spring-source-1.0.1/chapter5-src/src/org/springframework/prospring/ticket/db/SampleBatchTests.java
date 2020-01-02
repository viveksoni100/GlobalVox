package org.springframework.prospring.ticket.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.object.BatchSqlUpdate;

public class SampleBatchTests extends AbstractDatabaseTests {

    protected void setUp() throws Exception {
        ds = new SingleConnectionDataSource();
        ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        ds.setUrl("jdbc:oracle:thin:@fiji:1521:my10g");
        ds.setUsername("spring");
        ds.setPassword("t1cket");
//        ds.setDriverClassName("com.mysql.jdbc.Driver");
//        ds.setUrl("jdbc:mysql://localhost:3306/spring");
//        ds.setUsername("spring");
//        ds.setPassword("t1cket");
//        lobHandler = new DefaultLobHandler();
        loadTestData();
    }

    public void testInsertBatchWithJdbcTemplate() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        jt.execute("DELETE FROM Contact_List");

        List batchList = createBatchList();

        insertBatch(jt, batchList);

    }

    private void insertBatch(JdbcTemplate db, final List data) {
        int[] actualRowsAffected = db.batchUpdate(
                "insert into contact_list (id, name, email, added) " +
                        "values(?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        Map entry = (Map) data.get(i);
                        ps.setInt(1, ((Integer) entry.get("id")).intValue());
                        ps.setString(2, (String) entry.get("name"));
                        ps.setString(3, (String) entry.get("email"));
                        ps.setDate(4, new java.sql.Date(((Date) entry.get("added")).getTime()));
                    }

                    public int getBatchSize() {
                        return data.size();
                    }
                });
    }

    public void testInsertBatchWithBatchSqlUpdate() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        jt.execute("DELETE FROM Contact_List");

        List batchList = createBatchList();

        insertBatch(ds, batchList);
    }

    private void insertBatch(DataSource dataSource, List data) {
        BatchSqlUpdate update = new BatchSqlUpdate(dataSource,
                "insert into contact_list (id, name, email, added) " +
                        "values(?, ?, ?, ?)");
        update.declareParameter(new SqlParameter("id", Types.INTEGER));
        update.declareParameter(new SqlParameter("name", Types.VARCHAR));
        update.declareParameter(new SqlParameter("email", Types.VARCHAR));
        update.declareParameter(new SqlParameter("added", Types.DATE));

        for (int i = 0; i < data.size(); i++) {
            Map entry = (Map) data.get(i);
            Object[] values = new Object[4];
            values[0] = entry.get("id");
            values[1] = entry.get("name");
            values[2] = entry.get("email");
            values[3] = entry.get("added");
            update.update(values);
        }
        update.flush();
    }

    private List createBatchList() {
        List batchList = new ArrayList(5);
        Map entry1 = new HashMap(4);
        entry1.put("id", new Integer(1));
        entry1.put("name", "Rod");
        entry1.put("email", "rod@sample.com");
        entry1.put("added", new Date());
        batchList.add(entry1);
        Map entry2 = new HashMap(4);
        entry2.put("id", new Integer(1));
        entry2.put("name", "Juergen");
        entry2.put("email", "juergen@sample.com");
        entry2.put("added", new Date());
        batchList.add(entry2);
        Map entry3 = new HashMap(4);
        entry3.put("id", new Integer(1));
        entry3.put("name", "Colin");
        entry3.put("email", "colin@sample.com");
        entry3.put("added", new Date());
        batchList.add(entry3);
        Map entry4 = new HashMap(4);
        entry4.put("id", new Integer(1));
        entry4.put("name", "Alef");
        entry4.put("email", "alef@sample.com");
        entry4.put("added", new Date());
        batchList.add(entry4);
        Map entry5 = new HashMap(4);
        entry5.put("id", new Integer(1));
        entry5.put("name", "Thomas");
        entry5.put("email", "thomas@sample.com");
        entry5.put("added", new Date());
        batchList.add(entry5);
        return batchList;
    }
}



