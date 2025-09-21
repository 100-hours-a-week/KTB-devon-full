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
import store.config.SimulationConfig;

import java.util.List;

public class Mart {

    final String productsDataPath = "products.md";

    private final InputView inputView;
    private final OutputView outputView;

    private final InventoryService inventoryService;
    private final CheckoutService checkoutService;
    private final ThreadPoolManager threadPoolManager;
    private StoreSimulation storeSimulation;

    public Mart(){
        InMemoryDatabase database = new InMemoryDatabase();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database, productsDataPath);
        ProductRepository productRepository = new InMemoryProductRepository(database);
        databaseInitializer.initializeData();

        this.inputView = new InputView();
        this.outputView = new OutputView();

        this.inventoryService = new InventoryService(productRepository);
        this.checkoutService = new CheckoutService(productRepository, inventoryService);
        this.threadPoolManager = new ThreadPoolManager(SimulationConfig.THREAD_POOL_SIZE);
        this.storeSimulation = new StoreSimulation(threadPoolManager, checkoutService, inventoryService);
    }

    public void start(){
        // 시뮬레이션 시작
        if (SimulationConfig.ENABLE_SIMULATION) {
            storeSimulation.startSimulation(SimulationConfig.VIRTUAL_CUSTOMER_COUNT);
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
        if (SimulationConfig.ENABLE_SIMULATION) {
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
