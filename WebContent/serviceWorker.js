console.log("serviceWorker.js loaded");

self.addEventListener("fetch", event => {

    console.log("SW fetch:", event.request.url);
    
    event.respondWith(handleRequest(event));

});

self.addEventListener("activate", event => {

    console.log("SW activated");

    event.waitUntil(
        self.clients.claim()
    );

});

self.addEventListener("install", event => {

    console.log("SW installed");

    self.skipWaiting();

});

async function handleRequest(event) {

    const client =
        await self.clients.get(event.clientId);

    if (!client) {
        return fetch(event.request);
    }

    const token =
        await requestToken(client);

    console.log("Token received:", token);

    if (!token) {
        return fetch(event.request);
    }

    const url =
    new URL(event.request.url);

if (!url.pathname.startsWith(
        "/altoromutual")) {

    return fetch(event.request);
}

    const headers =
        new Headers(event.request.headers);

    headers.set(
        "Authorization",
        "DPoP " + token);

    headers.set(
        "DPoP",
        "test-proof");

    const request =
        new Request(
            event.request,
            { headers });

    return fetch(request);
}

function requestToken(client) {

    return new Promise(resolve => {

        const channel =
            new MessageChannel();

        channel.port1.onmessage =
            event => {

                resolve(
                    event.data);

            };

        client.postMessage(
            {
                type:
                    "GET_ACCESS_TOKEN"
            },
            [channel.port2]);

    });
}
