package ch02.sample5;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 */
public class Sample5Test extends TestCase {

  public void testBeanIds() throws Exception {
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        "ch02/sample5/applicationContext.xml");

    TestBean bean1 = (TestBean) ctx.getBean("bean1");

    TestBean bean2 = (TestBean) ctx.getBean("bean2");

    TestBean bean3 = (TestBean) ctx.getBean("/myservlet/myaction");

    TestBean comp1 = (TestBean) ctx.getBean("component1-dataSource");
    TestBean comp2 = (TestBean) ctx.getBean("component2-dataSource");
    TestBean comp3 = (TestBean) ctx.getBean("component3-dataSource");
  }

  public void testCreation() throws Exception {
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        "ch02/sample5/applicationContext-testCreation.xml");

    TestBean testBeanObtainedViaStaticFactory = (TestBean) ctx
        .getBean("testBeanObtainedViaStaticFactory");

    TestBean testBeanObtainedViaNonStaticFactory = (TestBean) ctx
        .getBean("testBeanObtainedViaNonStaticFactory");
  }

  public void testSingleton() throws Exception {
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        "ch02/sample5/applicationContext-testSingleton.xml");

    for (int i = 0; i < 5; ++i) {
      TestBean singleton1 = (TestBean) ctx.getBean("singleton1");
      singleton1.printCount();
    }
    
    for (int i = 0; i < 5; ++i) {
      TestBean prototype1 = (TestBean) ctx.getBean("prototype1");
      prototype1.printCount();
    }
  }
}
