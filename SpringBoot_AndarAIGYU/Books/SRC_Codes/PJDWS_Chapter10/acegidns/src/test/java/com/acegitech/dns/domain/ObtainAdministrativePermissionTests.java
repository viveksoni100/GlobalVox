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

import net.sf.acegisecurity.AccessDeniedException;


/**
 * Tests {@link
 * com.acegitech.dns.domain.DomainManager#obtainAdministrativePermission(Domain)}.
 *
 * @author Ben Alex
 * @version $Id: ObtainAdministrativePermissionTests.java,v 1.1 2004/11/18 06:55:18 administrator Exp $
 */
public class ObtainAdministrativePermissionTests
    extends AbstractDomainManagerTest {
    //~ Methods ================================================================

    public void testNonSupervisorUserIsRejected() {
        makeActiveUser("scott"); // so we can obtain the domain's ID

        int domainId = getDomain("scotty.com.zz.").getId();

        makeActiveUser("dianne"); // not holding ROLE_SUPERVISOR

        try {
            domainManager.obtainAdministrativePermission(new Integer(domainId),
                "ROLE_STAFF");
            fail(
                "Should have been rejected as dianne does not have ROLE_SUPERVISOR as required to call the service layer method");
        } catch (AccessDeniedException expected) {
            assertTrue(true);
        }
    }

    public void testSupervisorUserIsAllowed() {
        makeActiveUser("scott"); // so we can obtain the domain's ID

        int domainId = getDomain("scotty.com.zz.").getId();

        makeActiveUser("marissa"); // does hold ROLE_SUPERVISOR

        // ensure we don't have any access at all
        assertNull("Should have been rejected as marissa does not have access to scotty.com.zz.",
            getDomain("scotty.com.zz."));

        // now try to obtain administrative permissions
        domainManager.obtainAdministrativePermission(new Integer(domainId),
            "ROLE_STAFF");

        // now check dianne has access (as a member of ROLE_STAFF)
        makeActiveUser("dianne");
        assertEquals(domainId, getDomain("scotty.com.zz.").getId());
    }
}
