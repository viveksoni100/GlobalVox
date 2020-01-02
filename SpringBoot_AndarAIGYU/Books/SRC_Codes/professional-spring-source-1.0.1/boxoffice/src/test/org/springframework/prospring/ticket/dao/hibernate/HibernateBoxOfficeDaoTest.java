
package org.springframework.prospring.ticket.dao.hibernate;

import org.springframework.prospring.ticket.dao.*;
import org.springframework.prospring.ticket.domain.*;
import org.hibernate.*;

/**
 * HibernateBoxOfficeDao Test.
 *
 * @author Uri Boness
 */
public class HibernateBoxOfficeDaoTest extends AbstractBoxOfficeDaoTest {

    protected String[] getConfigLocations() {
        return new String[] { "testApplicationContext.xml" };
    }

    protected BoxOfficeDao createDao() {
        SessionFactory sessionFactory = (SessionFactory)applicationContext.getBean("sessionFactory");
        HibernateBoxOfficeDao dao = new HibernateBoxOfficeDao();
        dao.setSessionFactory(sessionFactory);
        return dao;
    }

    protected void beforeAssertions() {
        ((HibernateBoxOfficeDao)dao).getHibernateTemplate().flush();
    }

    protected void onTearDownInTransaction() {
        super.onTearDownInTransaction();
        clearCache();
    }

    private void clearCache() {
        SessionFactory sessionFactory = ((HibernateBoxOfficeDao)dao).getSessionFactory();
        sessionFactory.evict(Show.class);
        sessionFactory.evict(Performance.class);
        sessionFactory.evict(PriceBand.class);
        sessionFactory.evict(Booking.class);
        sessionFactory.evict(Genre.class);
        sessionFactory.evict(PriceStructure.class);
        sessionFactory.evict(Purchase.class);
        sessionFactory.evict(Seat.class);
        sessionFactory.evict(SeatClass.class);
        sessionFactory.evict(SeatingPlan.class);
        sessionFactory.evict(SeatStatus.class);
    }

}
