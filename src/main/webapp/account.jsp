<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link href="myStyle.css" rel = "stylesheet">
    <title>Your cabinet</title>
</head>
<body>
    <div class="tabs">
        <div class="tabs__nav">
            <button class="tabs__nav-btn" type="button" data-tab="#tab_1">Cabinet</button>
            <button class="tabs__nav-btn" type="button" data-tab="#tab_2">Cards</button>
        </div>
        <div class="tabs__content">

            <div class="tabs__item" id="tab_1">
                <h3>Your cabinet</h3><br>
                ${sessionScope.currUser.login}
                <hr>
                ${sessionScope.currUser.password}
                <hr>
                ${sessionScope.currUser.roleID}
                <hr>
                ${sessionScope.currUser.email}
                <hr>
                ${sessionScope.currUser.status}
            </div>
            <div class="tabs__item" id="tab_2">
                <h1>Cards</h1>
                <p>12463512345123</p>
            </div>
        </div>
    </div>
    <script src="myScript.js"></script>
</body>
</html>