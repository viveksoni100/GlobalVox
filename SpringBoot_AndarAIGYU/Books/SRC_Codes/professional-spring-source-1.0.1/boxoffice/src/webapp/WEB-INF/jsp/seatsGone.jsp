<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<html>

    <head>

        <title>Seats Taken</title>

        <script language="JavaScript">

            function submitForm() {
                var form = document.getElementById("theform");
                form.submit();
            }

        </script>

    </head>

    <body>

        <%-- if this page was reached due to an exception then retrieving the performance from the exception --%>
        <c:if test="${not empty exception}">
            <c:set var="performance" value="${exception.performance}" scope="request"/>
        </c:if>

        You have waited too long with your purchase. In the meantime your seats have been reserved
        by other customers. <br/>

        Please check out other seat availabilities for
        <a href="javascript:submitForm();">Check <c:out value="${performance.show.name}"/></a>.

        <form id="theform" action="/displayShow.html">
            <input type="hidden" name="showId" value="<c:out value="${performance.show.id}"/>"/>
        </form>

    </body>

</html>