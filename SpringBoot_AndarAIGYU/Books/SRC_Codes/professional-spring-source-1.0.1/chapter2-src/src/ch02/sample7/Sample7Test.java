package ch02.sample7;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 */
public class Sample7Test extends TestCase {

  public void testBeanIds() throws Exception {
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        "ch02/sample7/applicationContext.xml");

    CollectionsBean example = (CollectionsBean) ctx.getBean("collectionsExample");
  }
}
