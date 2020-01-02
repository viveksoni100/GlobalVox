package org.springframework.prospring.ticket.db;

import junit.framework.TestCase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class MinimalTest extends TestCase {
  private DriverManagerDataSource dataSource;

  public void setUp() {

    dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName( "org.hsqldb.jdbcDriver");
    dataSource.setUrl( "jdbc:hsqldb:hsql://localhost:9001");
    dataSource.setUsername( "sa");
    dataSource.setPassword( "");

    JdbcTemplate jt = new JdbcTemplate(dataSource);
    jt.execute("delete from mytable");
    jt.execute("insert into mytable (id, name) values(1, 'John')");
    jt.execute("insert into mytable (id, name) values(2, 'Jane')");

  }

  public void testSomething() {

    // the actual test code goes here

  }
}
