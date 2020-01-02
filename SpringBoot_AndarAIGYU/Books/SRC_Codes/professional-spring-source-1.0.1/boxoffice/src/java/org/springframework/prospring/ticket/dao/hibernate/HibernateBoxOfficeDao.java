package org.springframework.prospring.ticket.dao.hibernate;

import java.math.*;
import java.util.*;

import org.springframework.orm.hibernate3.support.*;
import org.springframework.prospring.ticket.dao.*;
import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.domain.support.*;
import org.springframework.prospring.ticket.service.*;

/**
 * Hibernate implementation of the BoxOfficeDao.
 */
public class HibernateBoxOfficeDao extends HibernateDaoSupport implements BoxOfficeDao {

    /**
     * Marks the given seats for the given performance as reserved for the given booking.     *
     * @param seats The seats to mark as reserved.
     * @param performance The peformance for which the seats are reserved.
     * @param booking The booking for which the seats are reserved.
     * @throws org.springframework.prospring.ticket.service.RequestedSeatNotAvailableException
     *         Thrown if any of the seats are not available anymore, that is, they
     *         are already reserved.
     */
    public void reserveSeats(Seat[] seats, Performance performance, Booking booking)
        throws RequestedSeatNotAvailableException {

        getHibernateTemplate().save(booking);
        List seatStats = getHibernateTemplate().findByNamedQueryAndNamedParam(
            "getSeatStatusForSeatsInPerformance",
            new String[] {"perfId", "ids"},
            new Object[] {new Long(performance.getId()), Arrays.asList(seats)}
        );
        for (Iterator iter = seatStats.iterator(); iter.hasNext();) {
            SeatStatus seatStatus = (SeatStatus)iter.next();
            if (seatStatus.getBooking() != null && (seatStatus.getBooking().getPurchase() != null ||
                seatStatus.getBooking().getReservedUntil().after(new Date()))) {

                throw new RequestedSeatNotAvailableException("Seat " + seatStatus.getSeat().getName() +
                    " is not available for the requested performance", performance);
            }
            seatStatus.setBooking(booking);
            getHibernateTemplate().update(seatStatus);
        }
    }

    /**
     * Returns a list of all performances of the given show. The returned performances hold information
     * about seats availability for each performance.
     * @param show The given show.
     * @return A list of all performances (with availability) of the given show.
     */
    public PerformanceWithAvailability[] getAvailabilityForPerformances(Show show) {

        Map performanceById = new HashMap();

        List availabilities = getAvailabilityDetails(show.getId());

        for (Iterator iter = availabilities.iterator(); iter.hasNext();) {
            AvailabilityDetail availability = (AvailabilityDetail)iter.next();

            Performance performance = availability.getPerformance();
            PerformanceWithAvailability performanceWithAvailability =
                (PerformanceWithAvailability)performanceById.get(new Long(performance.getId()));
            if (performanceWithAvailability == null) {
                performanceWithAvailability = new PerformanceWithAvailability(performance);
                performanceById.put(new Long(performance.getId()), performanceWithAvailability);
            }

            PriceBand priceBand = availability.getPriceBand();
            PriceBandWithAvailability priceBandWithAvailability =
                new PriceBandWithAvailability(priceBand, availability.getAvailableSeatCount());
            performanceWithAvailability.add(priceBandWithAvailability);
        }

        return (PerformanceWithAvailability[])performanceById.values().toArray(
            new PerformanceWithAvailability[performanceById.size()]);
    }

    // a helper method to retrieve the availability details for a show.
    private List getAvailabilityDetails(long showId) {

        List availabilities = getHibernateTemplate().findByNamedQueryAndNamedParam(
            "getPerformancesWithAvailability",
            new String[] { "showId", "date" },
            new Object[] { new Long(showId), new Date() }
        );

        List details = new ArrayList(availabilities.size());
        for (Iterator iter = availabilities.iterator(); iter.hasNext();) {
            Object[] values = (Object[])iter.next();
            AvailabilityDetail detail = new AvailabilityDetail();
            detail.setAvailableSeatCount(((Integer)values[0]).intValue());
            detail.setPriceBand((PriceBand)values[1]);
            detail.setPerformance((Performance)values[2]);
            details.add(detail);
        }

        return details;
    }


    /**
     * Returns a list of all available seats of the given class in the given performance.
     * @param performance The given performance.
     * @param seatClass   The class of the requested seats.
     * @return A list of all available seats of the given class in the given performance.
     */
    public Seat[] getAvailableSeats(Performance performance, SeatClass seatClass) {
        List seats = getHibernateTemplate().findByNamedQueryAndNamedParam(
            "getAvailableSeats",
            new String[] { "performance", "seatClass", "date"},
            new Object[] { performance, seatClass, new Date()}
        );

        return (Seat[])seats.toArray(new Seat[seats.size()]);
    }

    /**
     * Returns the number of available seats of the given class in the given performance.
     * @param performance The given performance.
     * @param seatClass The class of the requested seats.
     * @return Returns the number of available seats of the given class in the given performance.
     */
    public int getAvailableSeatsCount(Performance performance, SeatClass seatClass) {
        List result = getHibernateTemplate().findByNamedQueryAndNamedParam(
            "getAvailableSeatsCount",
            new String[] { "performance", "seatClass", "date"},
            new Object[] { performance, seatClass, new Date()}
        );

        return ((Integer)result.get(0)).intValue();
    }

    /**
     * Returns the cost of the given seats in the given performance.
     * @param performance The given performance.
     * @param seats       The given seats.
     * @return The cost of the given seats in the given performance.
     */
    public BigDecimal getCostOfSeats(Performance performance, Seat[] seats) {
        List price = getHibernateTemplate().findByNamedQueryAndNamedParam(
                "getCostOfSeats",
                new String[] { "performance", "seats"},
                new Object[] { performance , Arrays.asList(seats)}
        );
        return (BigDecimal)price.get(0);
    }

    /**
     * Removes the given booking from the database.
     * @param booking The booking to be removed from the database.
     */
    public void removeBooking(Booking booking) {

        // associating the given booking object with the hibernate session.
        getHibernateTemplate().refresh(booking);

        List seatStats = getHibernateTemplate().findByNamedQueryAndNamedParam(
            "getSeatStatusForBooking",
            new String[] {"booking"},
            new Object[] { booking }
        );

        for (Iterator iter = seatStats.iterator(); iter.hasNext();) {
            SeatStatus seatStatus = (SeatStatus)iter.next();
            seatStatus.setBooking(null);
        }

        getHibernateTemplate().delete(booking);

    }

    /**
     * Returns the booking associated with the given id.
     * @param bookingId The id of the requested booking.
     * @return The booking associated with the given id.
     */
    public Booking getBooking(long bookingId) {
        return (Booking)getHibernateTemplate().get(Booking.class, new Long(bookingId));
    }

    /**
     * Returns the seats associated with the given booking.
     * @param booking The given booking.
     * @return The seats associated with the given booking.
     */
    public List getSeatsForBooking(Booking booking) {
        return getHibernateTemplate().findByNamedQueryAndNamedParam(
            "getSeatsForBooking",
            new String[] { "booking" },
            new Object[] { booking }
        );
    }

    /**
     * Returns the performance associated with the given booking.
     * @param booking The given booking.
     * @return The performance associated with the given booking.
     */
    public Performance getPerformanceForBooking(Booking booking) {
        List result = getHibernateTemplate().findByNamedQueryAndNamedParam(
            "getPerformanceForBooking",
            new String[] { "booking" },
            new Object[] { booking }
        );
        return (result.isEmpty()) ? null : (Performance)result.get(0);
    }

    /**
     * Checks the availability of the seats of the given booking.
     * @param booking The booking to check.
     * @return True if the seats are available, false otherwise.
     */
    public boolean checkAvailabilityForBooking(Booking booking) {
        List seats = getSeatsForBooking(booking);
        return seats.size() == booking.getSeatCount();
    }

    /**
     * Updates the authorization code of the given purchase in the database.
     * @param purchase The purchase to update.
     */
    public void updatePurchaseAuthorizationCode(Purchase purchase) {
        getHibernateTemplate().update(purchase);
    }

    /**
     * Returns all generes in the database.
     * @return All generes in the database.
     */
    public List getAllGenres() {
        return getHibernateTemplate().find("from " + Genre.class.getName() + " g order by g.name");
    }

    /**
     * Returns all generes with shows in the database.
     * @return All generes with shows in the database.
     */
    public List getCurrentGenres() {
        return getHibernateTemplate().findByNamedQuery("currentGenres");
    }

    /**
     * Returns the show associated with the given show id.
     * @param id The id of the requested show.
     * @return The show associated with the given show id.
     */
    public Show getShow(long id) {
        return (Show)getHibernateTemplate().get(Show.class, new Long(id));
    }

    /**
     * Returns the performance associated with the given performance id.
     * @param id The id of the requested performance.
     * @return The performance associated with the given performance id.
     */
    public Performance getPerformance(long id) {
        return (Performance) getHibernateTemplate().load(Performance.class, new Long(id));
    }

    /**
     * Returns the price band associated with the given price band id.
     * @param id The id of the requesed price band.
     * @return The price band associated with the given price band id.
     */
    public PriceBand getPriceBand(long id) {
        return (PriceBand) getHibernateTemplate().load(PriceBand.class, new Long(id));
    }

    /**
     * Returns the price structure associated with the given price structure id.
     * @param id The id of the requested price structure.
     * @return The price structure associated with the given price structure id.
     */
    public PriceStructure getPriceStructure(long id) {
        return (PriceStructure)getHibernateTemplate().get(PriceStructure.class, new Long(id));
    }

    /**
     * Saves the purchase of the given booking in the database.
     * @param booking The booking of which the purchase will be saved in the database.
     */
    public void savePurchaseForBooking(Booking booking) {
        getHibernateTemplate().update(booking);
    }

}
