package ch02.sample2;

import java.util.Date;

/**
 * Data Access Object Interface for getting and storing weather records
 */
public interface WeatherDao {

  /**
   * Returns the WeatherData for a date, or null if there is none 
   * @param date the date to search on 
   */
  WeatherData find(Date date);

  /**
   * Saves the WeatherData for a date
   */
  WeatherData save(Date date);

  WeatherData update(Date date);
}