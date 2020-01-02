<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>

<head>
    <title>
        <c:out value="${reservationRequest.performance.show.name}"/> on <fmt:formatDate value="${reservationRequest.performance.dateAndTime}" pattern="EEEEEE MMMMM d, yyyy"/>
    </title>
</head>

<body>

    <spring:hasBindErrors name="reservationRequest">
        <spring:bind path="reservationRequest">
            <div style="color:red">
                <c:forEach items="${status.errorMessages}" var="errorMessage">
                    - <c:out value="${errorMessage}"/><br/>
                </c:forEach>
            </div>

            <p/>
        </spring:bind>
    </spring:hasBindErrors>

    <form method="POST" action="reserveSeats.html">

		<b>
            <c:out value="${reservationRequest.performance.show.name}"/>: <fmt:formatDate value="${reservationRequest.performance.dateAndTime}" pattern="EEEEEE MMMMM d, yyyy"/>
        </b>

        <p/>

        <b>
            <c:out value="${reservationRequest.priceBand.seatClass.description}"/>
            (<c:out value="${reservationRequest.priceBand.seatClass.code}"/>)
            at $<c:out value="${reservationRequest.priceBand.price}"/>
        </b>

        <p/>

		Number of tickets:
        <spring:bind path="reservationRequest.numberOfSeatsRequested">
            <select name="<c:out value="${status.expression}"/>">
                <c:forEach begin="1" end="${maximumSeats}" step="1" varStatus="iter">
                    <c:choose>
                        <c:when test="${iter.count == defaultSelected}">
                            <option selected="yes" value="<c:out value="${iter.count}"/>"><c:out value="${iter.count}"/></option>
                        </c:when>
                        <c:otherwise>
                            <option value="<c:out value="${iter.count}"/>"><c:out value="${iter.count}"/></option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
        </spring:bind>
        <br/>
        <br/>

        <input type="submit" value="Proceed" />

    </form>

<br/>

</body>

</html>

