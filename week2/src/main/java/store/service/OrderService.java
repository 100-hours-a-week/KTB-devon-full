package store.service;

import store.domain.order.Order;
import store.domain.order.ProductOrder;
import store.repository.OrderRepository;

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
            // 1. 재고 예약
            inventoryService.reserveStock(productOrders);

            // 2. 주문 생성 및 저장
            Order order = new Order(productOrders);
            orderRepository.save(order);

            return order.getOrderId();

        } catch (Exception e) {
            throw new IllegalArgumentException("주문 생성 실패: " + e.getMessage());
        }
    }

    // 주문 확정 (구매 확정)
    public synchronized void confirmOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (!order.isReserved()) {
            throw new IllegalArgumentException("예약된 주문이 아닙니다.");
        }

        order.confirmOrder();
        orderRepository.save(order);
    }

    // 주문 취소
    public synchronized void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (order.isConfirmed()) {
            throw new IllegalArgumentException("이미 확정된 주문은 취소할 수 없습니다.");
        }

        if (order.isReserved()) {
            // 재고 예약 취소
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