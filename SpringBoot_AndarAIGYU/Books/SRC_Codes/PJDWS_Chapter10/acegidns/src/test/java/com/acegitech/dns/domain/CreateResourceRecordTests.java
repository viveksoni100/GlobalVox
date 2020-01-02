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
 * com.acegitech.dns.domain.DomainManager#createResourceRecord(ResourceRecord)}.
 *
 * @author Ben Alex
 * @version $Id: CreateResourceRecordTests.java,v 1.1 2004/11/18 06:55:18 administrator Exp $
 */
public class CreateResourceRecordTests extends AbstractDomainManagerTest {
    //~ Methods ================================================================

    public void testCreateAsPeterWhenPermittedViaDeepInheritence() {
        makeActiveUser("peter");

        Domain parent = getDomain("compsci.science.zoo-uni.edu.zz.");

        ResourceRecord rr = new ResourceRecord(parent.getStartOfAuthority());
        rr.setName(parent.getFullDomainName()); // mail for the domain
        rr.setAux(10);
        rr.setType(ResourceRecord.TYPE_MX);
        rr.setData("mail.springhost.com.zz.");
        assertEquals(-1, rr.getId());

        // has permission via RWCD grant to science.zoo-uni.edu.zz
        rr = domainManager.createResourceRecord(rr);
        assertTrue(rr.getId() != -1);

        // Test it was really created
        ResourceRecord loadedRr = getResourceRecord(parent.getFullDomainName(),
                ResourceRecord.TYPE_MX, parent);
        assertEquals(rr.getId(), loadedRr.getId());
    }

    public void testCreateAsScottWhenNotPermitted() {
        makeActiveUser("peter"); // to get Domain

        Domain parent = getDomain("petes.com.zz.");

        makeActiveUser("scott");

        ResourceRecord rr = new ResourceRecord(parent.getStartOfAuthority());
        rr.setName(parent.getFullDomainName()); // mail for the domain
        rr.setAux(10);
        rr.setType(ResourceRecord.TYPE_MX);
        rr.setData("mail.springhost.com.zz.");

        try {
            rr = domainManager.createResourceRecord(rr);
            fail(
                "Should have been rejected as scott does not have Create permission to petes.com.zz parent domain");
        } catch (AccessDeniedException expected) {
            assertTrue(true);
        }
    }

    public void testCreateAsScottWhenPermitted() {
        makeActiveUser("scott");

        Domain parent = getDomain("scotty.com.zz.");

        ResourceRecord rr = new ResourceRecord(parent.getStartOfAuthority());
        rr.setName(parent.getFullDomainName()); // mail for the domain
        rr.setAux(10);
        rr.setType(ResourceRecord.TYPE_MX);
        rr.setData("mail.springhost.com.zz.");
        assertEquals(-1, rr.getId());

        rr = domainManager.createResourceRecord(rr);
        assertTrue(rr.getId() != -1);

        // Test it was really created
        ResourceRecord loadedRr = getResourceRecord(parent.getFullDomainName(),
                ResourceRecord.TYPE_MX, parent);
        assertEquals(rr.getId(), loadedRr.getId());
    }
}
