package store.repository.impl;

import store.domain.product.Product;
import store.infra.InMemoryDatabase;
import store.repository.ProductRepository;
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
        database.saveProduct(product);
    }

    @Override
    public boolean existsByName(String name) {
        return database.existsProduct(name);
    }

    public Optional<Product> reduceProductStock(String productName, int quantity) {
        return database.reduceProductStock(productName, quantity);
    }
}