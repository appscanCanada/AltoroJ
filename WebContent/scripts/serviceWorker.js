self.addEventListener("fetch", event => {

    event.respondWith(
        handleRequest(event)
    );

});

async function handleRequest(event) {

    const client =
        await self.clients.get(event.clientId);

    if (!client) {
        return fetch(event.request);
    }

    const token =
        await requestToken(client);

    if (!token) {
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
