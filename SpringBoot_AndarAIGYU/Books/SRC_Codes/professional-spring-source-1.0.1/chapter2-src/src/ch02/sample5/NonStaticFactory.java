package ch02.sample5;

/**
 */
public class NonStaticFactory {
  public TestBean getTestBeanInstance() {
    return new TestBean();
  }
}
