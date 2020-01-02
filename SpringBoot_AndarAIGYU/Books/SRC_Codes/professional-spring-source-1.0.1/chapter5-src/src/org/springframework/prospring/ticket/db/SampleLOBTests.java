package org.springframework.prospring.ticket.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.dao.DataAccessException;
import org.springframework.util.FileCopyUtils;

import javax.sql.DataSource;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;
import java.util.List;

public class SampleLOBTests extends AbstractDatabaseTests {
    private LobHandler lobHandler;

    protected void setUp() throws Exception {
        ds = new SingleConnectionDataSource();
        ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        ds.setUrl("jdbc:oracle:thin:@fiji:1521:my10g");
        ds.setUsername("spring");
        ds.setPassword("t1cket");
        lobHandler = new OracleLobHandler();
//        ds.setDriverClassName("com.mysql.jdbc.Driver");
//        ds.setUrl("jdbc:mysql://localhost:3306/spring");
//        ds.setUsername("spring");
//        ds.setPassword("t1cket");
//        lobHandler = new DefaultLobHandler();
        loadTestData();
    }

    public void testInsertBLOB() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        jt.execute("DELETE FROM Show_Poster");

        int newId = 1;
        Date firstPerformance = new Date(System.currentTimeMillis());
        File in = new File("spring2004.jpg");
        InputStream is = null;
        try {
            is = new FileInputStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int showId = 1;

        insertBlob(jt, newId, firstPerformance, is, (int) in.length(), showId);

    }

    private void insertBlob(JdbcTemplate jt, final int newId,
                            final Date firstPerformance, final InputStream is,
                            final int blobLength, final int showId) {
        jt.execute(
                "INSERT INTO Show_Poster " +
                        "(id, first_performance, poster_image, Show_id) " +
                        "VALUES (?, ?, ?, ?)",
                new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator)
                            throws SQLException {
                        ps.setInt(1, newId);
                        ps.setDate(2, new java.sql.Date(firstPerformance.getTime()));
                        lobCreator.setBlobAsBinaryStream(ps, 3, is, blobLength);
                        ps.setInt(4, showId);
                    }
                }
        );
    }

    public void testStreamBLOB() {
        JdbcTemplate jt = new JdbcTemplate(ds);

        int id = 1;
        File out = new File("copy-of-spring2004.jpg");
        OutputStream os = null;
        try {
            os = new FileOutputStream(out);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        streamBlob(jt, id, os);
    }

    private void streamBlob(JdbcTemplate jt, final int id, final OutputStream os)
            throws DataAccessException {
        jt.query(
                "SELECT poster_image FROM Show_Poster WHERE id = ?",
                new Object[]{new Integer(id)},
                new AbstractLobStreamingResultSetExtractor() {
                    public void streamData(ResultSet rs) throws SQLException, IOException {
                        FileCopyUtils.copy(lobHandler.getBlobAsBinaryStream(rs, 1), os);
                    }
                }
        );
    }

    public void testInsertWithSqlLobValue() {
        JdbcTemplate jt = new JdbcTemplate(ds);
        jt.execute("DELETE FROM Show_Poster");

        SqlUpdate su = new SqlUpdate(ds,
                "INSERT INTO Show_Poster  " +
                        "(id, first_performance, poster_image, Show_id) " +
                        "VALUES (?, ?, ?, ?)");
        su.declareParameter(new SqlParameter("id", Types.INTEGER));
        su.declareParameter(new SqlParameter("first_performance", Types.DATE));
        su.declareParameter(new SqlParameter("poster_image", Types.BLOB));
        su.declareParameter(new SqlParameter("REF_Show_id", Types.INTEGER));
        su.compile();

        Object[] parameterValues = new Object[4];
        parameterValues[0] = new Integer(1);
        parameterValues[1] = new Date(System.currentTimeMillis());
        File in = new File("spring2004.jpg");
        InputStream is = null;
        try {
            is = new FileInputStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        parameterValues[2] = new SqlLobValue(is, (int) in.length(), lobHandler);
        parameterValues[3] = new Integer(3);

        su.update(parameterValues);
    }

    public void testReadLobWithSqlQuery() {
        PosterQuery pq = new PosterQuery(ds);
        List posterList = pq.execute(1);
        assertEquals("correct nomber of entries", 1, posterList.size());
    }

    private class PosterQuery extends MappingSqlQuery {
      private static final String POSTER_QUERY =
        "SELECT id, first_performance, poster_image FROM Show_Poster WHERE id = ?";

      PosterQuery(DataSource dataSource) {
        super(dataSource, POSTER_QUERY);
        declareParameter(new SqlParameter("id", Types.INTEGER));
        compile();
      }

      public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Poster p = new Poster(
            rs.getInt(1),
            rs.getDate(2),
            lobHandler.getBlobAsBytes(rs, 3));
        System.out.println(p);
        return p;
      }

    }
}



