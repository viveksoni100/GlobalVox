package org.springframework.prospring.ticket.db;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.UpdatableSqlQuery;

import javax.sql.DataSource;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.Map;

public class UpdateBookings extends UpdatableSqlQuery {
    public UpdateBookings(DataSource dataSource, String sql) {
        super(dataSource, sql);
        declareParameter(new SqlParameter("date_made", Types.DATE));
        compile();
    }

    public Object updateRow(ResultSet rs, int rowNum, Map context)
            throws SQLException {
        BigDecimal price = rs.getBigDecimal("price");
        price = price.add((BigDecimal) context.get("increase"));
        rs.updateBigDecimal("price", price);
        return null;
    }
}