package store.concurrent;

import store.domain.product.Product;
import store.service.InventoryService;

import java.util.List;
import java.util.Random;

public class VirtualSupplier implements Runnable {
    private final InventoryService inventoryService;
    private final Random random;
    private volatile boolean running = true;

    public VirtualSupplier(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (running) {
            try {
                // 10초 대기
                Thread.sleep(10000);

                // 재고 보충 시도
                attemptRestock();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void attemptRestock() {
        try {
            // 재고 0인 상품들 조회
            List<Product> outOfStockProducts = inventoryService.getOutOfStockProducts();

            // 재고 0인 상품이 있으면 보충
            for (Product product : outOfStockProducts) {
                // 3~30개 랜덤 보충
                int restockQuantity = random.nextInt(28) + 3; // 3~30
                inventoryService.restockProduct(product.getName(), restockQuantity);
            }

        } catch (Exception e) {
            // 실패시 무시하고 다음 사이클에서 재시도
        }
    }

    public void stop() {
        this.running = false;
    }
}