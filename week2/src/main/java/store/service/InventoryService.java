package store.service;

import store.domain.product.Product;
import store.domain.order.ProductOrder;
import store.repository.ProductRepository;
import store.utils.ErrorMessages;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InventoryService {

    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 재고 확인 및 검증
    public synchronized void checkProductStock(List<ProductOrder> productOrders) {
        for (ProductOrder order : productOrders) {
            Product product = productRepository.findByName(order.getProductName())
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PRODUCT_NOT_FOUND));
            if (!product.isStockAvailable(order.getQuantity())) {
                throw new IllegalArgumentException(ErrorMessages.OUT_OF_STOCK);
            }
        }
    }

    // 모든 재고 확인
    public synchronized List<Product> getProducts() {
        return productRepository.findAll();
    }

    // 재고 감소
    public synchronized void updateProductStock(String productName, int quantity) {
        Optional<Product> result = productRepository.reduceProductStock(productName, quantity);
        if (result.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.PRODUCT_NOT_FOUND);
        }
    }

    // 재고 보충
    public synchronized void restockProduct(String productName, int quantity) {
        Optional<Product> productOpt = productRepository.findByName(productName);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.addStock(quantity);
            productRepository.save(product);
        }
    }

    // 재고 0인 상품들 조회
    public synchronized List<Product> getOutOfStockProducts() {
        return productRepository.findAll().stream()
                .filter(product -> product.getStock() == 0)
                .collect(Collectors.toList());
    }
}
