AltoroJ Keycloak Integration
Overview

This document describes the integration of IBM AltoroJ with Keycloak 26.8.0 using OpenID Connect (OIDC). Authentication is delegated entirely to Keycloak, while AltoroJ continues to manage customer accounts, balances, and banking functionality. The integration uses the OIDC Authorization Code Flow and maps authenticated Keycloak users directly to existing AltoroJ users.

Architecture
User
  ↓
Keycloak Login
  ↓
OidcCallbackServlet
  ↓
preferred_username
  ↓
ServletUtil.establishSession(username)
  ↓
AltoroJ Session
  ↓
/bank/*

Authentication Responsibilities
Component	ResponsibilityKeycloak	Authentication
AltoroJ Database	User Account Data
OidcCallbackServlet	User Mapping
ServletUtil.establishSession()	AltoroJ Session Creation
Prerequisites
Java 7
Apache Tomcat
AltoroJ
Keycloak 26.8.0
OpenID Connect (OIDC)
Keycloak Installation

Keycloak was installed using the ZIP distribution and started in development mode.

Windows
bin\kc.bat start-dev


The deployment followed the official Keycloak Getting Started guide.

Keycloak Configuration
Create Realm

Create a realm named:

myrealm


Realms provide isolated management boundaries for users, clients, and applications.

Create Users

Create users that correspond to users that already exist in the AltoroJ database.

Example:

jsmith
admin


Additional users may be added as needed.

Create OIDC Client

Create an OpenID Connect client:

Client ID: altoro
Client Type: OpenID Connect


Enable:

Standard Flow

Redirect URIs
Login Redirect URI
http://localhost:8088/altoromutual/oidc/callback

Post Logout Redirect URI
http://localhost:8088/altoromutual/index.jsp

AltoroJ Changes
New Components
OidcLoginServlet

Initiates OIDC authentication.

Responsibilities:

Builds Keycloak authorization URL
Redirects browser to Keycloak
Starts Authorization Code Flow
OidcCallbackServlet

Processes the callback from Keycloak.

Responsibilities:

Receives authorization code
Exchanges authorization code for tokens
Parses ID Token
Extracts preferred_username
Creates AltoroJ session
Handles unknown users
ConfigUtil

Centralized configuration management.

Responsibilities:

Loads values from keycloak.properties
Reads client secret from environment variable
userNotFound.jsp

Provides friendly error handling when a valid Keycloak user does not exist in the AltoroJ database.

Login Flow
Original AltoroJ Login
login.jsp
    ↓
LoginServlet
    ↓
DBUtil.isValidUser()
    ↓
AltoroJ Session

Keycloak Login
login.jsp
    ↓
OidcLoginServlet
    ↓
Keycloak Login Page
    ↓
OidcCallbackServlet
    ↓
preferred_username
    ↓
ServletUtil.establishSession()
    ↓
AltoroJ Session

Login Page Modification

The original login.jsp was modified to immediately redirect users into the Keycloak authentication flow.

<%
response.sendRedirect(
    request.getContextPath() + "/oidc/login");
return;
%>

User Mapping

Authentication is performed by Keycloak.

Authorization within AltoroJ is based on an exact username match.

Example
Keycloak User: jsmith
AltoroJ User:  jsmith

Keycloak User: admin
AltoroJ User:  admin


The Keycloak username must already exist within the AltoroJ database.

Unknown User Handling
Scenario
Keycloak User: rodney
AltoroJ User:  not found


Authentication succeeds in Keycloak but fails when AltoroJ attempts to locate the user.

Workflow
rodney
   ↓
Keycloak Login
   ↓
AltoroJ Lookup Fails
   ↓
Keycloak Logout
   ↓
userNotFound.jsp

User Experience

The following message is displayed:

Keycloak authentication succeeded, but the user
rodney
is not in the AltoroJ database.

Please contact your AltoroJ administrator.

Session Handling

When a user is not found:

AltoroJ session is terminated
Keycloak session is terminated
Future actions require reauthentication
Logout Integration
Previous Behavior
AltoroJ Session Removed

New Behavior
AltoroJ Session Invalidated
          ↓
Keycloak Logout Endpoint
          ↓
Redirect to index.jsp

Result

After logout:

User must authenticate again


No active Keycloak SSO session remains.

Configuration
keycloak.properties

Location:

src/keycloak.properties


Contents:

keycloak.url=http://localhost:8085
keycloak.realm=myrealm
keycloak.clientId=altoro
keycloak.redirectUri=http://localhost:8088/altoromutual/oidc/callback
keycloak.postLogoutRedirectUri=http://localhost:8088/altoromutual/index.jsp

Environment Variable

The Keycloak client secret is intentionally excluded from source control.

Windows
setx KEYCLOAK_CLIENT_SECRET "<client-secret>"

Usage
System.getenv("KEYCLOAK_CLIENT_SECRET")

Files Modified
WebContent/login.jsp

WebContent/userNotFound.jsp

src/com/ibm/security/appscan/altoromutual/servlet/
├── LoginServlet.java
├── OidcLoginServlet.java
└── OidcCallbackServlet.java

src/com/ibm/security/appscan/altoromutual/util/
├── ServletUtil.java
└── ConfigUtil.java

src/keycloak.properties

Testing
Valid User
User
jsmith

Expected Result
Authenticated
Redirected to bank/main.jsp

Admin User
User
admin

Expected Result
Authenticated
Admin functionality available

Unknown User
User
rodney

Expected Result
Authentication succeeds
User mapping fails
Friendly error page displayed
Keycloak session terminated
AltoroJ session terminated

Logout
Expected Result
AltoroJ logout succeeds
Keycloak logout succeeds
User redirected to index.jsp

Summary

This integration modernizes AltoroJ authentication by delegating identity management to Keycloak while preserving the existing AltoroJ account and banking model.

Benefits
Single Sign-On (SSO)
Centralized Identity Management
OIDC Standards-Based Authentication
Existing AltoroJ Database Preserved
Friendly Handling of Unknown Users
Full Logout Integration
No Client Secret Stored in Source Control

The result is a complete OIDC-based authentication solution for AltoroJ using Keycloak 26.8.0.
