package servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public abstract class AbstractServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            doRequest(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("test/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected abstract void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException;

    protected void addHttpInfo(HttpServletResponse response, List<String> info) throws IOException {
        response.getWriter().println("<html><body>");
        info.forEach(s -> {
            try {
                response.getWriter().println(s);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        response.getWriter().println("</body></html>");
    }
}