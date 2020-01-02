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
 * Represents a start of authority record compatible with MyDNS
 * (http://mydns.bboy.net/).
 * 
 * <p>
 * The <code>id</code> of the <code>Domain</code> is used for the identity of
 * the child <code>StartOfAuthority</code>. This is just a convenient linkage
 * given there is a one-to-one relationship between the two.
 * </p>
 *
 * @author Ben Alex
 * @version $Id: StartOfAuthority.java,v 1.1 2004/11/18 06:55:18 administrator Exp $
 */
public class StartOfAuthority {
    //~ Instance fields ========================================================

    private Domain domain;
    private String mbox;
    private String ns;
    private String origin;
    private int expire = 604800;
    private int id = -1;
    private int minimum = 86400;
    private int refresh = 28800;
    private int retry = 7200;
    private int serial = 1;
    private int ttl = 86400;

    //~ Constructors ===========================================================

    public StartOfAuthority(Domain domain, String ns, String mbox) {
        setDomain(domain);
        setOrigin(domain.getFullDomainName());
        setNs(ns);
        setMbox(mbox);
    }

    /**
     * Package protected for use by service layer only.
     */
    StartOfAuthority() {}

    //~ Methods ================================================================

    public Domain getDomain() {
        return domain;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getExpire() {
        return expire;
    }

    public int getId() {
        return id;
    }

    public void setMbox(String mbox) {
        if ((mbox == null) || "".equals(mbox)) {
            throw new IllegalArgumentException("mailBox mandatory");
        }

        this.mbox = mbox;
    }

    public String getMbox() {
        return mbox;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setNs(String ns) {
        if ((ns == null) || "".equals(ns)) {
            throw new IllegalArgumentException("nameServer mandatory");
        }

        this.ns = ns;
    }

    public String getNs() {
        return ns;
    }

    public String getOrigin() {
        return origin;
    }

    public void setRefresh(int refresh) {
        this.refresh = refresh;
    }

    public int getRefresh() {
        return refresh;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public int getRetry() {
        return retry;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public int getSerial() {
        return serial;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public int getTtl() {
        return ttl;
    }

    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * Package protected for service layer to change.
     *
     * @param domain the domain
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    void setDomain(Domain domain) {
        if (domain == null) {
            throw new IllegalArgumentException("domain mandatory");
        }

        this.domain = domain;
    }

    /**
     * Package protected so only persistence layer can change.
     *
     * @param id to set it to
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    void setId(int id) {
        if (this.id != -1) {
            throw new IllegalArgumentException("id is immutable!");
        }

        this.id = id;
    }

    private void setOrigin(String origin) {
        if ((origin == null) || "".equals(origin)) {
            throw new IllegalArgumentException("origin mandatory");
        }

        this.origin = origin;
    }
}
