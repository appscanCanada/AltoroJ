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

sessionStorage.setItem(
    "accessToken",
    window.accessToken);

navigator.serviceWorker
    .register(
        "<%= request.getContextPath() %>/serviceWorker.js")
    .then(async function() {

        await navigator.serviceWorker.ready;

        if (!navigator.serviceWorker.controller &&
            !sessionStorage.getItem(
                "swBootstrapReload")) {

            sessionStorage.setItem(
                "swBootstrapReload",
                "true");

            window.location.reload();

            return;
        }

        window.location.replace(
            "<%= request.getContextPath() %>/bank/main.jsp");

    });

</script>

        </body>
    </html>
