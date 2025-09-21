package store.infra;

import store.domain.product.Product;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDatabase {
    private final Map<String, Product> productTable;

    public InMemoryDatabase() {
        this.productTable = new ConcurrentHashMap<>();
    }

    public void insertProduct(Product product) {
        productTable.put(product.getName(), product);
    }

    public Optional<Product> findProductByName(String name) {
        return Optional.ofNullable(productTable.get(name));
    }

    public List<Product> findAllProducts() {
        return new ArrayList<>(productTable.values());
    }

    public void updateProduct(Product product) {
        productTable.put(product.getName(), product);
    }

    public boolean existsProduct(String name) {
        return productTable.containsKey(name);
    }

    public void clearAll() {
        productTable.clear();
    }

    public int size() {
        return productTable.size();
    }

    // 원자적 연산을 위한 메서드들 (개별 키 락 사용)
    public void saveProduct(Product product) {
        productTable.put(product.getName(), product);
    }

    public Optional<Product> reduceProductStock(String productName, int quantity) {
        Product result = productTable.compute(productName, (key, product) -> {
            if (product != null) {
                product.reduceStock(quantity);
                return product;
            }
            return null;
        });
        return Optional.ofNullable(result);
    }
}