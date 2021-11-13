<%@ include file="WEB-INF/jspf/tags.jspf"%>
<%@ include file="WEB-INF/jspf/page.jspf"%>
<!DOCTYPE html>
<html>

<c:set var="title" value="Login" scope="page" />
<%@ include file="WEB-INF/jspf/head.jspf"%>

<body>
    <table id="main-container">
        <tr >
            <td class="content center">
                <form action="controller" method="post">
                    <c:if test="${not empty requestScope.wrongData}">
                        ${requestScope.wrongData}
                        <br>
                    </c:if>
                    <br/>
                    <input type="hidden" name="command" value="login"/>
                    <label for="login"><b>Login</b></label>
                    <input type="text" placeholder="Enter login" name="login" id="login" required>
                    <label for="pass"><b>Password</b></label>
                    <input type="password" placeholder="Enter Password" name="pass" id="pass" required>
                    <hr>
                    <button type="submit" class="loginbtn" value="Login">Login</button>
                    <p> <a href="registration.jsp">Create account</a>.</p>
                </form>
            </td>
        </tr>
        <%@include file="WEB-INF/jspf/footer.jspf"%>
    </table>
</body>
</html>