package store.repository.impl;

import store.domain.order.Order;
import store.domain.order.OrderStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import store.repository.OrderRepository;

public class InMemoryOrderRepository implements OrderRepository {
    private final Map<String, Order> orders;

    public InMemoryOrderRepository() {
        this.orders = new ConcurrentHashMap<>();
    }

    @Override
    public void save(Order order) {
        orders.put(order.getOrderId(), order);
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return orders.values().stream()
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findAll() {
        return orders.values().stream().collect(Collectors.toList());
    }

    @Override
    public void deleteById(String orderId) {
        orders.remove(orderId);
    }
}