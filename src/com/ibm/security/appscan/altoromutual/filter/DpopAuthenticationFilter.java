package com.ibm.security.appscan.altoromutual.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class DpopAuthenticationFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // nothing for now
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;

    String dpop = req.getHeader("DPoP");

    String auth = req.getHeader("Authorization");

    System.out.println("Authorization = " + auth);

    System.out.println("DPoP = " + dpop);

    chain.doFilter(request, response);
  }
  @Override
  public void destroy() {
    // nothing for now
  }
}
