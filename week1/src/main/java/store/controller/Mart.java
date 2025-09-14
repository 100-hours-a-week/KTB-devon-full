package store.controller;

import store.Service.CheckoutService;
import store.Service.InventoryService;
import store.domain.order.Order;
import store.domain.order.ProductOrder;
import store.domain.order.Receipt;
import store.infra.ProductDataLoader;
import store.repository.InventoryManager;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;

public class Mart {

    final String productsDataPath = "products.md";

    InputView inputView;
    OutputView outputView;

    ProductDataLoader productDataLoader;

    InventoryManager inventoryManager;

    InventoryService inventoryService;
    CheckoutService checkoutService;

    public Mart(){
        initializeComponents();
    }

    private void initializeComponents() {
        this.inputView = new InputView();
        this.outputView = new OutputView();

        this.productDataLoader = new ProductDataLoader(productsDataPath);

        this.inventoryManager = new InventoryManager(productDataLoader);

        this.inventoryService = new InventoryService(inventoryManager);
        this.checkoutService = new CheckoutService(inventoryManager);
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
