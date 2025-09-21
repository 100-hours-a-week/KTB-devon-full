package store.event;

public enum StockEventType {
    OUT_OF_STOCK("재고가 모두 소진되었습니다"),
    RESTOCKED("재고가 새로 납입되었습니다");

    private final String message;

    StockEventType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}