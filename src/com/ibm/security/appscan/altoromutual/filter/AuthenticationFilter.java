package com.ibm.security.appscan.altoromutual.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import com.ibm.security.appscan.altoromutual.util.ServletUtil;

public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest req,
            ServletResponse res,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request =
                (HttpServletRequest) req;

        HttpServletResponse response =
                (HttpServletResponse) res;

        HttpSession session =
                request.getSession(false);

        if (session == null ||
            session.getAttribute(
                ServletUtil.SESSION_ATTR_USER) == null) {

            response.sendRedirect(
                request.getContextPath()
                + "/oidc/login");

            return;
        }

        chain.doFilter(req, res);
    }
}
