package store.concurrent;

import store.domain.order.Order;
import store.domain.order.ProductOrder;
import store.domain.product.Product;
import store.service.CheckoutService;
import store.service.InventoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VirtualCustomer implements Runnable {
    private final CheckoutService checkoutService;
    private final InventoryService inventoryService;
    private final Random random;
    private volatile boolean running = true;

    public VirtualCustomer(CheckoutService checkoutService, InventoryService inventoryService) {
        this.checkoutService = checkoutService;
        this.inventoryService = inventoryService;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (running) {
            try {
                // 1-3초 대기
                Thread.sleep((random.nextInt(3) + 1) * 1000);

                // 구매 시도
                attemptPurchase();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void attemptPurchase() {
        try {
            // 재고 조회
            List<Product> availableProducts = inventoryService.getProducts();
            if (availableProducts.isEmpty()) {
                return;
            }

            // 랜덤 상품 선택
            Product selectedProduct = availableProducts.get(random.nextInt(availableProducts.size()));

            // 재고가 있는지 확인
            if (selectedProduct.getStock() <= 0) {
                return;
            }

            // 1 ~ 재고개수 중 랜덤 수량 구매
            int quantity = random.nextInt(selectedProduct.getStock()) + 1;

            // 주문 생성
            List<ProductOrder> productOrders = new ArrayList<>();
            productOrders.add(new ProductOrder(selectedProduct.getName(), quantity));
            Order order = new Order(productOrders);

            // 주문 처리
            checkoutService.processOrder(order);

        } catch (Exception e) {
            // 실패시 무시하고 다음 사이클에서 재시도
        }
    }

    public void stop() {
        this.running = false;
    }

}