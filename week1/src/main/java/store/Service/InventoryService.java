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

    // 재고 업데이트 비즈니스 로직
    public void updateProductStock(String productName, int quantity) {
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PRODUCT_NOT_FOUND));

        try {
            product.reduceStock(quantity);
            productRepository.save(product);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErrorMessages.OUT_OF_STOCK);
        }
    }
}
