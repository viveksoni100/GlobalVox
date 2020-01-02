-- creates the database (called 'spring'), (sans relations), for the boxoffice app
-- for MySQL 4.1 or later

CREATE DATABASE spring;
USE spring;
-- creates the default spring user and grants a set of privileges to said user...
GRANT USAGE ON *.* TO spring@'localhost' IDENTIFIED BY 't1cket';
GRANT SELECT ON *.* TO spring@'localhost';
GRANT INSERT ON *.* TO spring@'localhost';
GRANT UPDATE ON *.* TO spring@'localhost';
GRANT DELETE ON *.* TO spring@'localhost';
GRANT CREATE ON *.* TO spring@'localhost';
GRANT REFERENCES ON *.* TO spring@'localhost';
GRANT INDEX ON *.* TO spring@'localhost';
GRANT ALTER ON *.* TO spring@'localhost';
GRANT CREATE TEMPORARY TABLES ON *.* TO spring@'localhost';
GRANT LOCK TABLES ON *.* TO spring@'localhost';
GRANT EXECUTE ON *.* TO spring@'localhost';
