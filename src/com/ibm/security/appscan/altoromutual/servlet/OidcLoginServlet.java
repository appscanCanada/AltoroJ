package com.ibm.security.appscan.altoromutual.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OidcLoginServlet extends HttpServlet {

    String clientId = ConfigUtil.get("keycloak.clientId");

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String redirectUri =
            "http://localhost:8088/altoromutual/oidc/callback";

        String authUrl =
            ConfigUtil.get("keycloak.url")
            + "/realms/"
            + ConfigUtil.get("keycloak.realm")
            + "/protocol/openid-connect/auth"
            + "?client_id="
            + ConfigUtil.get("keycloak.clientId")
            + "&response_type=code"
            + "&scope=openid"
            + "&redirect_uri="
            + URLEncoder.encode(
                ConfigUtil.get("keycloak.redirectUri"),
                "UTF-8");

        response.sendRedirect(authUrl);
    }
}
