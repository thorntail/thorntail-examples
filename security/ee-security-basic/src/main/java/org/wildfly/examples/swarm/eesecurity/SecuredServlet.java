package org.wildfly.examples.swarm.eesecurity;

import java.io.IOException;
import javax.annotation.security.DeclareRoles;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Secured servlet, only accessible to authenticated users.
 */
@BasicAuthenticationMechanismDefinition
@WebServlet("/servlet")
@DeclareRoles({"user", "admin"})
@ServletSecurity(@HttpConstraint( rolesAllowed = {"user", "admin"}))
public class SecuredServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getOutputStream().println("Hello " + req.getUserPrincipal().getName());
    }
}
