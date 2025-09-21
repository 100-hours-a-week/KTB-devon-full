package store.service;

import store.domain.product.Electronics;
import store.domain.product.FreshProduct;
import store.domain.product.LaptopProduct;
import store.domain.product.Product;
import store.domain.order.ProductOrder;
import store.domain.product.SmartphoneProduct;
import store.repository.ProductRepository;
import store.utils.ErrorMessages;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryService {

    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 재고 확인 및 검증
    public void checkProductStock(List<ProductOrder> productOrders) {
        for (ProductOrder order : productOrders) {
            Product product = productRepository.findByName(order.getProductName())
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PRODUCT_NOT_FOUND));
            if (!product.isStockAvailable(order.getQuantity())) {
                throw new IllegalArgumentException(ErrorMessages.OUT_OF_STOCK);
            }
        }
    }

    // 모든 제고 확인
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<FreshProduct> getFreshProducts() {
        return productRepository.findAll().stream()
                .filter(product -> product instanceof FreshProduct)
                .map(product -> (FreshProduct) product)
                .collect(Collectors.toList());
    }

    public List<Electronics> getElectronicsProducts() {
        return productRepository.findAll().stream()
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
        return productRepository.findAll().stream()
                .filter(product -> product instanceof SmartphoneProduct)
                .count();
    }

    public long countLaptops() {
        return productRepository.findAll().stream()
                .filter(product -> product instanceof LaptopProduct)
                .count();
    }
}
