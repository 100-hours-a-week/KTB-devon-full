package store.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventPublisher {
    private static EventPublisher instance;
    private final List<EventListener> listeners;
    private final ExecutorService eventExecutor;

    private EventPublisher() {
        this.listeners = new CopyOnWriteArrayList<>();
        this.eventExecutor = Executors.newFixedThreadPool(2); // 이벤트 처리 전용 스레드풀
    }

    public static synchronized EventPublisher getInstance() {
        if (instance == null) {
            instance = new EventPublisher();
        }
        return instance;
    }

    public void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(EventListener listener) {
        listeners.remove(listener);
    }

    public void publishStockEvent(StockEvent event) {
        // 비동기로 이벤트 처리
        CompletableFuture.runAsync(() -> {
            for (EventListener listener : listeners) {
                try {
                    listener.onStockEvent(event);
                } catch (Exception e) {
                    System.err.println("이벤트 처리 중 오류 발생: " + e.getMessage());
                }
            }
        }, eventExecutor);
    }

    public void shutdown() {
        eventExecutor.shutdown();
    }
}