package store.Service;

import store.domain.order.Order;
import store.domain.product.Product;
import store.domain.order.ProductOrder;
import store.domain.order.Receipt;
import store.domain.product.ReceiptProduct;
import store.repository.InventoryManager;

import java.util.ArrayList;
import java.util.List;

public class CheckoutService {

    private final InventoryManager inventoryManager;

    public CheckoutService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public Receipt processOrder(Order order) {
        updateStock(order);
        return generateReceipt(order);
    }

    private void updateStock(Order order) {
        for (ProductOrder totalOrder : order.getTotalProductOrders()) {
            processStockForOrder(totalOrder);
        }
    }

    private void processStockForOrder(ProductOrder totalOrder) {
        String productName = totalOrder.getProductName();
        int totalQuantity = totalOrder.getQuantity();

        inventoryManager.reduceProductStock(productName, totalQuantity, false);
    }

    private Receipt generateReceipt(Order order) {
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
            Product product = inventoryManager.getProduct(order.getProductName());

            products.add(new ReceiptProduct(product.getName(), order.getQuantity(), product.getPrice()));
        }
        return products;
    }

    private int calculateAmount(List<ReceiptProduct> orders) {
        return orders.stream().mapToInt(order -> order.getTotalAmount()).sum();
    }


}
