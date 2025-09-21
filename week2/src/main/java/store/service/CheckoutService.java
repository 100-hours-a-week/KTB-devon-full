package store.service;

import store.domain.order.Order;
import store.domain.product.Product;
import store.domain.order.ProductOrder;
import store.domain.order.Receipt;
import store.domain.product.ReceiptProduct;
import store.repository.ProductRepository;
import store.utils.ErrorMessages;

import java.util.ArrayList;
import java.util.List;

public class CheckoutService {

    private final ProductRepository productRepository;

    public CheckoutService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public synchronized Receipt generateReceipt(Order order) {
        validateOrder(order);

        try {
            return createReceipt(order);
        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorMessages.RECEIPT_GENERATION_FAILED + ": " + e.getMessage());
        }
    }

    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_INPUT);
        }

        if (!order.isConfirmed()) {
            throw new IllegalArgumentException(ErrorMessages.ORDER_NOT_CONFIRMED);
        }

        if (order.getTotalProductOrders() == null || order.getTotalProductOrders().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.NO_ORDER_PRODUCTS);
        }
    }

    private Receipt createReceipt(Order order) {
        List<ReceiptProduct> productOrders = mappingReceiptProduct(order.getTotalProductOrders());
        int totalAmount = calculateAmount(productOrders);
        int finalAmount = totalAmount;
        return new Receipt(
                productOrders,
                totalAmount,
                finalAmount
        );
    }

    private  List<ReceiptProduct> mappingReceiptProduct(List<ProductOrder> orders){
        List<ReceiptProduct> products = new ArrayList<>();
        for(ProductOrder order : orders){
            Product product = productRepository.findByName(order.getProductName())
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PRODUCT_NOT_FOUND));

            products.add(new ReceiptProduct(product.getName(), order.getQuantity(), product.getPrice()));
        }
        return products;
    }

    private int calculateAmount(List<ReceiptProduct> orders) {
        return orders.stream().mapToInt(order -> order.getTotalAmount()).sum();
    }

}
