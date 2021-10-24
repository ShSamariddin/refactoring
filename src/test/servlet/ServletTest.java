package servlet;

import db.DBUtil;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import product.Product;
import product.ProductOpr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServletTest {

    private static void sqlRequest(String request) {
        try (Statement stmt = DBUtil.getStatement()) {
            stmt.executeUpdate(request);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void initData() throws SQLException {
        sqlRequest("DROP TABLE IF EXISTS PRODUCT");

        DBUtil.templeDB();
        sqlRequest("INSERT INTO product (name, price) VALUES (\"Audi\", 3000000)");
        sqlRequest("INSERT INTO product (name, price) VALUES (\"BMW\", 4000000)");
        sqlRequest("INSERT INTO product (name, price) VALUES (\"Mercedes\", 5000000)");
    }

    @Mock
    private HttpServletRequest requestMock;
    @Mock
    private HttpServletResponse responseMock;
    @Mock
    private ProductOpr productOprMock;

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

        new AddProductServlet(productOprMock).doGet(requestMock, responseMock);
        Mockito.verify(requestMock, Mockito.atLeastOnce()).getParameter("name");
        Mockito.verify(requestMock, Mockito.atLeastOnce()).getParameter("price");

        String result = stringWriter.toString();
        assertTrue(result.contains("OK"));
    }

    @Test
    @DisplayName("Check product list test")
    public void getProductServletTest() throws IOException {
        Mockito.when(productOprMock.getProduct()).thenReturn(List.of(
                new Product("Audi", 3000000),
                new Product("BMW", 4000000),
                new Product("Mercedes", 5000000),
                new Product("Tesla", 6000000)
        ));
        Mockito.when(responseMock.getWriter()).thenReturn(printWriter);

        new GetProductsServlet(productOprMock).doGet(requestMock, responseMock);
        String result = stringWriter.toString();
        assertTrue(result.contains("Tesla\t6000000"));
        assertTrue(result.contains("Audi\t3000000"));
        assertTrue(result.contains("BMW\t4000000"));
        assertTrue(result.contains("Mercedes\t5000000"));
    }

    @Nested
    @DisplayName("QueryServlet tests")
    public class queryServletTest {
        String makeRequest(String command) throws IOException {
            Mockito.when(requestMock.getParameter("command")).thenReturn(command);
            Mockito.when(responseMock.getWriter()).thenReturn(printWriter);
            new QueryServlet(productOprMock).doGet(requestMock, responseMock);
            Mockito.verify(requestMock, Mockito.atLeastOnce()).getParameter("command");
            return stringWriter.toString();
        }

        @Test
        @DisplayName("Test sum")
        public void testSum() throws IOException {
            Mockito.when(productOprMock.sumProductPrice()).thenReturn(18000000L);
            String result = makeRequest("sum");
            assertTrue(result.contains("Summary price"));
            assertTrue(result.contains("18000000"));
        }

        @Test
        @DisplayName("Test count")
        public void testCount() throws IOException {
            Mockito.when(productOprMock.countProducts()).thenReturn(4L);
            String result = makeRequest("count");
            assertTrue(result.contains("Number of products"));
            assertTrue(result.contains("4"));
        }

        @Test
        @DisplayName("Test unknown")
        public void testUnknown() throws IOException {
            String result = makeRequest("invalid_command");
            assertTrue(result.contains("Unknown command"));
            assertTrue(result.contains("invalid_command"));
        }

        @Test
        @DisplayName("Car with max price")
        public void testMin() throws IOException {
            Mockito.when(productOprMock.findMinProduct())
                    .thenReturn(new Product("Audi", 3000000));
            String result = makeRequest("min");
            assertTrue(result.contains("min price"));
            assertTrue(result.contains("Audi\t3000000"));
        }

        @Test
        @DisplayName("Test max")
        public void testMax() throws IOException {
            Mockito.when(productOprMock.findMaxProduct())
                    .thenReturn(new Product("Tesla", 6000000));
            String result = makeRequest("max");
            assertTrue(result.contains("max price"));
            assertTrue(result.contains("Tesla\t6000000"));
            ;
        }
    }

}