package ch02.sample7;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 */
public class CollectionsBean {
  
  List theList;
  Set theSet;
  Map theMap;
  Properties theProperties;
  
  public void setTheList(List theList) {
    this.theList = theList;
  }
  public List getTheList() {
    return theList;
  }
  
  public void setTheSet(Set theSet) {
    this.theSet = theSet;
  }
  public Set getTheSet() {
    return theSet;
  }
  
  public void setTheMap(Map theMap) {
    this.theMap = theMap;
  }
  public Map getTheMap() {
    return theMap;
  }
  
  public void setTheProperties(Properties theProperties) {
    this.theProperties = theProperties;
  }
  public Properties getTheProperties() {
    return theProperties;
  }
}
