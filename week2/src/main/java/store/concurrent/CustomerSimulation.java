package store.concurrent;

import store.infra.ThreadPoolManager;
import store.service.CheckoutService;
import store.service.InventoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class CustomerSimulation {
    private final ThreadPoolManager threadPoolManager;
    private final CheckoutService checkoutService;
    private final InventoryService inventoryService;
    private final List<VirtualCustomer> customers;

    public CustomerSimulation(CheckoutService checkoutService, InventoryService inventoryService) {
        this.threadPoolManager = new ThreadPoolManager();
        this.checkoutService = checkoutService;
        this.inventoryService = inventoryService;
        this.customers = new ArrayList<>();
    }

    public void startSimulation(int customerCount) {
        ExecutorService executor = threadPoolManager.getExecutorService();

        // 가상 고객들 생성 및 실행
        for (int i = 1; i <= customerCount; i++) {
            VirtualCustomer customer = new VirtualCustomer( checkoutService, inventoryService);
            customers.add(customer);
            executor.submit(customer);
        }
    }

    public void stopSimulation() {
        // 모든 고객 중지
        for (VirtualCustomer customer : customers) {
            customer.stop();
        }

        // 스레드 풀 종료
        threadPoolManager.shutdown();
    }

    public int getActiveCustomerCount() {
        return customers.size();
    }
}