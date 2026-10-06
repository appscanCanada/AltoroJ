package com.ibm.security.appscan.altoromutual.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OidcLoginServlet extends HttpServlet {

    private static final String CLIENT_ID = "altoro";

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String redirectUri =
            "http://localhost:8088/altoromutual/oidc/callback";

        String authUrl =
            "http://localhost:8085/realms/myrealm/protocol/openid-connect/auth"
            + "?client_id=" + CLIENT_ID
            + "&response_type=code"
            + "&scope=openid"
            + "&redirect_uri="
            + URLEncoder.encode(
                    redirectUri,
                    "UTF-8");

        response.sendRedirect(authUrl);
    }
}
