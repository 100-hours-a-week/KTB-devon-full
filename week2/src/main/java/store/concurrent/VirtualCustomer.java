package store.concurrent;

import store.domain.order.Order;
import store.domain.order.ProductOrder;
import store.domain.product.Product;
import store.service.InventoryService;
import store.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class VirtualCustomer implements Runnable {
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final Random random;
    private volatile boolean running = true;

    public VirtualCustomer(InventoryService inventoryService, OrderService orderService) {
        this.inventoryService = inventoryService;
        this.orderService = orderService;
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
            // 1. 재고 조회
            List<Product> availableProducts = inventoryService.getProducts();
            if (availableProducts.isEmpty()) {
                return;
            }

            // 2. 랜덤 상품 선택
            Product selectedProduct = availableProducts.get(random.nextInt(availableProducts.size()));

            // 3. 재고가 있는지 확인
            if (selectedProduct.getStock() <= 0) {
                return;
            }

            // 4. 1 ~ 재고개수 중 랜덤 수량 선택
            int quantity = random.nextInt(selectedProduct.getStock()) + 1;

            // 5. 주문 생성 (재고 예약)
            List<ProductOrder> productOrders = new ArrayList<>();
            productOrders.add(new ProductOrder(selectedProduct.getName(), quantity));
            String orderId = orderService.createOrder(productOrders);

            // 6. 주문 확정 (구매 완료)
            orderService.confirmOrder(orderId);

            // 영수증은 가상 고객에게 불필요하므로 생략

        } catch (Exception e) {
            // 실패시 무시하고 다음 사이클에서 재시도
        }
    }

    public void stop() {
        this.running = false;
    }

}