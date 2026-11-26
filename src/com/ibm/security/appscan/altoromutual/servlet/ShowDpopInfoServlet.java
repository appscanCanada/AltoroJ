package com.ibm.security.appscan.altoromutual.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.security.appscan.altoromutual.model.User;

public class ShowDpopInfoServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
            request.getSession(false);

        response.setContentType("text/plain");

        if (session == null) {

            response.getWriter().println(
                "No active session"
            );

            return;
        }

        User user =
            (User) session.getAttribute("user");

        response.getWriter().println(
            "User: "
            + (user == null
                ? "(none)"
                : user.getUsername())
        );

        response.getWriter().println(
            "Session ID: "
            + session.getId()
        );

        response.getWriter().println(
            "DPoP JKT: "
            + session.getAttribute("dpop_jkt")
        );
    }
}
