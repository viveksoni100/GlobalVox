<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--

    This is an example for a page with i18n support. Notice there's no static text in this page, instead
    we use the <spring:message> tag with codes that are mapped in the ui-messages.properties resource bundle.

    see p. 502 in the book

--%>

<html>

<head>
    <title>
        <spring:message code="paymentForm.title" arguments="${purchaseRequest.performance.show.name}" />
    </title>

    <script language="JavaScript">

        function cancel() {
            var form = document.getElementById("cancelForm");
            form.submit();
        }

    </script>
</head>

<body>

    <h3>
        <c:out value="${purchaseRequest.performance.show.name}"/>:
        <fmt:formatDate value="${purchaseRequest.performance.dateAndTime}" pattern="EEEEEE MMMMM d, yyyy"/>
    </h3>

    <form method="POST" action="">

        <spring:message code="paymentForm.totalCostDescription"/> <fmt:formatNumber type="currency" value="${purchaseRequest.reservation.booking.price}"/>

        <h3><spring:message code="paymentForm.paymentDetailsHeader"/></h3>

        <spring:bind path="purchaseRequest" >
            <c:if test="${status.error}">
                <div style="color:red">
                    <c:forEach items="${status.errorMessages}" var="errorMessage">
                       - <c:out value="${status.errorMessage}"/><br/>
                    </c:forEach>
                </div>
            </c:if>
        </spring:bind>

        <%-- defines the context for the nested bind tags (p. 501 in the book) --%>
        <spring:nestedPath path="purchaseRequest" >

        <spring:bind path="reservation" >
            <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.displayValue}"/>"/>
        </spring:bind>

        <spring:message code="paymentForm.form.creditCardType"/>:
        <spring:bind path="creditCardDetails.type">
        <table>
            <tr>
                <td width="150"><input type="radio" name="<c:out value="${status.expression}"/>" value="visa" checked="yes">Visa</td>
                <td width="150"><input type="radio" name="<c:out value="${status.expression}"/>" value="mc">MasterCard</td>
                <td width="150"><input type="radio" name="<c:out value="${status.expression}"/>" value="amex">Amex</td>
            </tr>
        </table>
        </spring:bind>

        <table>

			<tr>
			<td><spring:message code="paymentForm.form.nameOnCard" /></td>
            <td>
                <spring:bind path="creditCardDetails.nameOnCard" >
                    <input type="text" size="40" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.displayValue}"/>" />
                    <c:if test="${status.error}">
                        <font color="red"><c:out value="${status.errorMessage}"/></font>
                    </c:if>
                </spring:bind>
            </td>
			</tr>

			<tr>
                <td><spring:message code="paymentForm.form.cardNumber" /> </td>
                <td>
                    <spring:bind path="creditCardDetails.cardNumber">
                        <input type="text" size="19" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <c:if test="${status.error}">
                            <font color="red"><c:out value="${status.errorMessage}"/></font>
                        </c:if>
                    </spring:bind>
                </td>
			</tr>

			<tr>
			    <td><spring:message code="paymentForm.form.expiryDate" /> </td>
                <td>
                    <spring:bind path="creditCardDetails.expiryDate">
                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.displayValue}"/>"/>
                        <c:if test="${status.error}">
                            <font color="red"><c:out value="${status.errorMessage}"/></font>
                        </c:if>
                    </spring:bind>
                </td>
			</tr>

    </table>

    <table>
        <tr>
            <td width="50%">
                <spring:message code="paymentForm.form.billingAddress.title"/>
            </td>

            <td width="50%">
                <spring:message code="paymentForm.form.deliveryAddress.title"/>
            </td>

        </tr>
        <tr>

            <td width="50%">
                <table>
                    <tr>
                        <td><spring:message code="paymentForm.form.country"/></td>
                        <td>
                            <spring:bind path="billingAddress.country">
                                <input type="text" size="16" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.displayValue}"/>"/>
                                <c:if test="${status.error}">
                                    <font color="red"><c:out value="${status.errorMessage}"/></font>
                                </c:if>
                            </spring:bind>
                        </td>
                    </tr>

                    <tr>
                        <td><spring:message code="paymentForm.form.city"/></td>
                        <td>
                            <spring:bind path="billingAddress.city">
                                <input type="text" size="16" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.displayValue}"/>"/>
                                <c:if test="${status.error}">
                                    <font color="red"><c:out value="${status.errorMessage}"/></font>
                                </c:if>
                            </spring:bind>
                        </td>
                    </tr>

                    <tr>
                        <td><spring:message code="paymentForm.form.street"/></td>
                        <td>
                            <spring:bind path="billingAddress.street">
                                <input type="text" size="16" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.displayValue}"/>"/>
                                <c:if test="${status.error}">
                                    <font color="red"><c:out value="${status.errorMessage}"/></font>
                                </c:if>
                            </spring:bind>
                        </td>
                    </tr>

                    <tr>
                        <td><spring:message code="paymentForm.form.postcode"/></td>
                        <td>
                            <spring:bind path="billingAddress.postcode">
                                <input type="text" size="16" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.displayValue}"/>"/>
                                <c:if test="${status.error}">
                                    <font color="red"><c:out value="${status.errorMessage}"/></font>
                                </c:if>
                            </spring:bind>
                        </td>
                    </tr>

                </table>
            </td>

            <td>
                <table>
                    <tr>
                        <td><spring:message code="paymentForm.form.country"/></td>
                        <td>
                            <spring:bind path="deliveryAddress.country">
                                <input type="text" size="16" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.displayValue}"/>"/>
                                <c:if test="${status.error}">
                                    <font color="red"><c:out value="${status.errorMessage}"/></font>
                                </c:if>
                            </spring:bind>
                        </td>
                    </tr>

                    <tr>
                        <td><spring:message code="paymentForm.form.city"/></td>
                        <td>
                            <spring:bind path="deliveryAddress.city">
                                <input type="text" size="16" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.displayValue}"/>"/>
                                <c:if test="${status.error}">
                                    <font color="red"><c:out value="${status.errorMessage}"/></font>
                                </c:if>
                            </spring:bind>
                        </td>
                    </tr>

                    <tr>
                        <td><spring:message code="paymentForm.form.street"/></td>
                        <td>
                            <spring:bind path="deliveryAddress.street">
                                <input type="text" size="16" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.displayValue}"/>"/>
                                <c:if test="${status.error}">
                                    <font color="red"><c:out value="${status.errorMessage}"/></font>
                                </c:if>
                            </spring:bind>
                        </td>
                    </tr>

                    <tr>
                        <td><spring:message code="paymentForm.form.postcode"/></td>
                        <td>
                            <spring:bind path="deliveryAddress.postcode">
                                <input type="text" size="16" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.displayValue}"/>"/>
                                <c:if test="${status.error}">
                                    <font color="red"><c:out value="${status.errorMessage}"/></font>
                                </c:if>
                            </spring:bind>
                        </td>
                    </tr>

                </table>
            </td>

        </tr>
    </table>

    <spring:bind path="collect" >

        <%-- this input indicates to te binder that if the checkbox is not checked it should set the value to false (p. 463 in the book) --%>
        <input type="hidden" name="_<c:out value="${status.expression}"/>"/>

        <input type="checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${status.value}">checked="yes"</c:if>/>
        <spring:message code="paymentForm.form.collect" />
    </spring:bind>

    <p/>
    <spring:bind path="email">
        <c:if test="${status.error}">
            <font color="red"><c:out value="${status.errorMessage}"/></font><br/>
        </c:if>
        <spring:message code="paymentForm.form.email" />
        <input type="text" size="30" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.displayValue}"/>"/>
    </spring:bind>

<p/>

<input type="button" value="<spring:message code="paymentForm.form.buttons.cancel"/>" onClick="cancel();"/>
<input type="submit" value="<spring:message code="paymentForm.form.buttons.debit"/>"/>

</spring:nestedPath>

</form>

<form id="cancelForm" action="displayShow.html">
    <input type="hidden" name="showId" value="<c:out value="${purchaseRequest.performance.show.id}"/>"/>
</form>


</body>

</html>