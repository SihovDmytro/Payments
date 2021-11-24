<%@ include file="/WEB-INF/jspf/tags.jspf"%>
<%@ include file="/WEB-INF/jspf/page.jspf"%>
<%@ page import="com.my.payment.constants.Path" %>
<!DOCTYPE html>
<html>
<c:set var="title" value="Login" scope="page" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
    <table id="main-container">
        <tr >
            <td class="content">
                <form action="controller" method="post">
                    <c:if test="${not empty sessionScope.wrongData}">
                        ${sessionScope.wrongData}
                        <br>
                    </c:if>
                    <br/>
                    <input type="hidden" name="command" value="login"/>

                    <label for="login"><b>Login</b></label>
                    <input type="text" placeholder="Enter login" name="login" id="login" required maxlength="20"
                    pattern="^[\w_-]{3,20}$">

                    <label for="pass"><b>Password</b></label>
                    <input type="password" placeholder="Enter Password" name="pass" id="pass" required maxlength="45" minlength="6">

                    <hr>
                    <button type="submit" class="mybtn" value="Login">Login</button>
                    <p> <a href="${Path.REGISTRATION_PAGE}">Create account</a>.</p>
                </form>
                <c:remove var="wrongData" scope="session"/>
            </td>
        </tr>

    </table>
</body>
</html>