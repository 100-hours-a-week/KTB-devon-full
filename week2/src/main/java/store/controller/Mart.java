package store.controller;

import store.service.CheckoutService;
import store.service.InventoryService;
import store.domain.order.Order;
import store.domain.order.ProductOrder;
import store.domain.order.Receipt;
import store.infra.DatabaseInitializer;
import store.infra.InMemoryDatabase;
import store.repository.InMemoryProductRepository;
import store.repository.ProductRepository;
import store.view.InputView;
import store.view.OutputView;
import store.concurrent.StoreSimulation;
import store.infra.ThreadPoolManager;
import store.config.AppConfig;

import java.util.List;

public class Mart {



    private final ThreadPoolManager threadPoolManager;

    private final InputView inputView;
    private final OutputView outputView;

    private final InventoryService inventoryService;
    private final CheckoutService checkoutService;

    private final StoreSimulation storeSimulation;

    public Mart(){
        InMemoryDatabase database = new InMemoryDatabase();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database, AppConfig.PRODUCTS_DATA_PATH);
        ProductRepository productRepository = new InMemoryProductRepository(database);
        databaseInitializer.initializeData();

        this.threadPoolManager = new ThreadPoolManager(AppConfig.THREAD_POOL_SIZE);

        this.inputView = new InputView();
        this.outputView = new OutputView();

        this.inventoryService = new InventoryService(productRepository);
        this.checkoutService = new CheckoutService(productRepository, inventoryService);
        this.storeSimulation = new StoreSimulation(threadPoolManager, checkoutService, inventoryService);
    }

    public void start(){
        // 시뮬레이션 시작
        if (AppConfig.ENABLE_SIMULATION) {
            storeSimulation.startSimulation(AppConfig.VIRTUAL_CUSTOMER_COUNT);
        }

        while (true){

            outputView.displayWelcomeMessage();
            outputView.displayProductList(inventoryService.getProducts());

            Order order = createOrder();
            processCheckout(order);

            if(!inputView.continueShopping()){
                break;
            }
        }
        if (AppConfig.ENABLE_SIMULATION) {
            storeSimulation.stopSimulation();
        }
        threadPoolManager.shutdown();
    }

    private Order createOrder() {
        List<ProductOrder> orders = getProductOrders();
        return new Order(orders);
    }

    private List<ProductOrder> getProductOrders(){
        while (true){
            try{
                List<ProductOrder> orders = inputView.getProductOrders();
                inventoryService.checkProductStock(orders);
                return orders;
            }catch (IllegalArgumentException e){
                outputView.displayError(e.getMessage());
            }
        }
    }


    private void processCheckout(Order order) {
        Receipt receipt = checkoutService.processOrder(order);
        outputView.displayReceipt(receipt);
    }
}
