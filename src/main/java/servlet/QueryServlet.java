package servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractServlet {

    @Override
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String command = request.getParameter("command");
        doCommand(command, response);
    }

    private void doCommand(String command, HttpServletResponse response) throws IOException {
        switch (command) {
            case "max":
                doMax(response);
                break;
            case "min":
                doMin(response);
                break;
            case "sum":
                doSum(response);
                break;
            case "count":
                doCount(response);
                break;
            default:
                response.getWriter().println("Unknown command: " + command);
        }
    }

    private void doMax(HttpServletResponse response) throws IOException {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
                List<String> info = new ArrayList<>();

                info.add("<h1>Product with max price: </h1>");

                while (rs.next()) {
                    String  name = rs.getString("name");
                    int price  = rs.getInt("price");
                    info.add(name + "\t" + price + "</br>");
                }

                rs.close();
                stmt.close();

                addHttpInfo(response, info);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doMin(HttpServletResponse response) throws IOException {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
                List<String> info = new ArrayList<>();

                info.add("<h1>Product with min price: </h1>");

                while (rs.next()) {
                    String  name = rs.getString("name");
                    int price  = rs.getInt("price");
                    info.add(name + "\t" + price + "</br>");
                }

                rs.close();
                stmt.close();

                addHttpInfo(response, info);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doSum(HttpServletResponse response) throws IOException {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
                List<String> info = new ArrayList<>();

                info.add("Summary price: ");

                if (rs.next()) {
                    info.add(rs.getString(1));
                }

                rs.close();
                stmt.close();

                addHttpInfo(response, info);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doCount(HttpServletResponse response) throws IOException {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
                List<String> info = new ArrayList<>();

                info.add("Number of products: ");

                if (rs.next()) {
                    info.add(rs.getString(1));
                }

                rs.close();
                stmt.close();

                addHttpInfo(response, info);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}