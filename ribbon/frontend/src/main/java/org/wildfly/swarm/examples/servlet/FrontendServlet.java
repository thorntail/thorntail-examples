package org.wildfly.swarm.examples.servlet;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/*")
public class FrontendServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/")) {
            pathInfo = "/index.html";
        } 
        URL url = Thread.currentThread().getContextClassLoader().getResource(pathInfo);
        try (InputStream is = url.openStream()) {                 
            copy(Channels.newChannel(is), Channels.newChannel(resp.getOutputStream())); 
        }
        
    }
    private static void copy(ReadableByteChannel src, WritableByteChannel dest) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
        
        while (src.read(buffer) != -1) {
            buffer.flip();
            dest.write(buffer);
            buffer.compact();
        }
        
        buffer.flip();
        
        while(buffer.hasRemaining()) {
            dest.write(buffer);
        }
    }
}
