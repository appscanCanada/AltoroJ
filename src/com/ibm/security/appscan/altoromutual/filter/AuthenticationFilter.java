package com.ibm.security.appscan.altoromutual.filter;

import com.ibm.security.appscan.altoromutual.util.ServletUtil;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class AuthenticationFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // Nothing to initialize
  }
  
  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;

    HttpServletResponse response = (HttpServletResponse) res;

    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute(ServletUtil.SESSION_ATTR_USER) == null) {
      response.sendRedirect(request.getContextPath() + "/oidc/login");

      return;
    }

    chain.doFilter(req, res);
  }

  @Override
  public void destroy() {
    // Nothing to clean up
  }
}
