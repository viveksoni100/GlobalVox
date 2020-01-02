package org.springframework.samples.jpetstore.web.pages;

import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 *  @author Colin Sampaleanu
 **/
public abstract class PetStoreBasePage extends BasePage implements ErrorProperty {

  private IValidationDelegate _validationDelegate;

  public void initialize() {
    _validationDelegate = null;
  }

  public IValidationDelegate getValDelegate() {
    if (_validationDelegate == null)
      _validationDelegate = new PetStoreValidationDelegate();

    return _validationDelegate;
  }

  protected void setErrorField(String componentId, String message) {
    IFormComponent component = (IFormComponent) getComponent(componentId);

    IValidationDelegate delegate = getValDelegate();

    delegate.setFormComponent(component);
    delegate.record(message, null);
  }

  /**
   *  Returns true if the delegate indicates an error, or the error property is not null.
   *
   **/
  protected boolean isInError() {
    return getError() != null || getValDelegate().getHasErrors();
  }

//  non-J2EE version
//  public void pageValidate(PageEvent event) {
//    Visit visit = (Visit) getVisit();
//
//    if (visit != null && visit.isUserLoggedIn())
//      return;
//
//    // User not logged in ... redirect through the Login page.
//
//    Login login = (Login) getRequestCycle().getPage("Login");
//
//    login.setCallback(new PageCallback(this));
//
//    throw new PageRedirectException(login);
//  }
}
