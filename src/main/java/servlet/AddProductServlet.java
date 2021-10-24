package servlet;


import product.Product;
import product.ProductOpr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractServlet {

    private final ProductOpr productOpr;

    public AddProductServlet(ProductOpr productOpr) {
        super();
        this.productOpr = productOpr;
    }

    @Override
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product product = new Product(
                request.getParameter("name"),
                Long.parseLong(request.getParameter("price"))
        );

        productOpr.addProduct(product);

        response.getWriter().println("OK");
    }
}