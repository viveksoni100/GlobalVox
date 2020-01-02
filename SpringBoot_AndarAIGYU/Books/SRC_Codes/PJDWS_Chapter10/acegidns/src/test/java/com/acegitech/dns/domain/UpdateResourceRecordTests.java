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
 * com.acegitech.dns.domain.DomainManager#updateResourceRecord(ResourceRecord)}.
 *
 * @author Ben Alex
 * @version $Id: UpdateResourceRecordTests.java,v 1.1 2004/11/18 06:55:17 administrator Exp $
 */
public class UpdateResourceRecordTests extends AbstractDomainManagerTest {
    //~ Methods ================================================================

    public void testUpdateAsPeterWhenPermittedViaDeepInheritence() {
        makeActiveUser("peter");

        ResourceRecord rr = getResourceRecord("www", ResourceRecord.TYPE_A,
                getDomain("compsci.science.zoo-uni.edu.zz."));

        // has permission via RWCD grant to science.zoo-uni.edu.zz
        rr.setData("192.168.0.123");

        int expectedVersion = rr.getZone().getSerial() + 1;

        rr = domainManager.updateResourceRecord(rr);
        assertEquals(expectedVersion, rr.getZone().getSerial());
    }

    public void testUpdateAsScottWhenNotPermitted() {
        makeActiveUser("peter"); // to get Resource Record

        ResourceRecord rr = getResourceRecord("www", ResourceRecord.TYPE_A,
                getDomain("petes.com.zz."));

        makeActiveUser("scott");
        rr.setData("192.168.0.243");

        try {
            domainManager.updateResourceRecord(rr);
            fail(
                "Should have been rejected as scott does not have Write permission to petes.com.zz parent domain");
        } catch (AccessDeniedException expected) {
            assertTrue(true);
        }
    }
}
