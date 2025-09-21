package store.repository;

import store.domain.order.Order;
import store.domain.order.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(String orderId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findAll();
    void deleteById(String orderId);
}