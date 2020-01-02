Code drop v1.0.1.

Installation:
~~~~~~~~~~~~~

Build:

    The "build.xml" is the Ant build script of the application.

    Targets:

        * build - this will compile all codes and run all unit tests.
        * dist - this will create the .war file for the application (it will
          be located under the "<application_root>/dist" directory.
        * docs - this will generate the javadocs for the application source code (it
          will be located in under the "<application_root>/doc/api" directory.

Database:

    For convenience, the lib directory contains JDBC client jars for PostgreSQL and HSQLDB.

    The code has been tested with MySQL 4.1.x, and pre-configured for this db. It is
    not possible to include the MySQL JDBC client jar with this download, due to the
    GPL license on that JAR, so for use of the app with MySQL, you must download the JDBC
    client jar from http://www.mysql.org (where you may also get the MySQL DB itself)
    and and drop it into the lib directory before building. The Eclipse project as
    shipped expects this client jar to exist!

    MySQL:
        * There is an error in the first printing of the book, in the command shown for 
          creating the 'spring' user and granting privileges. The proper command is:
              grant all on spring.* to spring@localhost identified by 't1cket';

    PostgreSQL:
        * While the app is configured by default for MySQL, it has also been tested with
          PostgreSQL. For proper operation with PostgreSQL, you must modify
              src/webapp/WEB-INF/jdbc.properties
          appropriately, commenting out the MySQL driver and url setting, and uncommenting
          the PostgreSQL variants. You must also modify
              src/webapp/WEB-INF/applicationContext.xml
          and in the Hibernate LocalSessionFactory definition, comment out the MySQL dialect,
          and uncomment the PostgreSQL dialect.
        * The db related tests will also fail unless
              src/test/jdbc.properties
          is changed in a similar fashion to specify PostgreSQL instead of MySQL settings.
    
    Oracle:
        * It is not possible to include the Oracle JDBC client jar with this download, due
          to the license under which it is provided. For use of this app with Oracle, you must
	  download the JDBC client jar from http://www.oracle.com and and drop it into the lib
	  directory before building.
        * Follow the same basic procedure for configuring the app for Oracle, as described for
          PostgreSQL

    HSQL:
        * Follow the same basic procedure for configuring the app for HSQLDB, as described for
          PostgreSQL

    Depending on the database used, use the appropriate scripts under the etc/db directory to
    create and populates the database used by the application. Here are the general
    instructions (the book has more details for the first 2 steps, for MySQL):

        * Create a database called "spring"
        * Create a database user "spring" with password "t1cket" (not that it's a '1' as the
	  second character of the pass, not an 'i'). The user should have all permissions on
	  the "spring" database.
        * Run script create-ticket-[db_name].sql (where [db_name] is the name of the database
          used) - for mysql it would be create-ticket-mysql.sql.
        * To run and see the application work the database should be populated with some data.
          The load-ticket-[db_name].sql script does exactly that.
        * If you wish to re-create the database, use drop-ticket-[db_name].sql to drop all
          created tables.

Servlet Engine:

    * Note that the book talks about TomCat 5, but this code has actually been tested
      in TomCat 5.5. TomCat 5.5 is now mature and should be used in preference to TomCat 5.

NOTE:

    * Before deploying (or re-deploying) the application, you may use the
      various properties files under the "WEB-INF" folder to configure the application.
      See the "WEB-INF/ticket-servlet.xml" for instructions how to enable/disable smtp
      functionality.
     