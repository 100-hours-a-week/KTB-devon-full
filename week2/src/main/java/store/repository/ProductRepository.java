package store.repository;

import store.domain.product.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findByName(String name);
    List<Product> findAll();
    void save(Product product);
    boolean existsByName(String name);
    Optional<Product> reduceProductStock(String productName, int quantity);
}