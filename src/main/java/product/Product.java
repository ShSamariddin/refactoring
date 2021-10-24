package product;

public class Product {
    private final String name;
    private final long price;

    public Product(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public long getPrice() {
        return this.price;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Product product = (Product) object;
        return this.price == product.getPrice() && this.name.equals(product.getName());
    }

    public String toHttpString() {
        return this.name + "\t" + this.price + "</br>";
    }
}
