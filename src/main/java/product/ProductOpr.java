package product;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductOpr {

    private Statement getStatement() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:test.db").createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addProduct(Product product) {
        try (Statement statement = getStatement()) {
            String sql = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES  (\"" + product.getName() + "\"," + product.getPrice() + ")";

            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getProduct() {
        try (Statement statement = getStatement()) {
            String sql = "SELECT * FROM PRODUCT";
            ResultSet rs = statement.executeQuery(sql);

            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                String  name = rs.getString("name");
                long price  = rs.getLong("price");
                products.add(new Product(name, price));
            }

            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product findMaxProduct() {
        try (Statement statement = getStatement()) {
            String sql = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
            ResultSet rs = statement.executeQuery(sql);
            List<Product> products = new ArrayList<>();

            while (rs.next()) {
                String  name = rs.getString("name");
                long price  = rs.getLong("price");

                products.add(new Product(name, price));
            }

            return products.get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product findMinProduct() {
        try (Statement statement = getStatement()) {
            String sql = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
            ResultSet rs = statement.executeQuery(sql);
            List<Product> products = new ArrayList<>();

            while (rs.next()) {
                String  name = rs.getString("name");
                long price  = rs.getLong("price");

                products.add(new Product(name, price));
            }

            return products.get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long sumProductPrice() {
        try (Statement statement = getStatement()) {
            String sql = "SELECT SUM(PRICE) AS RES FROM PRODUCT";
            ResultSet rs = statement.executeQuery(sql);

            return rs.getLong("res");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long countProducts() {
        try (Statement statement = getStatement()) {
            String sql = "SELECT COUNT(*) AS RES FROM PRODUCT";
            ResultSet rs = statement.executeQuery(sql);

            return rs.getLong("res");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
