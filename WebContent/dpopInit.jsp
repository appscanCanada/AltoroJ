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

        const keys =
            await DpopClient.getOrCreateKeys();

        const jkt =
            await DpopClient.getJkt();

        const response =
            await fetch(
                "/bank/registerDpopKey",
                {
                    method: "POST",
                    headers: {
                        "Content-Type":
                            "application/x-www-form-urlencoded"
                    },
                    body:
                        "jkt=" +
                        encodeURIComponent(jkt)
                }
            );

	const responseBody = await response.text();

	const proof =
            await DpopClient.generateProof(
                "POST",
                window.location.origin
                + "/bank/dpopTest"
            );

        console.log(
            "Registration status",
            responseBody
        );

        console.log(
            "JKT",
            jkt
        );

        console.log(
            "DPoP keypair initialized",
            keys
        );

        document.body.innerHTML +=
            "<hr>" +
            "<h3>JKT</h3>" +
            "<pre>" + jkt + "</pre>" +
	    "<h3>DPoP Proof</h3>" +
            "<pre>" + proof + "</pre>" +
            "<p><b>Registration Status:</b> "
            + response.status +
            "</p>";

        // window.location =
        //     "<%=request.getContextPath()%>/bank/main.jsp";

    } catch (e) {

        console.error(e);

        document.body.innerHTML =
            "<h2>DPoP initialization failed</h2>";
    }

})();

</script>

</body>
</html>

