<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
    <head>
        <title>
            <fmt:message key="page.error.title" />
       </title>
   </head>
    <body>
        <div class="centered">
            <div class="content">
                <p>
                    <fmt:message key="page.error.message"/>
                </p>
                <p>
                    <a href="<c:url value='listShows.html'/>">
                        <fmt:message key="page.error.goBack"/>
                    </a>
                </p>
            </div>
        </div>
    </body>
</html>
