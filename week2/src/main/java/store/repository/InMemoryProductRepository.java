package store.repository;

import store.domain.product.Product;
import store.infra.InMemoryDatabase;
import store.utils.ErrorMessages;
import java.util.List;
import java.util.Optional;

public class InMemoryProductRepository implements ProductRepository {
    private final InMemoryDatabase database;

    public InMemoryProductRepository(InMemoryDatabase database) {
        this.database = database;
    }

    @Override
    public Optional<Product> findByName(String name) {
        return database.findProductByName(name);
    }

    @Override
    public List<Product> findAll() {
        return database.findAllProducts();
    }

    @Override
    public void save(Product product) {
        if (database.existsProduct(product.getName())) {
            database.updateProduct(product);
        } else {
            database.insertProduct(product);
        }
    }

    @Override
    public boolean existsByName(String name) {
        return database.existsProduct(name);
    }
}