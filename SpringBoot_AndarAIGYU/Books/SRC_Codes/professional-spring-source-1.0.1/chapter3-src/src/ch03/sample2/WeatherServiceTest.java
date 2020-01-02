package ch03.sample2;

import java.util.Date;
import java.util.GregorianCalendar;

import org.easymock.MockControl;

import junit.framework.TestCase;
import ch02.sample2.StaticDataWeatherDaoImpl;
import ch02.sample2.WeatherDao;
import ch02.sample2.WeatherData;
import ch02.sample2.WeatherServiceImpl;

/**
 */
public class WeatherServiceTest extends TestCase {

  public void testWithRealObjects() throws Exception {
    
    WeatherServiceImpl ws = new WeatherServiceImpl();
    ws.setWeatherDao(new StaticDataWeatherDaoImpl());
    
    Double high = ws.getHistoricalHigh(new GregorianCalendar(2004, 0, 1).getTime());
    System.out.println("High was: " + high);
  }
  
  
  public void testWithMock() throws Exception {
    
    WeatherServiceImpl ws = new WeatherServiceImpl();

    ws.setWeatherDao(new WeatherDao() {
      public WeatherData find(Date date) {
        
        WeatherData wd = new WeatherData();
        wd.setDate(date);
        wd.setLow(10);
        wd.setHigh(20);
        return wd;
      }

      // 2 methods not needed for test
      public WeatherData save(Date date) { return null; }
      public WeatherData update(Date date) { return null; }
    });
    
    Double high = ws.getHistoricalHigh(new GregorianCalendar(2004, 0, 1).getTime());
    assertTrue(high.equals(new Double(20)));
  }
  
  public void testWithEasyMock() throws Exception {
    
    // create test data
    Date date = new GregorianCalendar(2004, 0, 1).getTime();
    WeatherData wd = new WeatherData();
    wd.setDate(date);
    wd.setLow(10);
    wd.setHigh(20);
    
    // create mock weather dao with Easymock, returning test data on find() call
    MockControl control = MockControl.createControl(WeatherDao.class);
    WeatherDao weatherDao = (WeatherDao) control.getMock();
    // set the method we expect called on the mock, and return value
    weatherDao.find(date);
    control.setReturnValue(wd);
    
    WeatherServiceImpl ws = new WeatherServiceImpl();
    ws.setWeatherDao(weatherDao);
    
    // turn on the mock
    control.replay();
    Double high = ws.getHistoricalHigh(date);
    // verify the mock actually received a find() call
    control.verify();

    assertTrue(high.equals(new Double(20)));
  }
}
