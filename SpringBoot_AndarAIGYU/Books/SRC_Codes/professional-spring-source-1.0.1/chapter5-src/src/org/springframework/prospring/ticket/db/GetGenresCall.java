package org.springframework.prospring.ticket.db;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.prospring.ticket.domain.Genre;

import javax.sql.DataSource;
import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetGenresCall extends StoredProcedure {
    private static final String GET_GENRES_SQL = "get_genres";

    public GetGenresCall(DataSource dataSource) {
        super(dataSource, GET_GENRES_SQL);
        declareParameter(new SqlOutParameter("genre",
                oracle.jdbc.OracleTypes.CURSOR, new MapGenre()));
        declareParameter(new SqlOutParameter("rundate", java.sql.Types.TIMESTAMP));
        compile();
    }

    Map executeGetGenre() {
        Map out = execute(new HashMap());
        return out;
    }

}

class MapGenre implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("name"));
        return genre;
    }
}
