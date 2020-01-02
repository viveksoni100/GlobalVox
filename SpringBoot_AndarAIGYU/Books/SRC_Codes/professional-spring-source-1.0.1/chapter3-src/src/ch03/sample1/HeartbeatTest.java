package ch03.sample1;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 */
public class HeartbeatTest extends TestCase {

  public void testSample2() throws Exception {
    
    // loading the context will pre-instantiate singletons. The TimerFactory will at
    // that time start the scheduled timer task
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        "ch03/sample1/applicationContext.xml");

    // sleep for 30 seconds to allow the timer to fire, and events to get published
    Thread.sleep(30 * 1000);
  }
}
