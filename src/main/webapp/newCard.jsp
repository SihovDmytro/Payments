<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/tags.jspf"%>
<!DOCTYPE html>
<html>
<c:set var="title" value="Add a new card" scope="page" />
<%@include file="/WEB-INF/jspf/head.jspf"%>
<body>
<table id ="main-container">
    <%@ include file="/WEB-INF/jspf/changeLocale.jspf"%>
    <%@ include file="/WEB-INF/jspf/header.jspf"%>
    <%@ include file="/WEB-INF/jspf/scripts.jspf"%>
    <tr>
        <td class="content">
            <form action="controller" method="post">

                <c:if test="${not empty sessionScope.invalidNumber}">
                    ${sessionScope.invalidNumber}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.wrongData}">
                    ${sessionScope.wrongData}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.doesNotEx}">
                    ${sessionScope.doesNotEx}
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
                <c:if test="${not empty sessionScope.invalidExpDate}">
                    ${sessionScope.invalidExpDate}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.alreadyAdded}">
                    ${sessionScope.alreadyAdded}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.isSuccess}">
                    ${sessionScope.isSuccess}
                    <br>
                </c:if>
                <input type="hidden" name="command" value="addCard"/>
                <div class="container">
                    <h1 ><fmt:message key='card.add'/></h1>
                    <hr>

                    <label for="cardNumber"><fmt:message key='card.number'/>*</label>
                    <input type="text"  name="cardNumber" id="cardNumber" required maxlength="16" minlength="16" onkeypress="return onlyNumberKey(event)">

                    <label for="cvv">CVV*</label>
                    <input type="text" name="cvv" id="cvv" required maxlength="3" minlength="3" onkeypress="return onlyNumberKey(event)" >

                    <label for="pin">PIN*</label>
                    <input type="text" name="pin" id="pin" required maxlength="4" minlength="4" onkeypress="return onlyNumberKey(event)">

                    <label for="exp-date"><fmt:message key='card.date'/>*</label>
                    <input type="date" name="exp-date" id="exp-date" required>

                    <hr>
                    <button type="submit" class="mybtn"><fmt:message key='label.add'/></button>
                </div>
            </form>
            <c:remove var="alreadyAdded" scope="session"/>
            <c:remove var="wrongData" scope="session"/>
            <c:remove var="doesNotEx" scope="session"/>
            <c:remove var="isSuccess" scope="session"/>
            <c:remove var="invalidCVV" scope="session"/>
            <c:remove var="invalidPIN" scope="session"/>
            <c:remove var="invalidNumber" scope="session"/>
            <c:remove var="invalidExpDate" scope="session"/>
        </td>
    </tr>
    <script src="myScript.js"></script>
</table>
</body>
</html>