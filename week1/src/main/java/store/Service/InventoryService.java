package store.Service;

import store.domain.product.Electronics;
import store.domain.product.FreshProduct;
import store.domain.product.LaptopProduct;
import store.domain.product.Product;
import store.domain.order.ProductOrder;
import store.domain.product.SmartphoneProduct;
import store.repository.InventoryManager;
import store.utils.ErrorMessages;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryService {

    private final InventoryManager inventoryManager;

    public InventoryService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    // 재고 확인 및 검증
    public void checkProductStock(List<ProductOrder> productOrders) {
        for (ProductOrder order : productOrders) {
            Product product = inventoryManager.getProduct(order.getProductName());
            if (!product.isStockAvailable(order.getQuantity())) {
                throw new IllegalArgumentException(ErrorMessages.OUT_OF_STOCK);
            }
        }
    }

    // 모든 제고 확인
    public List<Product> getProducts() {
        return inventoryManager.getAllProducts();
    }

    public List<FreshProduct> getFreshProducts() {
        return inventoryManager.getAllProducts().stream()
                .filter(product -> product instanceof FreshProduct)
                .map(product -> (FreshProduct) product)
                .collect(Collectors.toList());
    }

    public List<Electronics> getElectronicsProducts() {
        return inventoryManager.getAllProducts().stream()
                .filter(product -> product instanceof Electronics)
                .map(product -> (Electronics) product)
                .collect(Collectors.toList());
    }

    public List<FreshProduct> getExpiredProducts() {
        return getFreshProducts().stream()
                .filter(FreshProduct::isExpired)
                .collect(Collectors.toList());
    }

    public List<Electronics> getWarrantyProducts() {
        return getElectronicsProducts().stream()
                .filter(Electronics::hasWarranty)
                .collect(Collectors.toList());
    }

    public long countSmartphones() {
        return inventoryManager.getAllProducts().stream()
                .filter(product -> product instanceof SmartphoneProduct)
                .count();
    }

    public long countLaptops() {
        return inventoryManager.getAllProducts().stream()
                .filter(product -> product instanceof LaptopProduct)
                .count();
    }
}
