package store.domain.product;

import java.time.LocalDate;

public class ProcessedFoodProduct extends Food {
    private LocalDate manufacturingDate;
    private int shelfLife;

    public ProcessedFoodProduct(String name, int price, LocalDate manufacturingDate, int shelfLife, String storageMethod) {
        super(name, price, manufacturingDate.plusDays(shelfLife), storageMethod);
        this.manufacturingDate = manufacturingDate;
        this.shelfLife = shelfLife;
    }

    public LocalDate getManufacturingDate() {
        return manufacturingDate;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public boolean isLongShelfLife() {
        return shelfLife >= 365;
    }

    @Override
    public String toString() {
        String baseInfo = super.toString();
        return baseInfo + " (제조일: " + manufacturingDate + ")";
    }
}