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

import net.sf.acegisecurity.acl.basic.AclObjectIdentity;
import net.sf.acegisecurity.acl.basic.BasicAclExtendedDao;
import net.sf.acegisecurity.acl.basic.NamedEntityObjectIdentity;
import net.sf.acegisecurity.acl.basic.SimpleAclEntry;
import net.sf.acegisecurity.context.ContextHolder;
import net.sf.acegisecurity.context.SecureContext;

import org.springframework.beans.factory.InitializingBean;

import java.util.Iterator;
import java.util.List;


/**
 * Base implementation of {@link DomainManager}.
 *
 * @author Ben Alex
 * @version $Id: DomainManagerImpl.java,v 1.1 2004/11/18 06:55:19 administrator Exp $
 */
public class DomainManagerImpl implements DomainManager, InitializingBean {
    //~ Instance fields ========================================================

    private BasicAclExtendedDao basicAclExtendedDao;
    private DomainDao domainDao;

    //~ Methods ================================================================

    public void setBasicAclExtendedDao(BasicAclExtendedDao basicAclExtendedDao) {
        this.basicAclExtendedDao = basicAclExtendedDao;
    }

    public BasicAclExtendedDao getBasicAclExtendedDao() {
        return basicAclExtendedDao;
    }

    public void setDomainDao(DomainDao domainDao) {
        this.domainDao = domainDao;
    }

    public DomainDao getDomainDao() {
        return domainDao;
    }

    public void afterPropertiesSet() throws Exception {
        if (this.domainDao == null) {
            throw new IllegalArgumentException("domainDao required");
        }

        if (this.basicAclExtendedDao == null) {
            throw new IllegalArgumentException("basicAclExtendedDao required");
        }
    }

    public Domain createDomain(Domain domain) {
        // Create the Domain, including the nested StartOfAuthority
        Domain result = domainDao.createDomain(domain);

        // Assign current user Administrative permission to Domain
        basicAclExtendedDao.create(makeSimpleAclEntryWithAdministrationPermission(
                domain));

        return result;
    }

    public ResourceRecord createResourceRecord(ResourceRecord rr) {
        // Create the Domain, including the nested StartOfAuthority
        ResourceRecord result = domainDao.createResourceRecord(rr);

        // Ensure a link exists between the parent ACL (Domain)
        // and the child ACL (ResourceRecord) 
        SimpleAclEntry simpleAcl = new SimpleAclEntry();
        simpleAcl.setAclObjectIdentity(makeObjectIdentity(rr));
        simpleAcl.setAclObjectParentIdentity(makeObjectIdentity(
                rr.getZone().getDomain()));
        basicAclExtendedDao.create(simpleAcl);

        return result;
    }

    public void deleteDomain(Domain domain) {
        // Locate and delete any nested ResourceRecords
        Iterator iter = findAllResourceRecordsInDomain(domain).iterator();

        while (iter.hasNext()) {
            ResourceRecord rr = (ResourceRecord) iter.next();
            deleteResourceRecord(rr);
        }

        // Delete the domain, including the nested StartOfAuthority
        domainDao.deleteDomain(domain);

        // Delete the AclObjectIdentity and related AclPermissions
        basicAclExtendedDao.delete(makeObjectIdentity(domain));
    }

    public void deleteResourceRecord(ResourceRecord rr) {
        // Delete the resource record
        domainDao.deleteResourceRecord(rr);

        // Delete the AclObjectIdentity
        basicAclExtendedDao.delete(makeObjectIdentity(rr));
    }

    public List findAllDomainsLike(String domainName) {
        return domainDao.findAllDomainsLike(domainName);
    }

    public List findAllResourceRecordsInDomain(Domain domain) {
        return domainDao.findAllResourceRecordsInDomain(domain);
    }

    public void obtainAdministrativePermission(Integer domainId,
        String recipient) {
        // Lookup the requested Domain
        Domain domain = domainDao.readDomainById(domainId);

        // Remove any existing recipient permissions against the Domain
        basicAclExtendedDao.delete(makeObjectIdentity(domain), recipient);

        // Assign current user Administrative permission to Domain
        basicAclExtendedDao.create(makeSimpleAclEntryWithAdministrationPermission(
                domain, recipient));
    }

    public Domain updateDomain(Domain domain) {
        int newSerial = domain.getStartOfAuthority().getSerial() + 1;
        domain.getStartOfAuthority().setSerial(newSerial);

        return domainDao.updateDomain(domain);
    }

    public ResourceRecord updateResourceRecord(ResourceRecord rr) {
        // increment the version number (NB: accessing via DomainManager)
        Domain updatedDomain = this.updateDomain(rr.getNestedDomain());

        // perform update now
        ResourceRecord result = domainDao.updateResourceRecord(rr);

        // correct the serial number contained with the ResourceRecord
        result.getZone().setSerial(updatedDomain.getStartOfAuthority()
                                                .getSerial());

        return result;
    }

    protected String getUsername() {
        return ((SecureContext) ContextHolder.getContext()).getAuthentication()
                .getPrincipal().toString();
    }

    private AclObjectIdentity makeObjectIdentity(Domain domain) {
        return new NamedEntityObjectIdentity(domain.getClass().getName(),
            new Integer(domain.getId()).toString());
    }

    private AclObjectIdentity makeObjectIdentity(ResourceRecord rr) {
        return new NamedEntityObjectIdentity(rr.getClass().getName(),
            new Integer(rr.getId()).toString());
    }

    /**
     * Generates a <code>SimpleAclEntry</code> granting the currently logged on
     * user administrative permission to the passed <code>Domain</code>.
     *
     * @param domain that is receiving the ACL entry
     *
     * @return a fully populated <code>SimpleAclEntry</code>
     */
    private SimpleAclEntry makeSimpleAclEntryWithAdministrationPermission(
        Domain domain) {
        return makeSimpleAclEntryWithAdministrationPermission(domain,
            getUsername());
    }

    /**
     * Generates a <code>SimpleAclEntry</code> granting the specifed recipient
     * administrative permission to the passed <code>Domain</code>.
     *
     * @param domain that is receiving the ACL entry
     * @param recipient that is to receive permission
     *
     * @return a fully populated <code>SimpleAclEntry</code>
     */
    private SimpleAclEntry makeSimpleAclEntryWithAdministrationPermission(
        Domain domain, String recipient) {
        SimpleAclEntry simpleAcl = new SimpleAclEntry();
        simpleAcl.setAclObjectIdentity(makeObjectIdentity(domain));

        if (domain.getParent() != null) {
            simpleAcl.setAclObjectParentIdentity(makeObjectIdentity(
                    domain.getParent()));
        }

        simpleAcl.setMask(SimpleAclEntry.ADMINISTRATION);
        simpleAcl.setRecipient(recipient);

        return simpleAcl;
    }
}
