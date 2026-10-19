navigator.serviceWorker.addEventListener(
    "message",
    function(event) {

        if (event.data.type !==
            "GET_ACCESS_TOKEN") {
            return;
        }

        event.ports[0].postMessage(
            sessionStorage.getItem(
                "accessToken"));
    });
