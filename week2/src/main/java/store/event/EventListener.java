package store.event;

public interface EventListener {
    void onStockEvent(StockEvent event);
}