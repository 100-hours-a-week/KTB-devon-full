package store.view;

import store.domain.product.Product;
import store.domain.order.Receipt;
import store.domain.product.ReceiptProduct;

import java.text.DecimalFormat;
import java.util.List;

public class OutputView {

    public void displayWelcomeMessage() {
        System.out.println("안녕하세요. 마트입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
    }

    public void displayProductList(List<Product> products){
        for (Product product : products) {
            System.out.println(product.toString());
        }
    }


    public void displayReceipt(Receipt receipt) {
        DecimalFormat currencyFormat = new DecimalFormat("#,###");

        System.out.println("==============마트================");
        System.out.format("%-15s%-8s%-8s%n", "상품명", "수량", "금액");

        for (ReceiptProduct item : receipt.getPurchasedItems()) {
            int totalItemPrice = item.getQuantity() * item.getPrice();
            System.out.format("%-15s%-8d%-8s%n", item.getProductName(), item.getQuantity(), currencyFormat.format(totalItemPrice));
        }

        System.out.println("====================================");
        System.out.format("총구매액\t\t\t%s%n", currencyFormat.format(receipt.getTotalAmount()));
        System.out.format("내실돈\t\t\t%s%n", currencyFormat.format(receipt.getFinalAmount()));
    }

    public void displayError(String message) {
        System.out.println("[ERROR] " + message + " 다시 입력해 주세요.");
    }

    public void displayOrderCreated(String orderId) {
        System.out.println("\n✅ 주문이 생성되었습니다!");
        System.out.println("주문 ID: " + orderId);
        System.out.println("재고가 예약되었습니다.");
    }

    public void displayOrderCancelled(String orderId) {
        System.out.println("\n❌ 주문이 취소되었습니다.");
        System.out.println("주문 ID: " + orderId);
        System.out.println("예약된 재고가 복원되었습니다.");
    }

}
