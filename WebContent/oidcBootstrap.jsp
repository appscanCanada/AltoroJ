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

                sessionStorage.setItem("accessToken",window.accessToken);

                navigator.serviceWorker
                .register(
                "<%= request.getContextPath() %>/serviceWorker.js")
                .then(async function(registration) {

                console.log("Service Worker registered");

                await navigator.serviceWorker.ready;

                console.log("Service Worker ready");

                console.log("controller:", navigator.serviceWorker.controller);

                window.location.replace(
                "<%= request.getContextPath() %>/bank/main.jsp");
                });
            </script>

        </body>
    </html>
