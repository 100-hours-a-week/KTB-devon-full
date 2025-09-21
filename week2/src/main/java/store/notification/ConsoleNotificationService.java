package store.notification;

import store.event.EventListener;
import store.event.StockEvent;
import store.event.StockEventType;

public class ConsoleNotificationService implements EventListener {

    @Override
    public void onStockEvent(StockEvent event) {
        String notification = formatNotification(event);

        synchronized (System.out) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("📢 재고 알림");
            System.out.println("=".repeat(50));
            System.out.println(notification);
            System.out.println("=".repeat(50) + "\n");
        }
    }

    private String formatNotification(StockEvent event) {
        StringBuilder sb = new StringBuilder();

        if (event.getEventType() == StockEventType.OUT_OF_STOCK) {
            sb.append("🚨 [품절 알림] ");
        } else if (event.getEventType() == StockEventType.RESTOCKED) {
            sb.append("✅ [재입고 알림] ");
        }

        sb.append(String.format("상품명: %s", event.getProductName()));
        sb.append(String.format("\n상태: %s", event.getEventType().getMessage()));
        sb.append(String.format("\n현재 재고: %d개", event.getCurrentStock()));
        sb.append(String.format("\n시간: %s", new java.util.Date(event.getTimestamp())));

        return sb.toString();
    }
}