package store.controller;

import store.service.CheckoutService;
import store.service.InventoryService;
import store.service.OrderService;
import store.domain.order.Order;
import store.domain.order.ProductOrder;
import store.domain.order.Receipt;
import store.infra.DatabaseInitializer;
import store.infra.InMemoryDatabase;
import store.repository.impl.InMemoryProductRepository;
import store.repository.impl.InMemoryOrderRepository;
import store.repository.ProductRepository;
import store.repository.OrderRepository;
import store.view.InputView;
import store.view.OutputView;
import store.concurrent.StoreSimulation;
import store.infra.ThreadPoolManager;
import store.config.AppConfig;
import store.event.EventPublisher;
import store.notification.ConsoleNotificationService;

import java.util.List;
import java.util.Optional;

public class Mart {



    private final ThreadPoolManager threadPoolManager;

    private final InputView inputView;
    private final OutputView outputView;

    private final InventoryService inventoryService;
    private final CheckoutService checkoutService;
    private final OrderService orderService;

    private final StoreSimulation storeSimulation;

    public Mart(){
        InMemoryDatabase database = new InMemoryDatabase();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database, AppConfig.PRODUCTS_DATA_PATH);
        ProductRepository productRepository = new InMemoryProductRepository(database);
        OrderRepository orderRepository = new InMemoryOrderRepository();
        databaseInitializer.initializeData();

        this.threadPoolManager = new ThreadPoolManager(AppConfig.THREAD_POOL_SIZE);

        this.inputView = new InputView();
        this.outputView = new OutputView();

        this.inventoryService = new InventoryService(productRepository);
        this.checkoutService = new CheckoutService(productRepository);
        this.orderService = new OrderService(orderRepository, inventoryService);
        this.storeSimulation = new StoreSimulation(threadPoolManager, inventoryService, orderService);

        EventPublisher eventPublisher = EventPublisher.getInstance();
        ConsoleNotificationService notificationService = new ConsoleNotificationService();
        eventPublisher.subscribe(notificationService);
    }

    public void start(){
        // 시뮬레이션 시작
        if (AppConfig.ENABLE_SIMULATION) {
            storeSimulation.startSimulation(AppConfig.VIRTUAL_CUSTOMER_COUNT);
        }

        while (true){

            outputView.displayWelcomeMessage();
            outputView.displayProductList(inventoryService.getProducts());

            // 1. 주문 생성 (재고 예약)
            String orderId = createOrderWithReservation();
            if (orderId == null) {
                continue;
            }

            // 2. 구매 확정 여부 묻기
            if (inputView.confirmPurchase()) {
                processCheckout(orderId);
            } else {
                cancelOrder(orderId);
            }

            if(!inputView.continueShopping()){
                break;
            }
        }
        if (AppConfig.ENABLE_SIMULATION) {
            storeSimulation.stopSimulation();
        }

        // 이벤트 퍼블리셔 종료
        EventPublisher.getInstance().shutdown();
        threadPoolManager.shutdown();
    }

    // 주문 생성 및 재고 예약
    private String createOrderWithReservation() {
        List<ProductOrder> orders = getProductOrders();
        if (orders == null) {
            return null;
        }

        try {
            String orderId = orderService.createOrder(orders);
            outputView.displayOrderCreated(orderId);
            return orderId;
        } catch (IllegalArgumentException e) {
            outputView.displayError(e.getMessage());
            return null;
        }
    }

    private List<ProductOrder> getProductOrders(){
        while (true){
            try{
                List<ProductOrder> orders = inputView.getProductOrders();
                // 재고 확인은 OrderService에서 처리하므로 여기서는 제거
                return orders;
            }catch (IllegalArgumentException e){
                outputView.displayError(e.getMessage());
                return null; // 입력 오류시 null 반환
            }
        }
    }

    // 주문 확정 및 결제 처리
    private void processCheckout(String orderId) {
        try {
            Optional<Order> orderOpt = orderService.getOrder(orderId);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                orderService.confirmOrder(orderId);
                Receipt receipt = checkoutService.generateReceipt(order);
                outputView.displayReceipt(receipt);
            }
        } catch (Exception e) {
            outputView.displayError("결제 처리 실패: " + e.getMessage());
        }
    }

    // 주문 취소
    private void cancelOrder(String orderId) {
        try {
            orderService.cancelOrder(orderId);
            outputView.displayOrderCancelled(orderId);
        } catch (Exception e) {
            outputView.displayError("주문 취소 실패: " + e.getMessage());
        }
    }
}
