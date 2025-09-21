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

import java.util.List;

public class Mart {

    final String productsDataPath = "products.md";

    InputView inputView;
    OutputView outputView;

    InMemoryDatabase database;
    DatabaseInitializer databaseInitializer;
    ProductRepository productRepository;

    InventoryService inventoryService;
    CheckoutService checkoutService;

    public Mart(){
        initializeComponents();
    }

    private void initializeComponents() {
        this.inputView = new InputView();
        this.outputView = new OutputView();

        this.database = new InMemoryDatabase();
        this.databaseInitializer = new DatabaseInitializer(database, productsDataPath);
        this.productRepository = new InMemoryProductRepository(database);

        databaseInitializer.initializeData();

        this.inventoryService = new InventoryService(productRepository);
        this.checkoutService = new CheckoutService(productRepository);
    }

    public void start(){
        while (true){

            outputView.displayWelcomeMessage();
            outputView.displayProductList(inventoryService.getProducts());

            Order order = createOrder();
            processCheckout(order);

            if(!inputView.continueShopping()){
                break;
            }
        }
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
