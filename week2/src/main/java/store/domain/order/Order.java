package store.domain.order;

import java.util.List;
import java.util.UUID;

public class Order {
    private final String orderId;
    private final List<ProductOrder> totalProductOrders;
    private OrderStatus status;

    public Order(List<ProductOrder> totalProductOrders) {
        this.orderId = UUID.randomUUID().toString();
        this.totalProductOrders = totalProductOrders;
        this.status = OrderStatus.RESERVED;
    }

    public Order(String orderId, List<ProductOrder> totalProductOrders, OrderStatus status) {
        this.orderId = orderId;
        this.totalProductOrders = totalProductOrders;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<ProductOrder> getTotalProductOrders() {
        return totalProductOrders;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void confirmOrder() {
        this.status = OrderStatus.CONFIRMED;
    }

    public void cancelOrder() {
        this.status = OrderStatus.CANCELLED;
    }

    public boolean isReserved() {
        return status == OrderStatus.RESERVED;
    }

    public boolean isConfirmed() {
        return status == OrderStatus.CONFIRMED;
    }

    public boolean isCancelled() {
        return status == OrderStatus.CANCELLED;
    }
}
