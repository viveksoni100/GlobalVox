/*
 * Created on 6-Oct-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ch02.sample1;

import java.util.Date;

/**
 */
public class WeatherService {

  WeatherDao weatherDao = new StaticDataWeatherDaoImpl();

  public Double getHistoricalHigh(Date date) {
    WeatherData wd = weatherDao.find(date);
    if (wd != null)
      return new Double(wd.getHigh());
    return null;
  }
}