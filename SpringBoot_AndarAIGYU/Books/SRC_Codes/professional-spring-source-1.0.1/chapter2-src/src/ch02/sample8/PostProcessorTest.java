package ch02.sample8;

import junit.framework.TestCase;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 */
public class PostProcessorTest extends TestCase {

  public void testBeanPostProcessorForAppContext() throws Exception {

    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        "ch02/sample8/applicationContext.xml");

    // post-processor should have printed out each bean 
  }

  public void testBeanPostProcessorForBeanFactory() throws Exception {
    XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("ch02/sample8/beans.xml"));
    BeanInitializationLogger logger = new BeanInitializationLogger();
    factory.addBeanPostProcessor(logger);
    // our beans are singletons, so will be pre-instantiated, at
    // which time the post-processor will get callbacks for them too
    factory.preInstantiateSingletons();
  }
  
  public void testFactoryPostProcessorForAppContext() throws Exception {

    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        "ch02/sample8/applicationContext2.xml");

    // post-processor should have printed out each bean 
  }
  
  public void testFactoryPostProcessorForBeanFactory() throws Exception {
    XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("ch02/sample8/beans.xml"));
    AllBeansLister lister = new AllBeansLister();
    lister.postProcessBeanFactory(factory);
  }
  
}
