package org.springframework.samples.jpetstore.web.pages;

import org.apache.tapestry.IPage;

/**
 * Marks pages that have an error property (of type String).
 *
 * @author Colin Sampaleanu
 **/

public interface ErrorProperty extends IPage {

  public String getError();

  public void setError(String value);
  
}
