package ch02.sample3;

import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ch02.sample3.WeatherService;

/**
 */
public class WeatherServiceTest extends TestCase {

  public void testSample3() throws Exception {
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        "ch02/sample3/applicationContext.xml");
    WeatherService ws = (WeatherService) ctx.getBean("weatherService");

    Double high = ws.getHistoricalHigh(new GregorianCalendar(2004, 0, 1).getTime());
    //  ... do more validation of returned value here, this test is not realistic
    System.out.println("High was: " + high);
  }
}
