package store.service;

import store.domain.order.Order;
import store.domain.order.ProductOrder;
import store.repository.OrderRepository;
import store.utils.ErrorMessages;

import java.util.List;
import java.util.Optional;

public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    public OrderService(OrderRepository orderRepository, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
    }

    // 주문 생성 및 재고 예약
    public synchronized String createOrder(List<ProductOrder> productOrders) {
        try {
            inventoryService.reserveStock(productOrders);

            Order order = new Order(productOrders);
            orderRepository.save(order);

            return order.getOrderId();

        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorMessages.ORDER_CREATION_FAILED + ": " + e.getMessage());
        }
    }

    // 주문 확정 (구매 확정)
    public synchronized void confirmOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.ORDER_NOT_FOUND));

        if (!order.isReserved()) {
            throw new IllegalArgumentException(ErrorMessages.ORDER_NOT_RESERVED);
        }

        order.confirmOrder();
        orderRepository.save(order);
    }

    // 주문 취소
    public synchronized void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.ORDER_NOT_FOUND));

        if (order.isConfirmed()) {
            throw new IllegalArgumentException(ErrorMessages.ORDER_ALREADY_CONFIRMED);
        }

        if (order.isReserved()) {
            inventoryService.cancelReservation(order.getTotalProductOrders());
        }

        order.cancelOrder();
        orderRepository.save(order);
    }

    // 주문 조회
    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    // 모든 주문 조회
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}