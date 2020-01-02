package org.springframework.prospring.ticket.db;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.prospring.ticket.domain.Performance;

import javax.sql.DataSource;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;

class PerformanceQuery extends MappingSqlQuery {
  private static String SQL_PERFORMANCE_QUERY =
      "select id, date_and_time from Performance where id = ?";

  public PerformanceQuery(DataSource ds) {
    super(ds, SQL_PERFORMANCE_QUERY);
    declareParameter(new SqlParameter("id", Types.INTEGER));
    compile();
  }


  public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
    Performance performance = new Performance();
    performance.setId(rs.getInt("id"));
    performance.setDateAndTime(rs.getTimestamp("date_and_time"));
    return performance;
 }

}
