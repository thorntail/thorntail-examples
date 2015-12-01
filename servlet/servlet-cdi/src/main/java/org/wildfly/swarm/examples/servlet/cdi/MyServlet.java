package org.wildfly.swarm.examples.servlet.cdi;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ken Finnigan
 */
@WebServlet(name = "MyServlet", urlPatterns = "/*")
public class MyServlet extends HttpServlet {

    @Inject
    Greeter greeter;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write(greeter.greet());
    }
}
