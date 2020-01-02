package ch02.sample6;

import java.util.Date;

import org.springframework.dao.DataAccessException;

import ch02.sample6.WeatherDao;
import ch02.sample6.WeatherData;

/**
 */
public class WeatherServiceImpl implements WeatherService {

  WeatherDao weatherDao;
  int maxRetryAttempts = 0;
  
  public WeatherServiceImpl(WeatherDao weatherDao) {
    this.weatherDao = weatherDao;
  }
  
  public void setMaxRetryAttempts(int maxRetryAttempts) {
    this.maxRetryAttempts = maxRetryAttempts;
  }

  public Double getHistoricalHigh(Date date) {
    WeatherData wd = null;
    
    int retries = 0;
    while (true) {
      try {
        wd = weatherDao.find(date);
      }
      catch (DataAccessException e) {
        if (retries++ < maxRetryAttempts)
          continue;
        
        throw new RuntimeSeriveException("Unable to obtain weather data");
      }
      break;
    }
    if (wd != null)
      return new Double(wd.getHigh());
    return null;
  }
}
