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
 * Represents a resource record compatible with MyDNS (http://mydns.bboy.net/).
 * 
 * <p>
 * Every <code>ResourceRecord</code> belongs to a particular
 * <code>StartOfAuthority</code>, as indicated by the {@link #getZone()}
 * method.
 * </p>
 *
 * @author Ben Alex
 * @version $Id: ResourceRecord.java,v 1.1 2004/11/18 06:55:18 administrator Exp $
 */
public class ResourceRecord {
    //~ Static fields/initializers =============================================

    /** Standard "A" host address record */
    public static final String TYPE_A = "A";

    /** Standard IPv6 "A" host address record */
    public static final String TYPE_AAAA = "AAAA";

    /** Server-side alias, as an alternative to a "CNAME" */
    public static final String TYPE_ALIAS = "ALIAS";

    /** Standard "CNAME" canocial name for alias */
    public static final String TYPE_CNAME = "CNAME";

    /** Standard "HINFO" host information */
    public static final String TYPE_HINFO = "HINFO";

    /** Standard "MX" mail exchange record */
    public static final String TYPE_MX = "MX";

    /** Standard "NS" authoritative name server record */
    public static final String TYPE_NS = "NS";

    /**
     * Standard "PTR" domain name pointer record for use with IN-ADDR.ARPA
     * zones
     */
    public static final String TYPE_PTR = "PTR";

    /** Standard "RP" responsible person record */
    public static final String TYPE_RP = "RP";

    /** Standard "SRV" server location (RFC 2782) record */
    public static final String TYPE_SRV = "SRV";

    /** Standard "TXT" text string record */
    public static final String TYPE_TXT = "TXT";

    //~ Instance fields ========================================================

    private StartOfAuthority zone;
    private String data;
    private String name;
    private String type;
    private int aux = 0;
    private int id = -1;
    private int ttl = 86400;

    //~ Constructors ===========================================================

    public ResourceRecord(StartOfAuthority zone) {
        if (zone == null) {
            throw new IllegalArgumentException("zone mandatory");
        }

        this.zone = zone;
    }

    private ResourceRecord() {}

    //~ Methods ================================================================

    public void setAux(int aux) {
        this.aux = aux;
    }

    public int getAux() {
        return aux;
    }

    public void setData(String data) {
        this.data = data;
    }

    /**
     * The IP address or CNAME real machine name of the host specified by the
     * {@link #getName()} method.
     *
     * @return DOCUMENT ME!
     */
    public String getData() {
        return data;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The name of the record in DNS.
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * Convenience accessor for the zone's <code>StartOfAuthority</code>'s
     * <code>Domain</code>
     *
     * @return the <code>Domain</code> this resource record ultimately belongs
     *         in
     */
    public Domain getNestedDomain() {
        return zone.getDomain();
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public int getTtl() {
        return ttl;
    }

    public void setType(String type) {
        if (!type.equals(TYPE_A) && !type.equals(TYPE_AAAA)
            && !type.equals(TYPE_ALIAS) && !type.equals(TYPE_CNAME)
            && !type.equals(TYPE_HINFO) && !type.equals(TYPE_MX)
            && !type.equals(TYPE_NS) && !type.equals(TYPE_PTR)
            && !type.equals(TYPE_RP) && !type.equals(TYPE_SRV)
            && !type.equals(TYPE_TXT)) {
            throw new IllegalArgumentException("Invalid ResourceRecord type: "
                + type);
        }

        this.type = type;
    }

    public String getType() {
        return type;
    }

    public StartOfAuthority getZone() {
        return zone;
    }

    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * Package protected so only persistence layer can change.
     *
     * @param id the new identity
     */
    void setId(int id) {
        this.id = id;
    }

    private void setZone(StartOfAuthority zone) {
        this.zone = zone;
    }
}
