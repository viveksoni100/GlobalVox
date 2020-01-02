package org.springframework.prospring.ticket.db;

import oracle.sql.ArrayDescriptor;
import oracle.sql.ARRAY;
import oracle.jdbc.OracleTypes;

import java.util.HashMap;
import java.util.Map;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Types;
import java.math.BigDecimal;

import org.springframework.jdbc.core.ParameterMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;

public class CallReserveSeats extends StoredProcedure {
    private static final String RESERVE_SEATS_SQL = "reserve_seats";

    public CallReserveSeats(DataSource dataSource) {
        super(dataSource, RESERVE_SEATS_SQL);
        declareParameter(new SqlParameter("performance_id", Types.INTEGER));
        declareParameter(new SqlParameter("seats", Types.ARRAY));
        declareParameter(new SqlParameter("price", Types.DECIMAL));
        declareParameter(new SqlParameter("reserved_until", Types.TIMESTAMP));
        declareParameter(new SqlOutParameter("new_booking_id", Types.INTEGER));
        compile();
    }

    public Map execute(final Integer id,
                       final Integer[] seats,
                       final BigDecimal price,
                       final java.sql.Date reservedUntil) {
        return execute(new ParameterMapper() {
            public Map createMap(Connection conn) throws SQLException {
                HashMap inpar = new HashMap(4);
                inpar.put("performance_id", id);
                ArrayDescriptor desc = new ArrayDescriptor("NUMBERS", conn);
                ARRAY nums = new ARRAY(desc, conn, seats);
                inpar.put("seats", nums);
                inpar.put("price", price);
                inpar.put("reserved_until", reservedUntil);
                return inpar;
            }
        });
    }
}
