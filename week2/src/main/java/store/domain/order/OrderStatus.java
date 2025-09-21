package store.domain.order;

public enum OrderStatus {
    RESERVED,    // 주문 생성, 재고 예약됨
    CONFIRMED,   // 구매 확정
    CANCELLED    // 주문 취소
}