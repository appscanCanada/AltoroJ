function navigateWithAuth(url) {

    fetch(url, {
        headers: {
            'Authorization':
                'DPoP ' + window.accessToken,
            'DPoP':
                'test-proof'
        }
    })
    .then(response => response.text())
    .then(html => {

        document.open();
        document.write(html);
        document.close();

        hookLinks();
    });
}

function hookLinks() {

    document.querySelectorAll("a[href]")
        .forEach(function(link) {

            link.addEventListener(
                "click",
                function(e) {

                    const href =
                        link.getAttribute("href");

                    if (!href.startsWith("/altoromutual"))
                        return;

                    e.preventDefault();

                    navigateWithAuth(href);
                });
        });
}
