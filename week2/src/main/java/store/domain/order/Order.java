package store.domain.order;

import java.util.List;

public class Order {
    private List<ProductOrder> totalProductOrders;

    public Order(List<ProductOrder> totalProductOrders) {
        this.totalProductOrders = totalProductOrders;
    }

    public List<ProductOrder> getTotalProductOrders() {
        return totalProductOrders;
    }

}
