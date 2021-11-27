<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/tags.jspf"%>
<!DOCTYPE html>
<html>
<c:set var="title" value="Add a new card" scope="page" />
<%@include file="/WEB-INF/jspf/head.jspf"%>
<body>
<table id ="main-container">
    <%@ include file="/WEB-INF/jspf/changeLocale.jspf"%>
    <%@include file="/WEB-INF/jspf/header.jspf"%>
    <tr>
        <td class="content">
            <form action="controller" method="post">

                <c:if test="${not empty sessionScope.invalidName}">
                    ${sessionScope.invalidName}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.invalidNumber}">
                    ${sessionScope.invalidNumber}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.invalidCVV}">
                    ${sessionScope.invalidCVV}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.invalidPIN}">
                    ${sessionScope.invalidPIN}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.alreadyExist}">
                    ${sessionScope.alreadyExist}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.isSuccess}">
                    ${sessionScope.isSuccess}
                    <br>
                </c:if>
                <input type="hidden" name="command" value="createCard"/>

                <div class="container">
                    <h1 ><fmt:message key='card.create'/></h1>
                    <hr>
                    <label for="name"><b><fmt:message key='card.name'/></b></label>
                    <input type="text" name="name" id="name" maxlength="45">

                    <script src="myScript.js"></script>
                    <label for="cardNumber"><fmt:message key='card.number'/>*</label>
                    <input type="text"  name="cardNumber" id="cardNumber" required maxlength="16" minlength="16" onkeypress="return onlyNumberKey(event)">

                    <label for="cvv">CVV*</label>
                    <input type="text" name="cvv" id="cvv" required maxlength="3" minlength="3" onkeypress="return onlyNumberKey(event)">

                    <label for="cvv">PIN*</label>
                    <input type="text" name="pin" id="pin" required maxlength="4" minlength="4" onkeypress="return onlyNumberKey(event)">
                    <hr>
                    <button type="submit" class="mybtn"><fmt:message key='label.add'/></button>
                </div>
            </form>
            <c:remove var="alreadyExist" scope="session"/>
            <c:remove var="isSuccess" scope="session"/>
            <c:remove var="invalidCVV" scope="session"/>
            <c:remove var="invalidPIN" scope="session"/>
            <c:remove var="invalidNumber" scope="session"/>
            <c:remove var="invalidName" scope="session"/>
        </td>
    </tr>
</table>
</body>
</html>