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
        hookForms();
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

function hookForms() {

    document.querySelectorAll("form")
        .forEach(function(form) {

            if (form.dataset.dpopHooked)
                return;

            form.dataset.dpopHooked = "true";

            form.addEventListener(
                "submit",
                function(e) {

                    e.preventDefault();

                    const formData =
                        new FormData(form);

                    fetch(form.action, {
                        method: form.method || "POST",
                        body: formData,
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
                        hookForms();
                    });
                });
        });
}
