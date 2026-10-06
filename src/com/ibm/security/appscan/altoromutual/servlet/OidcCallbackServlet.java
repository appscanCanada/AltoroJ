package com.ibm.security.appscan.altoromutual.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OidcCallbackServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");

        response.setContentType("text/plain");

        if (code == null) {
            response.getWriter().println("NO CODE RECEIVED");
        } else {
            response.getWriter().println("CODE RECEIVED");
            response.getWriter().println(code);
        }
    }
}
