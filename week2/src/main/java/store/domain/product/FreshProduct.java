package store.domain.product;

import java.time.LocalDate;

public class FreshProduct extends Food {
    private String freshGrade;

    public FreshProduct(String name, int price, LocalDate expiryDate, String freshGrade) {
        super(name, price, expiryDate, "냉장");
        this.freshGrade = freshGrade;
    }

    public String getFreshGrade() {
        return freshGrade;
    }

    public boolean isPremiumGrade() {
        return freshGrade.equals("A");
    }

    @Override
    public String toString() {
        String baseInfo = super.toString();
        return baseInfo + " (등급: " + freshGrade + ")";
    }
}