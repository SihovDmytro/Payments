<%@include file="/WEB-INF/jspf/page.jspf"%>
<%@include file="/WEB-INF/jspf/tags.jspf"%>
<html>
<c:set var="title" value="Payment success" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
<form>
    <table id="main-container">
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <tr>
            <td class="content">
                Payment success!
            </td>
        </tr>
        <%@ include file="/WEB-INF/jspf/footer.jspf" %>
    </table>
    <hr>
</form>
</body>
</html>