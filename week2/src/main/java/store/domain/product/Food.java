package store.domain.product;

import java.time.LocalDate;

public abstract class Food extends Product {
    protected LocalDate expiryDate;
    protected String storageMethod;

    public Food(String name, int price, LocalDate expiryDate, String storageMethod) {
        super(name, price);
        this.expiryDate = expiryDate;
        this.storageMethod = storageMethod;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getStorageMethod() {
        return storageMethod;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public int getDaysUntilExpiry() {
        return (int) LocalDate.now().until(expiryDate).getDays();
    }

    @Override
    public String toString() {
        String baseInfo = super.toString();
        String expiryInfo = isExpired() ? " (유통기한 만료)" : " (유통기한: " + expiryDate + ")";
        return baseInfo + expiryInfo + " [" + storageMethod + "]";
    }
}