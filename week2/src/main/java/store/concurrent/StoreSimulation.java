package store.concurrent;

import store.infra.ThreadPoolManager;
import store.service.CheckoutService;
import store.service.InventoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class StoreSimulation {
    private final ThreadPoolManager threadPoolManager;
    private final CheckoutService checkoutService;
    private final InventoryService inventoryService;
    private final List<VirtualCustomer> customers;
    private VirtualSupplier supplier;

    public StoreSimulation(ThreadPoolManager threadPoolManager, CheckoutService checkoutService, InventoryService inventoryService) {
        this.threadPoolManager = threadPoolManager;
        this.checkoutService = checkoutService;
        this.inventoryService = inventoryService;
        this.customers = new ArrayList<>();
    }

    public void startSimulation(int customerCount) {
        ExecutorService executor = threadPoolManager.getExecutorService();

        // 가상 고객들 시작
        for (int i = 1; i <= customerCount; i++) {
            VirtualCustomer customer = new VirtualCustomer(checkoutService, inventoryService);
            customers.add(customer);
            executor.submit(customer);
        }

        // 가상 발주업체 시작
        supplier = new VirtualSupplier(inventoryService);
        executor.submit(supplier);
    }

    public void stopSimulation() {
        // 모든 고객 중지
        for (VirtualCustomer customer : customers) {
            customer.stop();
        }

        // 발주업체 중지
        if (supplier != null) {
            supplier.stop();
        }

    }

    public int getActiveCustomerCount() {
        return customers.size();
    }

    public boolean isSupplierRunning() {
        return supplier != null;
    }
}