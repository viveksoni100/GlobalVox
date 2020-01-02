package ch02.sample1;

import java.util.GregorianCalendar;

import junit.framework.TestCase;

/**
 */
public class WeatherServiceTest extends TestCase {

  public void testSample1() throws Exception {
    WeatherService ws = new WeatherService();
    Double high = ws.getHistoricalHigh(new GregorianCalendar(2004, 0, 1).getTime());
    //  ... do more validation of returned value here, this test is not realistic
    System.out.println("High was: " + high);
  }
}
