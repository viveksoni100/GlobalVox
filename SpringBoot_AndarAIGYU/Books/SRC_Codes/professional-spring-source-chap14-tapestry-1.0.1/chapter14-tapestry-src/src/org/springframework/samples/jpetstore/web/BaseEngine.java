
package org.springframework.samples.jpetstore.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.request.RequestContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Custom version of IEngine which puts the Spring ApplicationContext inside the Tapestry
 * Global object
 * 
 * @version $Revision: 1.2 $
 * @author colin
 */
public class BaseEngine extends org.apache.tapestry.engine.BaseEngine {

  public static final String APPLICATION_CONTEXT_KEY = "appContext";

  /* (non-Javadoc)
   * @see org.apache.tapestry.engine.AbstractEngine#setupForRequest(org.apache.tapestry.request.RequestContext)
   */
  protected void setupForRequest(RequestContext context) {
    super.setupForRequest(context);
    
    // insert ApplicationContext in global, if not there
    Map global = (Map) getGlobal();
    ApplicationContext ac = (ApplicationContext) global.get(APPLICATION_CONTEXT_KEY);
    if (ac == null) {
      ac = WebApplicationContextUtils.getWebApplicationContext(context.getServlet().getServletContext());
      global.put(APPLICATION_CONTEXT_KEY, ac);
    }
  }
}
