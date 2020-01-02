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
 * Services layer interface for Acegi DNS.
 *
 * @author Ben Alex
 * @version $Id: DomainManager.java,v 1.1 2004/11/18 06:55:19 administrator Exp $
 */
public interface DomainManager {
    //~ Methods ================================================================

    /**
     * Creates the <code>Domain</code>. The currently logged on user will be
     * granted administration access to the domain, allowing them full
     * permissions. A start of authority record will also be created for this
     * domain.  A <code>Domain</code> is immutable once created.
     *
     * @param domain to be created
     *
     * @return the domain, including an <code>id</code> property
     */
    public Domain createDomain(Domain domain);

    /**
     * Creates the <code>ResourceRecord</code>. Must also create the necessary
     * ACL entries so that the <code>ResourceRecord</code> inherits
     * permissions from the <code>ResourceRecord.getZone().getDomain()</code>.
     *
     * @param rr to create
     *
     * @return the resource record, including an <code>id</code> property
     */
    public ResourceRecord createResourceRecord(ResourceRecord rr);

    /**
     * Deletes the <code>Domain</code> together with any
     * <code>StartOfAuthority</code> records. An implementation may but is not
     * required to automatically delete the associated
     * <code>ResourceRecord</code>s and may throw an exception if related
     * <code>ResourceRecord</code>s exist.
     *
     * @param domain to delete
     */
    public void deleteDomain(Domain domain);

    /**
     * Deletes the specified <code>ResourceRecord</code> and any associated ACL
     * entries.
     *
     * @param rr to delete
     */
    public void deleteResourceRecord(ResourceRecord rr);

    /**
     * Returns a <code>List</code> of {@link Domain}s matching the search
     * criteria. The search criteria should be a word only, and it will be
     * matched using <code>%criteria%</code> against the {@link
     * Domain#getFullDomainName()}.
     *
     * @param domainName the search criteria without any control characters
     *        such as % (can be blank to indicate all domains)
     *
     * @return the matching domains
     */
    public List findAllDomainsLike(String domainName);

    /**
     * Returns a <code>List</code> of {@link ResourceRecord}s belonging to the
     * indicated <code>Domain</code>.
     *
     * @param domain to locate resource records for
     *
     * @return the matching resource records
     */
    public List findAllResourceRecordsInDomain(Domain domain);

    /**
     * Method grants the specified recipient administrative access to the
     * <code>Domain</code>, irrespective of existing ACLs applying to the
     * <code>Domain</code>.
     * 
     * <p>
     * If the recipient already has an explicit permission against the
     * <code>Domain</code>, that permission is deleted so the new
     * administrative level permission will be effective.
     * </p>
     *
     * @param domainId the identity of the <code>Domain</code> the currently
     *        logged on user is requesting administrative permission for
     * @param recipient the principal or role that is to be granted
     *        administrative permission
     */
    public void obtainAdministrativePermission(Integer domainId,
        String recipient);

    /**
     * Used to modify the <code>StartOfAuthority</code> contained with the
     * <code>Domain</code>, as the <code>Domain</code> itself is immutable.
     * The serial number should be incremented by a call to this method.
     *
     * @param domain containg the start of authority to update
     *
     * @return the updated object, with the new version number
     */
    public Domain updateDomain(Domain domain);

    /**
     * Updates the <code>ResourceRecord</code> (although the zone and id are
     * never updated). The method should ensure the related
     * <code>StartOfAuthority</code> serial number is incremented.
     *
     * @param rr to update
     *
     * @return the updated record
     */
    public ResourceRecord updateResourceRecord(ResourceRecord rr);
}
