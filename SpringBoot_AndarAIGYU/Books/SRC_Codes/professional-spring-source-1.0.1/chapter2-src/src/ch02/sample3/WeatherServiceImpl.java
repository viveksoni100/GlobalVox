package ch02.sample3;

import java.util.Date;

import ch02.sample3.WeatherDao;
import ch02.sample3.WeatherData;

/**
 */
public class WeatherServiceImpl implements WeatherService {

  WeatherDao weatherDao;
  
  public WeatherServiceImpl(WeatherDao weatherDao) {
    this.weatherDao = weatherDao;
  }

  public Double getHistoricalHigh(Date date) {
    WeatherData wd = weatherDao.find(date);
    if (wd != null)
      return new Double(wd.getHigh());
    return null;
  }
}
