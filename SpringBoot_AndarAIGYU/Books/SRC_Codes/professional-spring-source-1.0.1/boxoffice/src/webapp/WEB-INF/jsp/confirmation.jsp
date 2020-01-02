<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>

    <head>
        <title>Tickets purchased for <c:out value="${performance.show.name}"/></title>
    </head>

    <body>
        <b>
            Your purchase of seats for performance of <c:out value="${performance.show.name}"/> <br/>
            on <fmt:formatDate value="${performance.dateAndTime}" pattern="EEEEEE MMMMM d, yyyy"/>, has been accepted.
        </b>

        <p/>

        Your credit card No. <c:out value="${purchase.encryptedCardNumber}"/> will be debited $<c:out value="${reservation.booking.price}"/>.

        <p/>

        You have been allocated seats
        <c:forEach items="${reservation.seats}" var="seat" varStatus="status">
            <c:if test="${status.count > 1}">; </c:if>
            <c:out value="${seat.name}"/>
        </c:forEach>

        <p/>

        <c:choose>

            <c:when test="${!purchase.userCollected}">
                Your tickets will be sent by post to the following address:
                <br/><br/>
                <c:out value="${purchase.customerName}"/><br/>
                <c:choose>
                    <c:when test="${!useBillingAddressForDelivery}">
                        <c:out value="${purchase.deliveryAddress.street}"/><br/>
                        <c:out value="${purchase.deliveryAddress.city}"/><br/>
                        <c:out value="${purchase.deliveryAddress.country}"/><br/>
                        <c:out value="${purchase.deliveryAddress.postcode}"/>
                    </c:when>
                    <c:otherwise>
                        <c:out value="${purchase.billingAddress.street}"/><br/>
                        <c:out value="${purchase.billingAddress.city}"/><br/>
                        <c:out value="${purchase.billingAddress.country}"/><br/>
                        <c:out value="${purchase.billingAddress.postcode}"/>
                    </c:otherwise>
                </c:choose>
            </c:when>

            <c:otherwise>
                    Your tickets will waiting for you in the ticketing stand of the theatre.
            </c:otherwise>

        </c:choose>
        <p/>

        <b>Your booking reference is <c:out value="${purchase.paymentAuthorizationCode}"/>.</b>
        Please retain it for your records (you can print this page). You will need to have this
        number ready in case of enquiry.

        A confirmation of this transation will be sent by email to <c:out value="${purchase.email}"/>.

        <p/>
        <p/>

        <a href="listShows.html">Back to welcome page</a>

    </body>
</html>