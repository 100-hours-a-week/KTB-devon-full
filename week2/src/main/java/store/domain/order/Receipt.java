package store.domain.order;

import java.util.List;
import store.domain.product.ReceiptProduct;

public class Receipt {
    private List<ReceiptProduct> purchasedItems;
    private int totalAmount;
    private int finalAmount;

    public Receipt(List<ReceiptProduct> purchasedItems, int totalAmount, int finalAmount) {
        this.purchasedItems = purchasedItems;
        this.totalAmount = totalAmount;
        this.finalAmount = finalAmount;
    }

    public List<ReceiptProduct> getPurchasedItems() { return purchasedItems; }
    public int getTotalAmount() { return totalAmount; }
    public int getFinalAmount() { return finalAmount; }
}
