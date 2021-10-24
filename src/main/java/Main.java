
import db.DBUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import product.ProductOpr;
import servlet.AddProductServlet;
import servlet.GetProductsServlet;
import servlet.QueryServlet;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        DBUtil.templeDB();

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ProductOpr productDAO = new ProductOpr();

        context.addServlet(new ServletHolder(new AddProductServlet(productDAO)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(productDAO)), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(productDAO)), "/query");

        server.start();
        server.join();
    }
}