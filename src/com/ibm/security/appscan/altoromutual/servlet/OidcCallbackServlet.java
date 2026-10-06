package com.ibm.security.appscan.altoromutual.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OidcCallbackServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String code = request.getParameter("code");

    String form = "grant_type=authorization_code"
        + "&client_id=altoro"
        + "&client_secret=YOUR_SECRET"
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

    response.setContentType("application/json");
    response.getWriter().print(json.toString());
  }
}
