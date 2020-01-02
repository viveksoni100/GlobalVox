package ch02.sample9;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 */
public class CustomPropertyEditorTest extends TestCase {

  public void testBeanPostProcessorForAppContext() throws Exception {

    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        "ch02/sample9/applicationContext.xml");
    
    StartEndDatesBean startEnd = (StartEndDatesBean) ctx.getBean("startEndDatesBean");

    // post-processor should have printed out each bean 
  }

}
