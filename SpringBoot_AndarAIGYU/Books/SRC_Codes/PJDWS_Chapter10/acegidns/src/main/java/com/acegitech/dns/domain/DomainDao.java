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
 * Data access object for {@link Domain}, its contained {@link StartOfAuthority}
 * and {@link ResourceRecord}.
 *
 * @author Ben Alex
 * @version $Id: DomainDao.java,v 1.1 2004/11/18 06:55:19 administrator Exp $
 */
public interface DomainDao {
    //~ Methods ================================================================

    public Domain createDomain(Domain domain);

    public ResourceRecord createResourceRecord(ResourceRecord rr);

    public void deleteDomain(Domain domain);

    public void deleteResourceRecord(ResourceRecord rr);

    public List findAllDomainsLike(String domainName);

    public List findAllResourceRecordsInDomain(Domain domain);

    public Domain readDomainById(Integer domainId);

    public Domain updateDomain(Domain domain);

    public ResourceRecord updateResourceRecord(ResourceRecord rr);
}
