package store.view;

import store.domain.order.ProductOrder;
import store.utils.ErrorMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class InputView {
    private Scanner scanner = new Scanner(System.in);

    public List<ProductOrder> getProductOrders() {
        System.out.println("\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");

        while (true) {
            String input = scanner.nextLine();
            try {
                return parseItems(input);
            } catch (IllegalArgumentException e) {
                System.out.println("[ERROR] " + e.getMessage() + " 다시 입력해 주세요.");
            }
        }
    }

    private List<ProductOrder> parseItems(String input) {
        List<ProductOrder> items = new ArrayList<>();
        String[] itemInputs = input.split(",");

        for (String itemInput : itemInputs) {
            Matcher matcher = Pattern.compile("\\[(.+)-(\\d+)]").matcher(itemInput.trim());
            if (!matcher.matches()) {
                throw new IllegalArgumentException(ErrorMessages.INVALID_FORMAT);
            }
            String itemName = matcher.group(1);
            int quantity = Integer.parseInt(matcher.group(2));
            items.add(new ProductOrder(itemName, quantity));
        }
        return items;
    }


    public boolean confirmAction() {
        while (true) {
            String input = scanner.nextLine();
            try {
                return checkInput(input);
            } catch (IllegalArgumentException e) {
                System.out.println("[ERROR] " + e.getMessage() +" 다시 입력해 주세요.");
            }
        }
    }


    public boolean confirmPurchase() {
        System.out.println("구매를 확정하시겠습니까? (Y/N)");
        return confirmAction();
    }

    public boolean continueShopping() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return confirmAction();
    }

    private boolean checkInput(String input){
        if (!input.equals("Y") && !input.equals("N")) {
                throw new IllegalArgumentException(ErrorMessages.INVALID_INPUT);
            }
        return input.equals("Y");
    }
}
