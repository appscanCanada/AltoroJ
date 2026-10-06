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

public class OidcCallbackServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String code = request.getParameter("code");

    String form = "grant_type=authorization_code"
        + "&client_id=altoro"
        + "&client_secret="
          + "ZFjUh4SGWLBj7OzuTb6H2ZrM9U3P9492JcloaoFvvSNIlfmRyQT3oTpLw5bbrj8XTmnVkx6Vywh1E09gErNByO"
        + "&code=" + URLEncoder.encode(code, "UTF-8") + "&redirect_uri="
        + URLEncoder.encode("http://localhost:8088/altoromutual/oidc/callback", "UTF-8");

    URL url = new URL("http://localhost:8085/realms/myrealm/protocol/openid-connect/token");

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

    HttpSession session = request.getSession(true);

    Cookie accountCookie = ServletUtil.establishSession("jsmith", session);

    response.addCookie(accountCookie);

    response.sendRedirect(request.getContextPath() + "/bank/main.jsp");
    response.getWriter().println(username);
  }
}
