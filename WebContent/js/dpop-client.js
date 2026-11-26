var DpopClient = (function() {

    const DB_NAME = "altoroj-dpop";
    const STORE_NAME = "keys";

    async function sha256(data) {

        return crypto.subtle.digest(
            "SHA-256",
            new TextEncoder().encode(data)
        );
    }

    function base64Url(buffer) {

        return btoa(
                String.fromCharCode(
                    ...new Uint8Array(buffer)
                )
            )
            .replace(/\+/g, "-")
            .replace(/\//g, "_")
            .replace(/=+$/, "");
    }

    async function generateProof(method, url) {

        const keys =
            await getOrCreateKeys();

        const publicJwk =
            await crypto.subtle.exportKey(
                "jwk",
                keys.publicKey
            );

        const header = {
            typ: "dpop+jwt",
            alg: "ES256",
            jwk: {
                kty: publicJwk.kty,
                crv: publicJwk.crv,
                x: publicJwk.x,
                y: publicJwk.y
            }
        };

        const payload = {
            jti: generateJti(),
            iat: Math.floor(Date.now() / 1000),
            htm: method,
            htu: url
        };

        const encodedHeader =
            base64UrlString(
                JSON.stringify(header)
            );

        const encodedPayload =
            base64UrlString(
                JSON.stringify(payload)
            );

        const signingInput =
            encodedHeader + "." + encodedPayload;

        const signature =
            await crypto.subtle.sign({
                    name: "ECDSA",
                    hash: {
                        name: "SHA-256"
                    }
                },
                keys.privateKey,
                new TextEncoder().encode(
                    signingInput
                )
            );

        const encodedSignature =
            base64Url(
                new Uint8Array(signature)
            );

        console.log(
            "DPoP payload",
            payload
        );

        console.log(
            "DPoP header",
            header
        );

        return (
            signingInput +
            "." +
            encodedSignature
        );
    }

    async function getJkt() {

        const keys =
            await getOrCreateKeys();

        const jwk =
            await crypto.subtle.exportKey(
                "jwk",
                keys.publicKey
            );

        const canonical =
            JSON.stringify({
                crv: jwk.crv,
                kty: jwk.kty,
                x: jwk.x,
                y: jwk.y
            });

        const digest =
            await sha256(canonical);

        return base64Url(digest);
    }

    function generateJti() {

        return crypto.randomUUID();
    }



    async function openDb() {

        return new Promise((resolve, reject) => {

            const request =
                indexedDB.open(DB_NAME, 1);

            request.onupgradeneeded = () => {

                const db = request.result;

                if (
                    !db.objectStoreNames.contains(
                        STORE_NAME
                    )
                ) {
                    db.createObjectStore(
                        STORE_NAME
                    );
                }
            };

            request.onsuccess =
                () => resolve(request.result);

            request.onerror =
                () => reject(request.error);

        });
    }

    function base64UrlString(str) {

        return base64Url(
            new TextEncoder().encode(str)
        );
    }

    async function getOrCreateKeys() {

        const db = await openDb();

        const existing =
            await new Promise((resolve, reject) => {

                const req =
                    db.transaction(STORE_NAME)
                    .objectStore(STORE_NAME)
                    .get("dpop");

                req.onsuccess =
                    () => resolve(req.result);

                req.onerror =
                    () => reject(req.error);
            });


        if (existing) {
            console.log("Using existing DPoP keypair");
            return existing;
        }

        console.log("Generating new DPoP keypair");

        const keys =
            await crypto.subtle.generateKey({
                    name: "ECDSA",
                    namedCurve: "P-256"
                },
                false,
                ["sign", "verify"]
            );

        await new Promise(resolve => {

            const tx =
                db.transaction(
                    STORE_NAME,
                    "readwrite"
                );

            tx.objectStore(STORE_NAME)
                .put(keys, "dpop");

            tx.oncomplete = resolve;
        });

        return keys;
    }

    return {
        getOrCreateKeys,
        getJkt,
        generateProof
    };

})();

window.testDpop = async function() {

    const keys =
        await DpopClient.getOrCreateKeys();

    console.log(keys);

    return keys;
};