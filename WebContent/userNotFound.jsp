<%@ page language="java"
    contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<jsp:include page="header.jspf"/>

<div id="wrapper" style="width:99%;">
    <jsp:include page="/toc.jspf"/>

    <div class="fl" style="width:99%;">
        <h1>Access Denied</h1>

        <p style="color:red;font-weight:bold;">
            Keycloak authentication succeeded, but the user
            <b><%= request.getParameter("user") %></b>
            is not in the AltoroJ database.
        <br>
        <br>
            Please contact your AltoroJ administrator.
        </p>
    </div>
</div>
