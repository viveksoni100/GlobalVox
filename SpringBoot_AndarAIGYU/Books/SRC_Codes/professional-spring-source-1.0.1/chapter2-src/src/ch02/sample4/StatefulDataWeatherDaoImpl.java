package ch02.sample4;

import java.util.Date;

/**
 * Implementation of WeatherDao
 */
public class StatefulDataWeatherDaoImpl implements WeatherDao {

  public WeatherData find(Date date) {

    WeatherData wd = new WeatherData();
    wd.setDate((Date) date.clone());
    // some bogus values
    wd.setLow(date.getMonth() + 5);
    wd.setHigh(date.getMonth() + 15);
    return wd;
  }

  public WeatherData save(Date date) {
    throw new UnsupportedOperationException("This class uses static data only");
  }

  /* (non-Javadoc)
   * @see ch02.sample1.WeatherDao#update(java.util.Date)
   */
  public WeatherData update(Date date) {
    throw new UnsupportedOperationException("This class uses static data only");
  }
}