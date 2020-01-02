package ch03.sample4;

import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import ch02.sample2.StaticDataWeatherDaoImpl;
import ch02.sample2.WeatherService;
import ch02.sample2.WeatherServiceImpl;

/**
 */
public class WeatherServiceTest extends TestCase {

  public void testBeanFactoryLoadedFromProperties() throws Exception {
    
    DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
    PropertiesBeanDefinitionReader bdReader = new PropertiesBeanDefinitionReader(bf);
    Resource def = new ClassPathResource("ch03/sample4/beans.properties");
    bdReader.loadBeanDefinitions(def);

    // now test the contents
    
    WeatherService ws = (WeatherService) bf.getBean("weatherService");

    Double high = ws.getHistoricalHigh(new GregorianCalendar(2004, 0, 1).getTime());
    System.out.println("High was: " + high);
  }
  
  public void testApplicationContextLoadedFromProperties() throws Exception {
    
    GenericApplicationContext ctx = new GenericApplicationContext();
    PropertiesBeanDefinitionReader bdReader = new PropertiesBeanDefinitionReader(ctx);
    Resource def = new ClassPathResource("ch03/sample4/beans.properties");
    bdReader.loadBeanDefinitions(def);
    ctx.refresh();
    
    // now test the contents
    
    WeatherService ws = (WeatherService) ctx.getBean("weatherService");

    Double high = ws.getHistoricalHigh(new GregorianCalendar(2004, 0, 1).getTime());
    System.out.println("High was: " + high);
  }
  
  public void testBeanFactoryLoadedProgrammatically() throws Exception {
    
    DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
    
    MutablePropertyValues mpv = new MutablePropertyValues();
    mpv.addPropertyValue("weatherDao", new RuntimeBeanReference("weatherDao"));
    RootBeanDefinition weatherService = new RootBeanDefinition(WeatherServiceImpl.class, mpv);
    bf.registerBeanDefinition("weatherService", weatherService);
    
    mpv = new MutablePropertyValues();
    RootBeanDefinition weatherDao = new RootBeanDefinition(StaticDataWeatherDaoImpl.class, mpv);
    bf.registerBeanDefinition("weatherDao", weatherDao);
    
    // now test the contents
    
    WeatherService ws = (WeatherService) bf.getBean("weatherService");

    Double high = ws.getHistoricalHigh(new GregorianCalendar(2004, 0, 1).getTime());
    System.out.println("High was: " + high);
  }
}
