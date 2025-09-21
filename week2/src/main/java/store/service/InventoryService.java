package store.service;

import store.domain.product.Product;
import store.domain.order.ProductOrder;
import store.repository.ProductRepository;
import store.utils.ErrorMessages;
import store.event.EventPublisher;
import store.event.StockEvent;
import store.event.StockEventType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InventoryService {

    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.eventPublisher = EventPublisher.getInstance();
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

        // 재고 감소 후 품절 체크
        Product product = result.get();
        if (product.getStock() == 0) {
            StockEvent event = new StockEvent(productName, 0, StockEventType.OUT_OF_STOCK);
            eventPublisher.publishStockEvent(event);
        }
    }

    // 재고 보충
    public synchronized void restockProduct(String productName, int quantity) {
        Optional<Product> productOpt = productRepository.findByName(productName);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            boolean wasOutOfStock = product.getStock() == 0;

            product.addStock(quantity);
            productRepository.save(product);

            // 품절 상태에서 재입고된 경우 이벤트 발행
            if (wasOutOfStock && quantity > 0) {
                StockEvent event = new StockEvent(productName, product.getStock(), StockEventType.RESTOCKED);
                eventPublisher.publishStockEvent(event);
            }
        }
    }

    // 재고 0인 상품들 조회
    public synchronized List<Product> getOutOfStockProducts() {
        return productRepository.findAll().stream()
                .filter(product -> product.getStock() == 0)
                .collect(Collectors.toList());
    }

    // 재고 예약 (주문 생성시 사용)
    public synchronized void reserveStock(List<ProductOrder> productOrders) {
        checkProductStock(productOrders);
        for (ProductOrder order : productOrders) {
            updateProductStock(order.getProductName(), order.getQuantity());
        }
    }

    // 재고 예약 취소 (주문 취소시 사용)
    public synchronized void cancelReservation(List<ProductOrder> productOrders) {
        for (ProductOrder order : productOrders) {
            restockProduct(order.getProductName(), order.getQuantity());
        }
    }
}
