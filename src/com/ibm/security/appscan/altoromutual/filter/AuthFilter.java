package com.ibm.security.appscan.altoromutual.filter;

import com.ibm.security.appscan.altoromutual.model.User;
import com.ibm.security.appscan.altoromutual.util.ServletUtil;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

/**
 * This class ensures that the user is authenticated when they try to access
 * account section of the site, and redirects to the login page if they are not.
 *
 */
public class AuthFilter implements Filter {
  /* (non-Java-doc)
   * @see java.lang.Object#Object()
   */
  public AuthFilter() {
    super();
  }

  /* (non-Java-doc)
   * @see javax.servlet.Filter#init(FilterConfig arg0)
   */
  public void init(FilterConfig arg0) throws ServletException {
    // do nothing
  }

  /* (non-Java-doc)
   * @see javax.servlet.Filter#doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
   */
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
      throws IOException, ServletException {
    if (req instanceof HttpServletRequest) {
      HttpServletRequest request = (HttpServletRequest) req;

      Object user = request.getSession().getAttribute(ServletUtil.SESSION_ATTR_USER);
      javax.servlet.http.HttpSession session = request.getSession(false);
      String sessionToken =
          (session == null) ? null : (String) session.getAttribute("access_token");
      String authorization = request.getHeader("Authorization");
      String dpop = request.getHeader("DPoP");

      if (user == null || !(user instanceof User)) {
        ((HttpServletResponse) resp).sendRedirect(request.getContextPath() + "/login.jsp");

        return;
      }
      if (authorization == null || !authorization.startsWith("DPoP ")) {
        ((HttpServletResponse) resp).sendError(401, "Authorization header required");

        return;
      }

      if (dpop == null) {
        ((HttpServletResponse) resp).sendError(401, "DPoP header required");

        return;
      }

      String presentedToken = authorization.substring("DPoP ".length());
      if (sessionToken == null) {
        ((HttpServletResponse) resp).sendError(401, "No session access token");

        return;
      }

      JwtClaims claims;

      try {
        JwtConsumer consumer = new JwtConsumerBuilder()
                                   .setSkipAllValidators()
                                   .setDisableRequireSignature()
                                   .setSkipSignatureVerification()
                                   .build();

        claims = consumer.processToClaims(presentedToken);

      } catch (Exception e) {
        ((HttpServletResponse) resp).sendError(401, "Invalid JWT");
        return;
      }

      String username;
      try {
        username = claims.getStringClaimValue("preferred_username");
      } catch (MalformedClaimException e) {
        ((HttpServletResponse) resp).sendError(401, "Invalid preferred_username claim");

        return;
      }

      if (username == null || username.trim().isEmpty()) {
        ((HttpServletResponse) resp).sendError(401, "preferred_username missing");

        return;
      }

      User sessionUser = (User) request.getSession().getAttribute(ServletUtil.SESSION_ATTR_USER);

      if (sessionUser == null) {
        ((HttpServletResponse) resp).sendError(401, "No authenticated session user");

        return;
      }

      if (!sessionUser.getUsername().equalsIgnoreCase(username)) {
        ((HttpServletResponse) resp).sendError(401, "Token user mismatch");

        return;
      }
      chain.doFilter(req, resp);
    }
  }

  /* (non-Java-doc)
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy() {
    // do nothing
  }
}
