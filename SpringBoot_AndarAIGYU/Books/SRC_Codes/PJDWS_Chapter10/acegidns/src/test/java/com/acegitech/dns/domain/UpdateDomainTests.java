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
 * Tests {@link com.acegitech.dns.domain.DomainManager#updateDomain(Domain)}.
 *
 * @author Ben Alex
 * @version $Id: UpdateDomainTests.java,v 1.1 2004/11/18 06:55:18 administrator Exp $
 */
public class UpdateDomainTests extends AbstractDomainManagerTest {
    //~ Methods ================================================================

    public void testUpdateAsPeterWhenPermittedViaDeepInheritence() {
        makeActiveUser("peter");

        Domain domain = getDomain("compsci.science.zoo-uni.edu.zz.");
        StartOfAuthority soa = domain.getStartOfAuthority();

        // has permission via RWCD grant to science.zoo-uni.edu.zz
        soa.setNs("dns1.competingisp.net.zz.");

        int expectedVersion = soa.getSerial() + 1;

        domain = domainManager.updateDomain(domain);
        assertEquals(expectedVersion, domain.getStartOfAuthority().getSerial());
    }

    public void testUpdateAsScottWhenNotPermitted() {
        makeActiveUser("peter"); // to get Domain

        Domain domain = getDomain("petes.com.zz.");
        StartOfAuthority soa = domain.getStartOfAuthority();

        makeActiveUser("scott");
        soa.setNs("dns1.competingisp.net.zz.");

        try {
            domainManager.updateDomain(domain);
            fail(
                "Should have been rejected as scott does not have Write permission to petes.com.zz parent domain");
        } catch (AccessDeniedException expected) {
            assertTrue(true);
        }
    }
}
