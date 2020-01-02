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
 * Tests {@link com.acegitech.dns.domain.DomainManager#createDomain(Domain)}.
 *
 * @author Ben Alex
 * @version $Id: CreateDomainTests.java,v 1.1 2004/11/18 06:55:18 administrator Exp $
 */
public class CreateDomainTests extends AbstractDomainManagerTest {
    //~ Methods ================================================================

    public void testCreateAsPeterWhenPermittedViaDeepInheritence() {
        makeActiveUser("peter");

        Domain parent = getDomain("compsci.science.zoo-uni.edu.zz.");

        // has permission via RWCD grant to science.zoo-uni.edu.zz
        Domain domain = new Domain(parent, "java", "ns1.springhost.com.zz.",
                "mailbag.springhost.com.zz.");
        domain = domainManager.createDomain(domain);
        assertTrue(domain.getId() != -1);

        Domain reloadedDomain = getDomain(
                "java.compsci.science.zoo-uni.edu.zz.");
        assertEquals(domain.getId(), reloadedDomain.getId());
    }

    public void testCreateAsScottWhenNotPermitted() {
        makeActiveUser("peter"); // to get Domain

        Domain parent = getDomain("petes.com.zz.");

        makeActiveUser("scott");

        Domain domain = new Domain(parent, "toys", "ns1.springhost.com.zz.",
                "mailbag.springhost.com.zz.");

        try {
            domain = domainManager.createDomain(domain);
            fail(
                "Should have been rejected as scott does not have Create permission to petes.com.zz parent domain");
        } catch (AccessDeniedException expected) {
            assertTrue(true);
        }
    }

    public void testCreateAsScottWhenPermitted() {
        makeActiveUser("scott");

        Domain parent = getDomain("scotty.com.zz.");

        Domain domain = new Domain(parent, "belmont", "ns1.springhost.com.zz.",
                "mailbag.springhost.com.zz.");
        assertEquals("belmont.scotty.com.zz.", domain.getFullDomainName());
        assertEquals(-1, domain.getId());

        domain = domainManager.createDomain(domain);
        assertTrue(domain.getId() != -1);

        Domain reloadedDomain = getDomain("belmont.scotty.com.zz.");
        assertEquals(domain.getId(), reloadedDomain.getId());
    }
}
