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
 * com.acegitech.dns.domain.DomainManager#findAllDomainsLike(String)}.
 *
 * @author Ben Alex
 * @version $Id: FindAllDomainsLikeTests.java,v 1.1 2004/11/18 06:55:17 administrator Exp $
 */
public class FindAllDomainsLikeTests extends AbstractDomainManagerTest {
    //~ Methods ================================================================

    public void testFindAllDomainsLikeAsDianne() {
        makeActiveUser("dianne"); // has ROLE_STAFF

        List domains = domainManager.findAllDomainsLike("");
        assertNotContainsDomain("scotty.com.zz.", domains);
        assertEquals(8, domains.size()); // all domains except scotty.com.zz.
    }

    public void testFindAllDomainsLikeAsMarissa() {
        makeActiveUser("marissa"); // has ROLE_STAFF

        List domains = domainManager.findAllDomainsLike("");
        assertNotContainsDomain("scotty.com.zz.", domains);
        assertEquals(8, domains.size()); // all domains except scotty.com.zz.
    }

    public void testFindAllDomainsLikeAsPeter() {
        makeActiveUser("peter");

        List domains = domainManager.findAllDomainsLike("");
        assertContainsDomain("petes.com.zz.", domains); // explict grant (A:1)
        assertNotContainsDomain("scotty.com.zz.", domains);
        assertNotContainsDomain("jackpot.com.zz.", domains);
        assertNotContainsDomain("zoo-uni.edu.zz.", domains);
        assertContainsDomain("science.zoo-uni.edu.zz.", domains); // explict grant (RWCD:30)
        assertContainsDomain("compsci.science.zoo-uni.edu.zz.", domains);
        assertNotContainsDomain("arts.zoo-uni.edu.zz.", domains);
        assertNotContainsDomain("tafe.edu.zz.", domains);
        assertNotContainsDomain("zoohigh.edu.zz.", domains);
        assertTrue(true);
    }

    public void testFindAllDomainsLikeAsScott() {
        makeActiveUser("scott");

        List domains = domainManager.findAllDomainsLike("");
        assertNotContainsDomain("petes.com.zz.", domains);
        assertContainsDomain("scotty.com.zz.", domains); // explict grant (A:1)
        assertNotContainsDomain("jackpot.com.zz.", domains);
        assertNotContainsDomain("zoo-uni.edu.zz.", domains);
        assertNotContainsDomain("science.zoo-uni.edu.zz.", domains);
        assertNotContainsDomain("compsci.science.zoo-uni.edu.zz.", domains);
        assertNotContainsDomain("arts.zoo-uni.edu.zz.", domains);
        assertContainsDomain("tafe.edu.zz.", domains); // explict grant (RW:6)
        assertNotContainsDomain("zoohigh.edu.zz.", domains);
        assertTrue(true);
    }
}
