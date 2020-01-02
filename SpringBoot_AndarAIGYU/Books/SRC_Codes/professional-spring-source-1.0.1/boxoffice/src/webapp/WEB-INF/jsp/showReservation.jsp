<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
    <head>
        <title>Seats reserved for <c:out value="${performance.show.name}"/></title>
    </head>

    <body>

        <b><c:out value="${performance.show.name}"/>:
        <fmt:formatDate value="${performance.dateAndTime}" type="date" pattern="EEEE MMMM dd, yyyy"/>
        </b>
        <br/>
        <p/>

        <c:out value="${seatsCount}" />
        seats have been reserved for you for
        <c:out value="${minutesReservationWillBeValid}" />
        minutes to give you time to complete your purchase.
        The seat numbers are

        <ul>
        <c:forEach items="${reservation.seats}" var="seat">
            <li><c:out value="${seat.name}"/></li>
        </c:forEach>
        </ul>


        <p/>
        The total cost of these tickets will be
        <fmt:formatNumber value="${reservation.booking.price}" type="currency"/>.
        This includes a booking fee of
        <fmt:formatNumber value="${bookingFee}" type="currency"/>.

        <p/>

        <c:if test="${!seatsAdjacent}" >
            <b>Please note that due to lack of availability, some of the
            seats offered are not adjacent.</b>
        </c:if>

        <table>

            <tr>

                <td>
                    <form method="GET" action="displayShow.html">
                        <input type="hidden" name="showId" value="<c:out value="${performance.show.id}"/>"/>
                        <c:choose>
                            <c:when test="${!seatsAdjacent}" >
                                <input type="submit" value="Try another date"/>
                            </c:when >
                            <c:otherwise>
                                <input type="submit" value="Cancel"/>
                            </c:otherwise >
                        </c:choose>
                    </form>
                </td>

                <td>
                    <form method="GET" action="payment.html">
                        <input type="hidden" name="bookingId" value="<c:out value="${reservation.booking.id}"/>"/>
                        <input type="hidden" name="performanceId" value="<c:out value="${performance.id}"/>"/>
                        <input type="submit" value="Proceed"/>
                    </form>

                </td>

            </tr>

        </table>

    </body>

</html>