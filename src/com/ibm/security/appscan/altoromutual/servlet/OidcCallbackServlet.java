package com.ibm.security.appscan.altoromutual.servlet;

import com.ibm.security.appscan.altoromutual.util.ServletUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

public class OidcCallbackServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String code = request.getParameter("code");

    String form = "grant_type=authorization_code"
        + "&client_id=" + ConfigUtil.get("keycloak.clientId")
        + "&client_secret="
        + ConfigUtil.get("keycloak.clientSecret")
        + "&code=" + URLEncoder.encode(code, "UTF-8") + "&redirect_uri="
        + URLEncoder.encode(ConfigUtil.get("keycloak.redirectUri"), "UTF-8");

    String tokenEndpoint =
      ConfigUtil.get("keycloak.url")
      + "/realms/"
      + ConfigUtil.get("keycloak.realm")
      + "/protocol/openid-connect/token";

    URL url = new URL(tokenEndpoint);

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    conn.setRequestMethod("POST");
    conn.setDoOutput(true);
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

    try (OutputStream os = conn.getOutputStream()) {
      os.write(form.getBytes("UTF-8"));
    }

    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

    String line;
    StringBuilder json = new StringBuilder();

    while ((line = br.readLine()) != null) {
      json.append(line);
    }

    try {
      JSONObject tokenResponse = new JSONObject(json.toString());
      String idToken = tokenResponse.getString("id_token");

      String[] parts = idToken.split("\\.");
      String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]), "UTF-8");
      JSONObject jwtPayload = new JSONObject(payload);
      String username = jwtPayload.getString("preferred_username");

      HttpSession session = request.getSession(true);
      session.setAttribute("id_token", idToken);
      Cookie accountCookie = ServletUtil.establishSession(username, session);

      if (accountCookie == null) {
        response.sendError(
            HttpServletResponse.SC_UNAUTHORIZED, "AltoroJ user not found: " + username);
        return;
      }
      
      response.addCookie(accountCookie);
      response.sendRedirect(request.getContextPath() + "/bank/main.jsp");

      return;
    } catch (JSONException e) {
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
