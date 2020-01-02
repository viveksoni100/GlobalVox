package ch02.sample6;

import java.util.Date;

import org.springframework.dao.DataAccessException;

/**
 * Data Access Object Interface for getting and storing weather records
 */
public interface WeatherDao {

  /**
   * Returns the WeatherData for a date, or null if there is none 
   * @param date the date to search on 
   * @throws DataAccessException on any data access failure
   */
  WeatherData find(Date date) throws DataAccessException;

  /**
   * Saves the WeatherData for a date
   * @throws DataAccessException on any data access failure
   */
  WeatherData save(Date date) throws DataAccessException;

  /**
   * Update the WeatherData for a date
   * @throws DataAccessException on any data access failure
   */
  WeatherData update(Date date) throws DataAccessException;
}