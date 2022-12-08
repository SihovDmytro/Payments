<%@ include file="/WEB-INF/jspf/tags.jspf" %>
<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ page import="com.my.payment.constants.Path" %>
<!DOCTYPE html>
<html>
<c:set var="title" scope="page" value="Login"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>
<div class="limit">
    <%@ include file="/WEB-INF/jspf/changeLocale.jspf" %>
    <div class="login-form">
        <form action="controller" method="post">
            <c:if test="${not empty sessionScope.wrongData}">
                ${sessionScope.wrongData}
                <br>
            </c:if>
            <br/>
            <input type="hidden" name="command" value="login"/>

            <label for="login"><b><fmt:message key='label.login'/></b></label>
            <input type="text" name="login" id="login" required maxlength="20"
                   pattern="^[\w_-]{3,20}$">

            <label for="pass"><b><fmt:message key='label.password'/></b></label>
            <input type="password" name="pass" id="pass" required maxlength="45" minlength="6">

            <hr>
            <div class="mybtn-wrapper">
                <button type="submit" class="mybtn" value="Login"><fmt:message key='login.enter'/></button>
            </div>
            <p><a href="${Path.REGISTRATION_PAGE}"><fmt:message key='signUp.createAccount'/></a>.</p>
        </form>
        <c:remove var="wrongData" scope="session"/>
    </div>
</div>
</body>
</html>