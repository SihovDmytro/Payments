<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/tags.jspf"%>
<!DOCTYPE html>
<html>
<c:set var="title" value="Add new card" scope="page" />
<%@include file="/WEB-INF/jspf/head.jspf"%>
<body>
<table id ="main-container">
    <%@include file="/WEB-INF/jspf/header.jspf"%>
    <tr>
        <td class="content">
            <form action="controller" method="post">

                <c:if test="${not empty requestScope.invalidName}">
                    ${requestScope.invalidName}
                    <br>
                </c:if>
                <c:if test="${not empty requestScope.invalidNumber}">
                    ${requestScope.invalidNumber}
                    <br>
                </c:if>
                <c:if test="${not empty requestScope.invalidCVV}">
                    ${requestScope.invalidCVV}
                    <br>
                </c:if>
                <c:if test="${not empty requestScope.invalidExpDate}">
                    ${requestScope.invalidExpDate}
                    <br>
                </c:if>
                <c:if test="${not empty requestScope.alreadyAdded}">
                    ${requestScope.alreadyAdded}
                    <br>
                </c:if>
                <c:if test="${not empty requestScope.isSuccess}">
                    ${requestScope.isSuccess}
                    <br>
                </c:if>
                <input type="hidden" name="command" value="addCard"/>

                <div class="container">
                    <h1 >Add card</h1>
                    <hr>
                    <label for="name"><b>Card name</b></label>
                    <input type="text" placeholder="Enter card name" name="name" id="name" maxlength="45">

                    <script src="${pageContext.request.contextPath}/myScript.js"></script>
                    <label for="cardNumber"><b>Card number*</b></label>
                    <input type="text" placeholder="Enter card number" name="cardNumber" id="cardNumber" required maxlength="16" minlength="16" onkeypress="return onlyNumberKey(event)">

                    <label for="cvv"><b>CVV*</b></label>
                    <input type="text" placeholder="Enter CVV" name="cvv" id="cvv" required maxlength="3" minlength="3" onkeypress="return onlyNumberKey(event)">

                    <label for="exp-date"><b>Expiration date*</b></label>
                    <input type="date" placeholder="Enter expiration date" name="exp-date" id="exp-date" required>

                    <hr>
                    <button type="submit" class="mybtn">Add</button>
                </div>
            </form>
        </td>
    </tr>
    <%@include file="/WEB-INF/jspf/footer.jspf"%>
</table>
</body>
</html>