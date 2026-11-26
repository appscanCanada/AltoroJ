<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
<title>Initializing Security</title>
</head>
<body>

<p>Initializing DPoP...</p>

<script src="<%=request.getContextPath()%>/js/dpop-client.js"></script>

<script>

(async () => {

    try {

        const keys = await DpopClient.getOrCreateKeys();

	const jkt = await DpopClient.getJkt();

        console.log("JKT", jkt);

        document.body.innerHTML +=
            "<hr><h3>JKT</h3><pre>" +
            jkt +
            "</pre>";

        console.log(
            "DPoP keypair initialized",
            keys
        );

        // window.location = "<%=request.getContextPath()%>/bank/main.jsp";

    } catch (e) {

        console.error(e);

        document.body.innerHTML =
            "<h2>DPoP initialization failed</h2>";
    }

})();

</script>

</body>
</html>
