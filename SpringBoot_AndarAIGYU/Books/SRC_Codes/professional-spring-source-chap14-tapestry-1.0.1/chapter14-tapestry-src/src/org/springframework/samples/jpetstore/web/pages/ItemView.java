package org.springframework.samples.jpetstore.web.pages;

import org.apache.tapestry.IRequestCycle;
import org.springframework.samples.jpetstore.domain.Item;
import org.springframework.samples.jpetstore.domain.logic.PetStoreFacade;

/**
 */
public abstract class ItemView extends PetStoreBasePage {
  
  public abstract Item getItem();
  public abstract void setItem(Item item);
  
  public abstract String getItemIdFilter();
  
  public abstract PetStoreFacade getPetStore();

  
  /**
   * handle form submissions, which let an item be specified 
   **/
  public void formSubmit(IRequestCycle cycle) {
    
    if (getItemIdFilter() != null) {
	    Item item = getPetStore().getItem(getItemIdFilter());
	    setItem(item);
    }
  }
  
  // JPetStore is pretty lame (for the record it was not written by Spring people :-)
  // as it actually hard-codes HTML and relative image paths into product 
  // descriptions in the DB! This function transcribes them so they are ok for Tapestry,
  // by adding the servlet context. This function hard-codes the context name, which you
  // would never do in real life, but in real life you would never put in html and html
  // image paths into the db!
  // original: <image src="../images/fish3.gif">Fresh Water fish from Japan  
  public String transposeDescription(String descIn) {
    String desc = descIn;
    int index = descIn.indexOf("../");
    if (index != -1)
      desc = descIn.subSequence(0, index) + "/jpetstore/" + descIn.substring(index + 3);
    return desc;        
  }
}
