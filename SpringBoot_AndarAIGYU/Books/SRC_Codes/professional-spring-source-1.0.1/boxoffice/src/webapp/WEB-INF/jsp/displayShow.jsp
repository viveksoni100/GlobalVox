<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>


<html>

<head>

<script language="JavaScript" >

    function submitForm(performanceId, priceBandId) {
		document.seatsForm.performanceId.value = performanceId;
		document.seatsForm.priceBandId.value = priceBandId;
		document.seatsForm.submit();
	}

    function openWindow(url) {
        window.open(url);
    }

</script>

<title>Performances of <c:out value="${show.name}"/></title>

</head>


<body>

<form name="seatsForm" method="GET" action="reserveSeats.html">

	<%-- These form fields are populated by JavaScript --%>
	<input type="hidden" name="performanceId" value=""/>
	<input type="hidden" name="priceBandId" value=""/>


	<table cellpadding=10 cellspacing="0">


        <tr>
            <c:choose>
                <c:when test="${not empty lastPerformanceDate}">
                    <td width="55%"><font size='4'>Showing <i><c:out value="${show.name}"/></i> until
                        <fmt:formatDate value="${lastPerformanceDate}" pattern="MMMMMM d yyyy"/></font></td>
                </c:when>
                <c:otherwise>
                    <td width="55%"><font size='4'>Currently not showing <i><c:out value="${show.name}"/></i></font></td>
                </c:otherwise>
            </c:choose>

            <c:if test="${performancesCount > 0}">
                <td width="*"><font size='4'>Performances & availability</font></td>
            </c:if>
        </tr>


		<tr>

            <td>

                <%-- importing the information page for the show --%>
                <c:choose>
                    <c:when test="${not empty showInfoUrl}">
                        <c:import url="/${showInfoUrl}"/>
                    </c:when>
                    <c:otherwise>
                        No info available
                    </c:otherwise>
                </c:choose>
            </td>

	<td valign="top">

        <c:if test="${performancesCount > 0}">

        <div border="10">
            <font size="2">
                <i>
                Legend:
                <c:forEach items="${seatClasses}" var="seatClass">
                    <c:out value="${seatClass.code}"/> = <c:out value="${seatClass.description}"/>,
                </c:forEach>
                <font color="red">*</font> = sold out
                </i>

                <br/>
                <c:if test="${not empty seatingPlanUrl}">
                    <a href="javascript:openWindow('<c:out value="${seatingPlanUrl}"/>');">View the seating plan</a>
                </c:if>
            </font>
        </div>

        <br/>
        <br/>

        <table >

            <c:forEach items="${performances}" var="performance" varStatus="status" >

                <c:choose>
                    <c:when test="${status.count % 2 == 0}">
                        <tr bgcolor="#E0E0E0">
                    </c:when>
                    <c:otherwise>
                        <tr>
                    </c:otherwise>
                </c:choose>

                    <td width="150"><fmt:formatDate value="${performance.dateAndTime}" pattern="EEE MMMMMM d"/></td>

                    <c:forEach items="${performance.priceBandWithAvailability}" var="priceBand">

                        <c:choose>
                            <c:when test="${priceBand.availableSeatCount > 0}">
                                <td width="40">
                                    <a href="javascript:submitForm(<c:out value="${performance.id}"/> , <c:out value="${priceBand.id}"/>)">
                                        <c:out value="${priceBand.seatClass.code}"/>(<c:out value="${priceBand.availableSeatCount}"/>)
                                    </a>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td width="40"><c:out value="${priceBand.seatClass.code}"/><font color="red"><SUP>*</SUP></font></td>
                            </c:otherwise>
                        </c:choose>

                    </c:forEach>
                </tr>

            </c:forEach>

        </table>

        </c:if>

    </td>


</form>

<br/>

</body>

</html>