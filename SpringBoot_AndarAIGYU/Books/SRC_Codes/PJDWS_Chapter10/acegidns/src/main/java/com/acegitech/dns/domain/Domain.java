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

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * Models a domain name.
 * 
 * <p>
 * A domain name cannot have its name or parent changed once created. This is
 * because a domain by its nature does not change. It either exists or it
 * expires. Whilst its owner can change, this is accommodated separately via
 * the project's access control list services.
 * </p>
 * 
 * <p>
 * The <code>Domain</code> object is the top-level object for transparent
 * object persistence. Specifically, the <code>Domain</code> must store its
 * {@link StartOfAuthority}.
 * </p>
 * 
 * <p>
 * The object is immutable, except for its {@link #getStartOfAuthority()}
 * object.
 * </p>
 *
 * @author Ben Alex
 * @version $Id: Domain.java,v 1.1 2004/11/18 06:55:18 administrator Exp $
 */
public class Domain {
    //~ Instance fields ========================================================

    public StartOfAuthority startOfAuthority;
    public int id = -1;
    private Domain parent;
    private String fullDomainName;

    //~ Constructors ===========================================================

    /**
     * Construct the <code>Domain</code> object.
     * 
     * <p>
     * The newly constructed <code>Domain</code> will not have an
     * <code>id</code>. This will be assigned by the services layer upon
     * initial passing to the persistence layer.
     * </p>
     *
     * @param parent if applicable, a parent domain (<code>null</code> is valid
     *        but will typically require an administrator permission when
     *        passed to the services layer)
     * @param subDomainName the additional segment of the domain name, such as
     *        "acegi" if the parentDomain represented "com.au" (the segment
     *        name cannot contain any dots)
     * @param ns DOCUMENT ME!
     * @param mbox DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Domain(Domain parent, String subDomainName, String ns, String mbox) {
        setParent(parent);

        if (subDomainName.lastIndexOf(".") != -1) {
            throw new IllegalArgumentException(
                "Dots are prohibited in the subDomainName");
        }

        if (parent == null) {
            setFullDomainName(subDomainName + ".");
        } else {
            setFullDomainName(subDomainName + "." + parent.getFullDomainName());
        }

        setStartOfAuthority(new StartOfAuthority(this, ns, mbox));
    }

    /**
     * Constructor used when generating an existing <code>Domain</code>
     * instance.
     * 
     * <p>
     * Package protected so only persistence tier can modify.
     * </p>
     *
     * @param id identity
     * @param parent the parent domain
     * @param fullDomainName the full name of the domain
     */
    Domain(int id, Domain parent, String fullDomainName) {
        setId(id);
        setParent(parent);
        setFullDomainName(fullDomainName);
    }

    private Domain() {}

    //~ Methods ================================================================

    public String getFullDomainName() {
        return fullDomainName;
    }

    public int getId() {
        return id;
    }

    public Domain getParent() {
        return parent;
    }

    public StartOfAuthority getStartOfAuthority() {
        return startOfAuthority;
    }

    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * Changes the <code>id</code> of the domain object.
     * 
     * <p>
     * Package protected as persistance layer needs to set the <code>id</code>,
     * which should be the same as the <code>StartOfAuthority</code> identity.
     * </p>
     *
     * @param id that uniquely identifies this record
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    void setId(int id) {
        if (this.id != -1) {
            throw new IllegalArgumentException("id is immutable!");
        }

        this.id = id;
    }

    /**
     * Persistence classes are allowed to change the
     * <code>StartOfAuthority</code> record if needed
     *
     * @param startOfAuthority the start of authority
     */
    void setStartOfAuthority(StartOfAuthority startOfAuthority) {
        this.startOfAuthority = startOfAuthority;
    }

    private void setFullDomainName(String fullDomainName) {
        if (fullDomainName.length() > 63) {
            throw new IllegalArgumentException(
                "The requested domain name is invalid as its full name exceeds 63 characters: "
                + this.fullDomainName);
        }

        this.fullDomainName = fullDomainName;
    }

    private void setParent(Domain parent) {
        this.parent = parent;
    }
}
