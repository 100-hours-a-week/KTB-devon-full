package store.repository;

import store.domain.product.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findByName(String name);
    List<Product> findAll();
    void save(Product product);
    void updateStock(String productName, int quantity);
    boolean existsByName(String name);
}