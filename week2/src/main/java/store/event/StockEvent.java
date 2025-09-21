package store.event;

public class StockEvent {
    private final String productName;
    private final int currentStock;
    private final StockEventType eventType;
    private final long timestamp;

    public StockEvent(String productName, int currentStock, StockEventType eventType) {
        this.productName = productName;
        this.currentStock = currentStock;
        this.eventType = eventType;
        this.timestamp = System.currentTimeMillis();
    }

    public String getProductName() {
        return productName;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public StockEventType getEventType() {
        return eventType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (현재 재고: %d)",
            new java.util.Date(timestamp),
            productName,
            eventType.getMessage(),
            currentStock);
    }
}