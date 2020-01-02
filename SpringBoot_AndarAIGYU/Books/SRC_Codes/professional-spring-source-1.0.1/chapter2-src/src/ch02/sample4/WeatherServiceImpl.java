package ch02.sample4;

import java.util.Date;

import ch02.sample4.WeatherDao;
import ch02.sample4.WeatherData;

/**
 */
public abstract class WeatherServiceImpl implements WeatherService {

  protected abstract WeatherDao getWeatherDao();

  public Double getHistoricalHigh(Date date) {
    WeatherData wd = getWeatherDao().find(date);
    if (wd != null)
      return new Double(wd.getHigh());
    return null;
  }
}
