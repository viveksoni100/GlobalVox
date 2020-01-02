/* Copyright 2004 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.acegitech.dns.domain;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.context.ContextHolder;
import net.sf.acegisecurity.context.SecureContextImpl;
import net.sf.acegisecurity.providers.UsernamePasswordAuthenticationToken;

import org.springframework.test.AbstractTransactionalSpringContextTests;

import java.util.Iterator;
import java.util.List;


/**
 * Provides simplified access to the <code>DomainManager</code> bean and
 * convenience test support methods.
 *
 * @author Ben Alex
 * @version $Id: AbstractDomainManagerTest.java,v 1.1 2004/11/18 06:55:17 administrator Exp $
 */
public abstract class AbstractDomainManagerTest
    extends AbstractTransactionalSpringContextTests {
    //~ Instance fields ========================================================

    protected DomainManager domainManager;

    //~ Methods ================================================================

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public DomainManager getDomainManager() {
        return domainManager;
    }

    protected String[] getConfigLocations() {
        return new String[] {"applicationContext-common-authorization.xml", "applicationContext-common-business.xml", "applicationContext-testing-authentication.xml"};
    }

    /**
     * Locates the first <code>Domain</code> of the exact name specified.
     * 
     * <p>
     * Uses the {@link DomainManager#findAllDomainsLike(String)} method.
     * </p>
     *
     * @param domainName to locate (must be an exact match)
     *
     * @return the domain or <code>null</code> if not found
     */
    protected Domain getDomain(String domainName) {
        List domains = domainManager.findAllDomainsLike("");
        Iterator iter = domains.iterator();

        while (iter.hasNext()) {
            Domain domain = (Domain) iter.next();

            if (domain.getFullDomainName().equals(domainName)) {
                return domain;
            }
        }

        return null;
    }

    /**
     * Locates the first <code>ResourceRecord</code> with the name specified
     * from the <code>Domain</code> specified.
     * 
     * <p>
     * Uses the {@link DomainManager#findAllResourceRecordsInDomain(Domain)}
     * method.
     * </p>
     *
     * @param name to locate (must be an exact match)
     * @param type of resource record to locate (must be an exact match)
     * @param domain containing the resource record (must be an exact match)
     *
     * @return the resource record or <code>null</code> if not found
     */
    protected ResourceRecord getResourceRecord(String name, String type,
        Domain domain) {
        List list = domainManager.findAllResourceRecordsInDomain(domain);
        Iterator iter = list.iterator();

        while (iter.hasNext()) {
            ResourceRecord rr = (ResourceRecord) iter.next();

            if (rr.getName().equals(name) && rr.getType().equals(type)) {
                return rr;
            }
        }

        return null;
    }

    protected void assertContainsDomain(String domainName, List domains) {
        Iterator iter = domains.iterator();

        while (iter.hasNext()) {
            Domain domain = (Domain) iter.next();

            if (domain.getFullDomainName().equals(domainName)) {
                return;
            }
        }

        fail("List of domains should have contained: " + domainName);
    }

    protected void assertNotContainsDomain(String domainName, List domains) {
        Iterator iter = domains.iterator();

        while (iter.hasNext()) {
            Domain domain = (Domain) iter.next();

            if (domain.getFullDomainName().equals(domainName)) {
                fail("List of domains should NOT (but did) contain: "
                    + domainName);
            }
        }
    }

    protected void makeActiveUser(String username) {
        String password = "";

        if ("marissa".equals(username)) {
            password = "koala";
        } else if ("dianne".equals(username)) {
            password = "emu";
        } else if ("scott".equals(username)) {
            password = "wombat";
        } else if ("peter".equals(username)) {
            password = "opal";
        }

        Authentication authRequest = new UsernamePasswordAuthenticationToken(username,
                password);
        SecureContextImpl secureContext = new SecureContextImpl();
        secureContext.setAuthentication(authRequest);
        ContextHolder.setContext(secureContext);
    }

    protected void onTearDownInTransaction() {
        ContextHolder.setContext(null);
    }
}
