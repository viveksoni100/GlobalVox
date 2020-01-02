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

import org.springframework.dao.DataRetrievalFailureException;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.sql.DataSource;


/**
 * Base implementation of {@link DomainDao} that uses Spring JDBC services.
 *
 * @author Ben Alex
 * @version $Id: DomainDaoSpring.java,v 1.1 2004/11/18 06:55:18 administrator Exp $
 */
public class DomainDaoSpring extends JdbcDaoSupport implements DomainDao {
    //~ Instance fields ========================================================

    private DomainByIdQuery domainByIdQuery;
    private DomainDelete domainDelete;
    private DomainInsert domainInsert;
    private DomainLikeNameQuery domainLikeNameQuery;
    private ResourceRecordDelete resourceRecordDelete;
    private ResourceRecordInsert resourceRecordInsert;
    private ResourceRecordUpdate resourceRecordUpdate;
    private ResourceRecordsWithZoneQuery resourceRecordsWithZoneQuery;
    private StartOfAuthorityDelete startOfAuthorityDelete;
    private StartOfAuthorityInsert startOfAuthorityInsert;
    private StartOfAuthorityUpdate startOfAuthorityUpdate;
    private StartOfAuthorityWithIdQuery startOfAuthorityWithIdQuery;

    //~ Methods ================================================================

    public Domain createDomain(Domain domain) {
        Domain result = domainInsert.insert(domain);
        startOfAuthorityInsert.insert(domain.getStartOfAuthority());

        return result;
    }

    public ResourceRecord createResourceRecord(ResourceRecord rr) {
        return resourceRecordInsert.insert(rr);
    }

    public void deleteDomain(Domain domain) {
        startOfAuthorityDelete.delete(domain.getId());
        domainDelete.delete(domain.getId());
    }

    public void deleteResourceRecord(ResourceRecord rr) {
        resourceRecordDelete.delete(rr.getId());
    }

    public List findAllDomainsLike(String domainName) {
        // List of DomainDetailsHolder
        Iterator ddhIter = domainLikeNameQuery.execute("%" + domainName + "%")
                                              .iterator();

        List result = new Vector();

        while (ddhIter.hasNext()) {
            DomainDetailsHolder ddh = (DomainDetailsHolder) ddhIter.next();
            result.add(buildDomainIncludingParents(ddh));
        }

        return result;
    }

    public List findAllResourceRecordsInDomain(Domain domain) {
        Iterator rrhIter = resourceRecordsWithZoneQuery.execute(domain.getId())
                                                       .iterator();
        List result = new Vector();

        while (rrhIter.hasNext()) {
            ResourceRecordHolder rrh = (ResourceRecordHolder) rrhIter.next();

            // Lookup Domain associated with ResourceRecord's StartOfAuthority zone
            // We could have just used the Domain argument, but we want to ensure
            // the returned ResourceRecord has all StartOfAuthority and Domains populated
            List ddhList = domainByIdQuery.execute(rrh.zone);

            if (ddhList.size() == 0) {
                throw new DataRetrievalFailureException(
                    "Unable to retrieve ResourceRecord because the ResourceRecord.zone: "
                    + rrh.zone + " does not have a corresponding Domain record");
            }

            DomainDetailsHolder ddh = (DomainDetailsHolder) ddhList.get(0);

            Domain loadedDomain = buildDomainIncludingParents(ddh);

            ResourceRecord rr = new ResourceRecord(loadedDomain
                    .getStartOfAuthority());
            rr.setName(rrh.name);
            rr.setType(rrh.type);
            rr.setData(rrh.data);
            rr.setAux(rrh.aux);
            rr.setTtl(rrh.ttl);
            rr.setId(rrh.id);

            result.add(rr);
        }

        return result;
    }

    public Domain readDomainById(Integer domainId) {
        List list = domainByIdQuery.execute(domainId.intValue());

        if (list.size() == 0) {
            throw new DataRetrievalFailureException(
                "Domain with requested id: " + domainId + " could not be found");
        }

        DomainDetailsHolder ddh = (DomainDetailsHolder) list.get(0);

        return buildDomainIncludingParents(ddh);
    }

    public Domain updateDomain(Domain domain) {
        // Domain itself is immutable, but its StartOfAuthority is not
        startOfAuthorityUpdate.update(domain.getStartOfAuthority());

        return domain;
    }

    public ResourceRecord updateResourceRecord(ResourceRecord rr) {
        return resourceRecordUpdate.update(rr);
    }

    protected void initDao() throws Exception {
        domainInsert = new DomainInsert(getDataSource());
        domainDelete = new DomainDelete(getDataSource());
        domainLikeNameQuery = new DomainLikeNameQuery(getDataSource());
        domainByIdQuery = new DomainByIdQuery(getDataSource());
        resourceRecordInsert = new ResourceRecordInsert(getDataSource());
        resourceRecordUpdate = new ResourceRecordUpdate(getDataSource());
        resourceRecordDelete = new ResourceRecordDelete(getDataSource());
        resourceRecordsWithZoneQuery = new ResourceRecordsWithZoneQuery(getDataSource());
        startOfAuthorityInsert = new StartOfAuthorityInsert(getDataSource());
        startOfAuthorityUpdate = new StartOfAuthorityUpdate(getDataSource());
        startOfAuthorityDelete = new StartOfAuthorityDelete(getDataSource());
        startOfAuthorityWithIdQuery = new StartOfAuthorityWithIdQuery(getDataSource());
    }

    /**
     * Returns a StartOfAuthority <b>that does not contain a
     * <code>Domain</code> property</b>.
     *
     * @param soaId to lookup
     *
     * @return the start of authority (without a null domain property)
     *
     * @throws DataRetrievalFailureException DOCUMENT ME!
     */
    private StartOfAuthority getStartOfAuthorityWithoutDomain(int soaId) {
        List soaHolderList = startOfAuthorityWithIdQuery.execute(soaId);

        if (soaHolderList.size() == 0) {
            throw new DataRetrievalFailureException(
                "StartOfAuthority with id: " + soaId + " not found");
        }

        StartOfAuthorityHolder soaHolder = (StartOfAuthorityHolder) soaHolderList
            .get(0);

        // Generate StartOfAuthority
        StartOfAuthority soa = new StartOfAuthority();
        soa.setId(soaHolder.id);

        // soa.setOrigin not required (see StartOfAuthority constructor)
        soa.setNs(soaHolder.ns);
        soa.setMbox(soaHolder.mbox);
        soa.setSerial(soaHolder.serial);
        soa.setRefresh(soaHolder.refresh);
        soa.setRetry(soaHolder.retry);
        soa.setExpire(soaHolder.expire);
        soa.setMinimum(soaHolder.minimum);
        soa.setTtl(soaHolder.ttl);

        return soa;
    }

    private Domain buildDomainIncludingParents(DomainDetailsHolder ddh) {
        Domain parent = null;

        if (ddh.parentId != 0) {
            // This domain has a parent, so look it up
            List parentDdhList = domainByIdQuery.execute(ddh.parentId);

            if (parentDdhList.size() == 0) {
                throw new DataRetrievalFailureException("Domain with id: "
                    + ddh.id + " and parent: " + ddh.parentId
                    + " could not be retrieved as parent could not be found");
            }

            DomainDetailsHolder parentDdh = (DomainDetailsHolder) parentDdhList
                .get(0);
            parent = buildDomainIncludingParents(parentDdh);
        }

        Domain domain = new Domain(ddh.id, parent, ddh.fullName);

        // Lookup StartOfAuthority
        StartOfAuthority soa = getStartOfAuthorityWithoutDomain(ddh.id);
        soa.setDomain(domain);
        domain.setStartOfAuthority(soa);

        return domain;
    }

    private String makeObjectIdentity(Domain domain) {
        return domain.getClass().getName() + ":" + domain.getId();
    }

    private String makeObjectIdentity(ResourceRecord rr) {
        return rr.getClass().getName() + ":" + rr.getId();
    }

    //~ Inner Classes ==========================================================

    /**
     * Returns a <code>DomainDetailsHolder</code>s for the domain with the
     * indicated <code>id</code>.
     */
    protected class DomainByIdQuery extends MappingSqlQuery {
        protected DomainByIdQuery(DataSource ds) {
            super(ds,
                "SELECT id, parent_domain_id, full_name FROM domain WHERE id = ?");
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }

        protected Object mapRow(ResultSet rs, int rownum)
            throws SQLException {
            DomainDetailsHolder ddh = new DomainDetailsHolder();
            ddh.id = rs.getInt("id");
            ddh.parentId = rs.getInt("parent_domain_id");
            ddh.fullName = rs.getString("full_name");

            return ddh;
        }
    }

    protected class DomainDelete extends SqlUpdate {
        protected DomainDelete(DataSource ds) {
            super(ds, "DELETE FROM domain WHERE id = ?");
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }

        protected void delete(int domainId) {
            super.update(domainId);
        }
    }

    protected class DomainDetailsHolder {
        public String fullName;
        public int id;
        public int parentId;
    }

    protected class DomainInsert extends SqlUpdate {
        protected DomainInsert(DataSource ds) {
            super(ds,
                "insert into domain (id, parent_domain_id, full_name) values (null, ?, ?)");
            declareParameter(new SqlParameter(Types.INTEGER));
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        protected Domain insert(Domain domain) {
            if (domain.getParent() == null) {
                Object[] objs = {null, domain.getFullDomainName()};
                super.update(objs);
            } else {
                Object[] objs = {new Integer(domain.getParent().getId()), domain
                        .getFullDomainName()};
                super.update(objs);
            }

            int id = getJdbcTemplate().queryForInt("call identity()");

            domain.setId(id);
            domain.getStartOfAuthority().setId(id);

            return domain;
        }
    }

    /**
     * Returns a <code>DomainDetailsHolder</code>s for each located domain.
     */
    protected class DomainLikeNameQuery extends MappingSqlQuery {
        protected DomainLikeNameQuery(DataSource ds) {
            super(ds,
                "SELECT id, parent_domain_id, full_name FROM domain WHERE full_name like ? ORDER BY full_name");
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        protected Object mapRow(ResultSet rs, int rownum)
            throws SQLException {
            DomainDetailsHolder ddh = new DomainDetailsHolder();
            ddh.id = rs.getInt("id");
            ddh.parentId = rs.getInt("parent_domain_id");
            ddh.fullName = rs.getString("full_name");

            return ddh;
        }
    }

    protected class ResourceRecordDelete extends SqlUpdate {
        protected ResourceRecordDelete(DataSource ds) {
            super(ds, "DELETE FROM rr WHERE id = ?");
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }

        protected void delete(int resourceRecordId) {
            super.update(resourceRecordId);
        }
    }

    protected class ResourceRecordHolder {
        public String data;
        public String name;
        public String type;
        public int aux;
        public int id;
        public int ttl;
        public int zone;
    }

    protected class ResourceRecordInsert extends SqlUpdate {
        protected ResourceRecordInsert(DataSource ds) {
            super(ds,
                "insert into rr (id, zone, name, type, data, aux, ttl) values (null, ?, ?, ?, ?, ?, ?)");
            declareParameter(new SqlParameter(Types.INTEGER)); // zone
            declareParameter(new SqlParameter(Types.VARCHAR)); // name
            declareParameter(new SqlParameter(Types.VARCHAR)); // type
            declareParameter(new SqlParameter(Types.VARCHAR)); // data
            declareParameter(new SqlParameter(Types.INTEGER)); // aux
            declareParameter(new SqlParameter(Types.INTEGER)); // ttl
            compile();
        }

        protected ResourceRecord insert(ResourceRecord rr) {
            Object[] objs = {new Integer(rr.getZone().getId()), rr.getName(), rr
                    .getType(), rr.getData(), new Integer(rr.getAux()), new Integer(rr
                        .getTtl()),};

            super.update(objs);

            int id = getJdbcTemplate().queryForInt("call identity()");
            rr.setId(id);

            return rr;
        }
    }

    protected class ResourceRecordUpdate extends SqlUpdate {
        protected ResourceRecordUpdate(DataSource ds) {
            // id and zone are immutable and thus not updated
            super(ds,
                "UPDATE rr SET name = ?, type = ?, data = ?, aux = ?, ttl = ? WHERE id = ?");
            declareParameter(new SqlParameter(Types.VARCHAR)); // name
            declareParameter(new SqlParameter(Types.VARCHAR)); // type
            declareParameter(new SqlParameter(Types.VARCHAR)); // data
            declareParameter(new SqlParameter(Types.INTEGER)); // aux
            declareParameter(new SqlParameter(Types.INTEGER)); // ttl
            declareParameter(new SqlParameter(Types.INTEGER)); // id
            compile();
        }

        protected ResourceRecord update(ResourceRecord rr) {
            Object[] objs = {rr.getName(), rr.getType(), rr.getData(), new Integer(rr
                        .getAux()), new Integer(rr.getTtl()), new Integer(rr
                        .getId())};

            super.update(objs);

            return rr;
        }
    }

    /**
     * Returns a <code>ResourceRecordHolder</code>s for each located
     * <code>ResourceRecord</code> in the specified RR.ZONE (which is equal to
     * the specified SOA.ID and the DOMAIN.ID).
     */
    protected class ResourceRecordsWithZoneQuery extends MappingSqlQuery {
        protected ResourceRecordsWithZoneQuery(DataSource ds) {
            super(ds,
                "SELECT id, zone, name, type, data, aux, ttl FROM rr WHERE zone = ? ORDER BY type, name");
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }

        protected Object mapRow(ResultSet rs, int rownum)
            throws SQLException {
            ResourceRecordHolder rrh = new ResourceRecordHolder();
            rrh.id = rs.getInt("id");
            rrh.zone = rs.getInt("zone");
            rrh.name = rs.getString("name");
            rrh.type = rs.getString("type");
            rrh.data = rs.getString("data");
            rrh.aux = rs.getInt("aux");
            rrh.ttl = rs.getInt("ttl");

            return rrh;
        }
    }

    protected class StartOfAuthorityDelete extends SqlUpdate {
        protected StartOfAuthorityDelete(DataSource ds) {
            super(ds, "DELETE FROM soa WHERE id = ?");
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }

        protected void delete(int startOfAuthorityId) {
            super.update(startOfAuthorityId);
        }
    }

    protected class StartOfAuthorityHolder {
        public String mbox;
        public String ns;
        public String origin;
        public int domain;
        public int expire;
        public int id;
        public int minimum;
        public int refresh;
        public int retry;
        public int serial;
        public int ttl;
    }

    protected class StartOfAuthorityInsert extends SqlUpdate {
        protected StartOfAuthorityInsert(DataSource ds) {
            super(ds,
                "insert into soa (id, origin, ns, mbox, serial, refresh, retry, expire, minimum, ttl) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            declareParameter(new SqlParameter(Types.INTEGER)); // id
            declareParameter(new SqlParameter(Types.VARCHAR)); // origin
            declareParameter(new SqlParameter(Types.VARCHAR)); // ns
            declareParameter(new SqlParameter(Types.VARCHAR)); // mbox
            declareParameter(new SqlParameter(Types.INTEGER)); // serial
            declareParameter(new SqlParameter(Types.INTEGER)); // refresh
            declareParameter(new SqlParameter(Types.INTEGER)); // retry
            declareParameter(new SqlParameter(Types.INTEGER)); // expire
            declareParameter(new SqlParameter(Types.INTEGER)); // minimum
            declareParameter(new SqlParameter(Types.INTEGER)); // ttl
            compile();
        }

        protected StartOfAuthority insert(StartOfAuthority soa) {
            Object[] objs = {new Integer(soa.getDomain().getId()), soa
                    .getOrigin(), soa.getNs(), soa.getMbox(), new Integer(soa
                        .getSerial()), new Integer(soa.getRefresh()), new Integer(soa
                        .getRetry()), new Integer(soa.getExpire()), new Integer(soa
                        .getMinimum()), new Integer(soa.getTtl())};

            super.update(objs);

            return soa;
        }
    }

    protected class StartOfAuthorityUpdate extends SqlUpdate {
        protected StartOfAuthorityUpdate(DataSource ds) {
            super(ds,
                "UPDATE soa SET ns = ?, mbox = ?, serial = ?, refresh = ?, retry = ?, expire = ?, minimum = ?, ttl = ? WHERE id = ?");
            declareParameter(new SqlParameter(Types.VARCHAR)); // ns
            declareParameter(new SqlParameter(Types.VARCHAR)); // mbox
            declareParameter(new SqlParameter(Types.INTEGER)); // serial
            declareParameter(new SqlParameter(Types.INTEGER)); // refresh
            declareParameter(new SqlParameter(Types.INTEGER)); // retry
            declareParameter(new SqlParameter(Types.INTEGER)); // expire
            declareParameter(new SqlParameter(Types.INTEGER)); // minimum
            declareParameter(new SqlParameter(Types.INTEGER)); // ttl
            declareParameter(new SqlParameter(Types.INTEGER)); // id
            compile();
        }

        protected StartOfAuthority update(StartOfAuthority soa) {
            // id and origin are immutable and thus not updated
            Object[] objs = {soa.getNs(), soa.getMbox(), new Integer(soa
                        .getSerial()), new Integer(soa.getRefresh()), new Integer(soa
                        .getRetry()), new Integer(soa.getExpire()), new Integer(soa
                        .getMinimum()), new Integer(soa.getTtl()), new Integer(soa
                        .getId())};

            super.update(objs);

            return soa;
        }
    }

    /**
     * Returns a <code>StartOfAuthorityHolder</code>s for the located
     * <code>StartOfAuthority</code> with the specified SOA.ID.
     */
    protected class StartOfAuthorityWithIdQuery extends MappingSqlQuery {
        protected StartOfAuthorityWithIdQuery(DataSource ds) {
            super(ds,
                "SELECT id, origin, ns, mbox, serial, refresh, retry, expire, minimum, ttl FROM soa WHERE id = ?");
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }

        protected Object mapRow(ResultSet rs, int rownum)
            throws SQLException {
            StartOfAuthorityHolder soah = new StartOfAuthorityHolder();
            soah.id = rs.getInt("id");
            soah.origin = rs.getString("origin");
            soah.ns = rs.getString("ns");
            soah.mbox = rs.getString("mbox");
            soah.serial = rs.getInt("serial");
            soah.refresh = rs.getInt("refresh");
            soah.retry = rs.getInt("retry");
            soah.expire = rs.getInt("expire");
            soah.minimum = rs.getInt("minimum");
            soah.ttl = rs.getInt("ttl");

            return soah;
        }
    }
}
