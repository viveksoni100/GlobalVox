package ch03.sample3;

import java.util.GregorianCalendar;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import ch02.sample2.WeatherService;

/**
 */
public class WeatherServiceTest extends
    AbstractDependencyInjectionSpringContextTests {

  WeatherService weatherService;

  public void setWeatherService(WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  protected String[] getConfigLocations() {
    return new String[]{"ch03/sample3/applicationContext.xml"};
  }

  public void testWeatherService() throws Exception {

    Double high = weatherService.getHistoricalHigh(new GregorianCalendar(2004, 0, 1)
        .getTime());
    //  ... do more validation of returned value here, this test is not realistic
    System.out.println("High was: " + high);
  }
}
