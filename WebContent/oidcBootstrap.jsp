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

                navigator.serviceWorker
                .register(
                "<%= request.getContextPath() %>/sw.js")
                .then(function() {

                window.location.replace(
                "<%= request.getContextPath() %>/bank/main.jsp");

                });
            </script>

        </body>
    </html>
