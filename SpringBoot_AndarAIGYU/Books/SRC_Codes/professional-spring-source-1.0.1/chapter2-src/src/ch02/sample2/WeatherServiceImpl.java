package ch02.sample2;

import java.util.Date;

import ch02.sample2.WeatherDao;
import ch02.sample2.WeatherData;

/**
 */
public class WeatherServiceImpl implements WeatherService {

  private WeatherDao weatherDao;
  
  public void setWeatherDao(WeatherDao weatherDao) {
    this.weatherDao = weatherDao;
  }

  public Double getHistoricalHigh(Date date) {
    WeatherData wd = weatherDao.find(date);
    if (wd != null)
      return new Double(wd.getHigh());
    return null;
  }
}
