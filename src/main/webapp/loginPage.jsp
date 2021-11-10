<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
    <link href="myStyle.css" rel = "stylesheet" type = "">
    <title>Login</title>
</head>
<body>
    <form action="controller" method="post">
        <c:if test="${not empty requestScope.wrongData}">
            ${requestScope.wrongData}
            <br>
        </c:if>
    <div>
        <br/>
        <input type="hidden" name="command" value="login"/>
        <label for="login"><b>Login</b></label>
        <input type="text" placeholder="Enter login" name="login" id="login" required>
        <label for="pass"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="pass" id="pass" required>
        <hr>
        <button type="submit" class="loginbtn" value="Login">Login</button>
    </div>
    <div class="container register">
        <p> <a href="registration.jsp">Forgot password?</a>.</p>
    </div>
    </form>
</body>
</html>