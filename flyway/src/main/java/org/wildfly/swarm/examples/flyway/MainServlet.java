package org.wildfly.swarm.examples.flyway;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@WebServlet(urlPatterns = "/")
public class MainServlet extends HttpServlet {

    @Resource(mappedName = "java:jboss/datasources/ExampleDS")
    DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("<html><body><h1>Data from PERSON table</h1>");
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM PERSON");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                writer.printf("<li>%s - %s</li>", rs.getInt("ID"), rs.getString("NAME"));
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
        writer.println("</ul></body></html>");
    }
}
