<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>OIDC Bootstrap</title>
</head>
<body>

Loading...

<script>
const token =
    '<%= session.getAttribute("access_token") %>';

fetch('/altoromutual/bank/main.jsp', {
    headers: {
        'Authorization': 'DPoP ' + token,
        'DPoP': 'test-proof'
    }
})
.then(response => response.text())
.then(html => {
    document.open();
    document.write(html);
    document.close();
})
.catch(error => {
    document.body.innerHTML =
        '<h1>Bootstrap failed</h1><pre>' +
        error +
        '</pre>';
});
</script>

</body>
</html>
