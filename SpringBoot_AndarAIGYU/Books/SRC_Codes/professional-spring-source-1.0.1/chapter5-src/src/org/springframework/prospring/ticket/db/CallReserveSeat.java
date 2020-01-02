package org.springframework.prospring.ticket.db;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;

public class CallReserveSeat extends StoredProcedure {
    private static final String RESERVE_SEAT_SQL = "reserve_seat";

    public CallReserveSeat(DataSource dataSource) {
        super(dataSource, RESERVE_SEAT_SQL);
        declareParameter(new SqlParameter("performance_id", Types.INTEGER));
        declareParameter(new SqlParameter("seat", Types.INTEGER));
        declareParameter(new SqlParameter("price", Types.DECIMAL));
        declareParameter(new SqlParameter("reserved_until", Types.TIMESTAMP));
        declareParameter(new SqlOutParameter("new_booking_id", Types.INTEGER));
        compile();
    }

    public int execute(int performanceId, int seatId, BigDecimal price,
                       java.util.Date reservedUntil) {
        Map inParams = new HashMap(4);
        inParams.put("performance_id", new Integer(performanceId));
        inParams.put("seat", new Integer(seatId));
        inParams.put("price", price);
        inParams.put("reserved_until", new java.sql.Date(reservedUntil.getTime()));
        Map outParams = execute(inParams);
        if (outParams.size() > 0)
            return ((Integer) outParams.get("new_booking_id")).intValue();
        else
            return 0;
    }
}

