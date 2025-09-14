package store.domain.product;

public abstract class Product {
    protected String name;
    protected int price;
    protected int stock;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
        this.stock = 0;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isStockAvailable(int required) {
        return required <= stock;
    }

    public void reduceStock(int quantity) {
        if (quantity > stock) {
            throw new IllegalArgumentException("재고 부족");
        }
        this.stock -= quantity;
    }

    @Override
    public String toString() {
        if (stock > 0) {
            return String.format("- %s %,d원 %d개", name, price, stock);
        }
        return String.format("- %s %,d원 재고 없음", name, price);
    }
}