package servlet;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServletTest {

    private static void sqlRequest(String request) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = connection.createStatement();

            statement.executeUpdate(request);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void initData() {
        sqlRequest("DROP TABLE IF EXISTS product");
        sqlRequest("CREATE TABLE IF NOT EXISTS product" +
                "(id    INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " name  TEXT    NOT NULL, " +
                " price INT     NOT NULL)");
        sqlRequest("INSERT INTO product (name, price) VALUES (\"Audi\", 3000000)");
        sqlRequest("INSERT INTO product (name, price) VALUES (\"BMW\", 4000000)");
        sqlRequest("INSERT INTO product (name, price) VALUES (\"Mercedes\", 5000000)");
    }

    @Mock
    private HttpServletRequest requestMock;
    @Mock
    private HttpServletResponse responseMock;

    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    @DisplayName("Add car list")
    public void addProductTest() throws IOException {
        Mockito.when(requestMock.getParameter("name")).thenReturn("Tesla");
        Mockito.when(requestMock.getParameter("price")).thenReturn("6000000");
        Mockito.when(responseMock.getWriter()).thenReturn(printWriter);

        new AddProductServlet().doGet(requestMock, responseMock);

        Mockito.verify(requestMock, Mockito.atLeastOnce()).getParameter("name");
        Mockito.verify(requestMock, Mockito.atLeastOnce()).getParameter("price");

        String result = stringWriter.toString();
        assertTrue(result.contains("OK"));
    }

    @Test
    @DisplayName("Check product list test")
    public void getProductServletTest() throws IOException {
        Mockito.when(responseMock.getWriter()).thenReturn(printWriter);

        new GetProductsServlet().doGet(requestMock, responseMock);

        String result = stringWriter.toString();
        assertTrue(result.contains("Tesla"));
        assertTrue(result.contains("6000000"));
        assertTrue(result.contains("Audi"));
        assertTrue(result.contains("3000000"));
        assertTrue(result.contains("BMW"));
        assertTrue(result.contains("4000000"));
        assertTrue(result.contains("Mercedes"));
        assertTrue(result.contains("5000000"));
    }

}