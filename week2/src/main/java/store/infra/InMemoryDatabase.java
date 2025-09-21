package store.infra;

import store.domain.product.Product;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InMemoryDatabase {
    private final Map<String, Product> productTable;
    private final ReadWriteLock lock;

    public InMemoryDatabase() {
        this.productTable = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public void insertProduct(Product product) {
        lock.writeLock().lock();
        try {
            productTable.put(product.getName(), product);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Optional<Product> findProductByName(String name) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(productTable.get(name));
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Product> findAllProducts() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(productTable.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    public void updateProduct(Product product) {
        lock.writeLock().lock();
        try {
            productTable.put(product.getName(), product);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean existsProduct(String name) {
        lock.readLock().lock();
        try {
            return productTable.containsKey(name);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void clearAll() {
        lock.writeLock().lock();
        try {
            productTable.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return productTable.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    // 원자적 연산을 위한 메서드들
    public void saveProduct(Product product) {
        lock.writeLock().lock();
        try {
            productTable.put(product.getName(), product);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Optional<Product> reduceProductStock(String productName, int quantity) {
        lock.writeLock().lock();
        try {
            Product product = productTable.get(productName);
            if (product != null) {
                product.reduceStock(quantity);
                productTable.put(productName, product);
                return Optional.of(product);
            }
            return Optional.empty();
        } finally {
            lock.writeLock().unlock();
        }
    }
}