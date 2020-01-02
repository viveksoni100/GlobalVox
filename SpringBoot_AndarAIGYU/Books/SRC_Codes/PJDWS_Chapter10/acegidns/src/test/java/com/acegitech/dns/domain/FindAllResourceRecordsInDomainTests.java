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

import java.util.List;


/**
 * Tests {@link
 * com.acegitech.dns.domain.DomainManager#findAllResourceRecordsInDomain(Domain)}.
 *
 * @author Ben Alex
 * @version $Id: FindAllResourceRecordsInDomainTests.java,v 1.1 2004/11/18 06:55:17 administrator Exp $
 */
public class FindAllResourceRecordsInDomainTests
    extends AbstractDomainManagerTest {
    //~ Methods ================================================================

    public void testFindAllResourceRecordsAsDianne() {
        // same right as with marissa, via ROLE_STAFF on zoo-uni.edu.zz
        makeActiveUser("dianne");

        Domain domain = getDomain("compsci.science.zoo-uni.edu.zz.");
        List rrList = domainManager.findAllResourceRecordsInDomain(domain);
        assertEquals(2, rrList.size());
    }

    public void testFindAllResourceRecordsAsMarissa() {
        // marissa has ACL via ROLE_STAFF on zoo-uni.edu.zz, demonstrating
        // ACLs inherit from zoo-uni.edu.zz to science.zoo-uni.edu.zz to
        // compsci.science.zoo-uni.edu.zz and then to that SOA's
        // resource records
        makeActiveUser("marissa");

        Domain domain = getDomain("compsci.science.zoo-uni.edu.zz.");
        List rrList = domainManager.findAllResourceRecordsInDomain(domain);
        assertEquals(2, rrList.size());
    }

    public void testFindAllResourceRecordsAsPeter() {
        // peter has ACL via direct ACL on science.zoo-uni.edu.zz,
        //  demonstrating ACLs inherit from zoo-uni.edu.zz to
        // science.zoo-uni.edu.zz to compsci.science.zoo-uni.edu.zz
        // and then to that SOA's resource records
        makeActiveUser("peter");

        Domain domain = getDomain("compsci.science.zoo-uni.edu.zz.");
        List rrList = domainManager.findAllResourceRecordsInDomain(domain);
        assertEquals(2, rrList.size());
    }

    public void testFindAllResourceRecordsAsPeterWhenNoPermission() {
        // peter has ACL via on science.zoo-uni.edu.zz but not
        // zoo-uni.edu.zz, demonstrating proper blocking
        makeActiveUser("marissa"); // so we can get the domain itself

        Domain domain = getDomain("zoo-uni.edu.zz.");

        makeActiveUser("peter");

        List rrList = domainManager.findAllResourceRecordsInDomain(domain);
        assertEquals(0, rrList.size());
    }

    public void testFindAllResourceRecordsAsScott() {
        makeActiveUser("marissa"); // so we can get the Domain itself

        Domain domain = getDomain("compsci.science.zoo-uni.edu.zz.");

        makeActiveUser("scott"); // who has no access to the Domain

        List rrList = domainManager.findAllResourceRecordsInDomain(domain);
        assertEquals(0, rrList.size());
    }
}
