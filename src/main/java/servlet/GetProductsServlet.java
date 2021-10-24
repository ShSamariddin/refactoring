package servlet;

import product.Product;
import product.ProductOpr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractServlet {

    private final ProductOpr productOpr;

    public GetProductsServlet(ProductOpr productOpr) {
        this.productOpr = productOpr;
    }

    @Override
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Product> products = productOpr.getProduct();
        List<String> info = products.stream().map(Product::toHttpString).collect(Collectors.toList());

        addHttpInfo(response, info);
    }
}