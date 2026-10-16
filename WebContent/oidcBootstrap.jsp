<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>OIDC Bootstrap</title>
</head>
<body>

Loading...

<script>
window.accessToken =
    '<%= session.getAttribute("access_token") %>';
</script>

<script src="<%=request.getContextPath() %>/scripts/dpop-client.js"></script>

<script>
window.addEventListener("load", function () {

    navigateWithAuth(
        '<%= request.getContextPath() %>/bank/main.jsp');

});
</script>

</body>
</html>
