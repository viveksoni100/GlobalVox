<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<html>

<head>
    <title>Welcome to the Mandala Center</title>

    <script language="JavaScript">

        function submitForm(genreShowInputId) {
            var genreShowInput = document.getElementById(genreShowInputId);
            var showId = genreShowInput.value;
            if (showId == "--Select--") {
                return;
            }
            var showIdInput = document.getElementById("showIdInput");
            showIdInput.value = showId;
            var form = document.getElementById("theform");
            form.submit();
        }

    </script>

</head>

<body>

    Welcome to the <font size="+2"><i><b>Mandala Center</b></i></font> -- Metropolis' most exciting and diverse entertainment venue.

    <br/><p>(Note, in this sample application, only the Romeo and Juliet Theatre performance is available)</p>

    <form id="theform" action="displayShow.html" method="post">
        <input id="showIdInput" type="hidden" name="showId"/>
    </form>

    <table border="0">

    <c:forEach items="${genres}" var="genre">
        <c:if test="${not empty genre.shows}">
            <tr>
                <td><c:out value="${genre.name}"/>: </td>
                <td>
                    <select id="genreShowInput<c:out value="${genre.id}"/>" style="width: 150px;" name="showId">
                        <option value="--Select--">-- Make a selection --</option>
                        <c:forEach items="${genre.shows}" var="show">
                            <option value="<c:out value="${show.id}"/>"><c:out value="${show.name}"/></option>
                        </c:forEach>
                    </select>
                    <input type="button" value="Go" onClick="submitForm('genreShowInput<c:out value="${genre.id}"/>');"/>
                </td>
            </tr>
            <p/>
        </c:if>
    </c:forEach>

    </table>

</body>

</html>