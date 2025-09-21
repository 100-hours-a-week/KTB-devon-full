package store.domain.product;

public abstract class Electronics extends Product {
    protected int warrantyPeriod;
    protected String brand;

    public Electronics(String name, int price, int warrantyPeriod, String brand) {
        super(name, price);
        this.warrantyPeriod = warrantyPeriod;
        this.brand = brand;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public String getBrand() {
        return brand;
    }

    public boolean hasWarranty() {
        return warrantyPeriod > 0;
    }

    @Override
    public String toString() {
        String baseInfo = super.toString();
        String warrantyInfo = hasWarranty() ? " (보증기간: " + warrantyPeriod + "개월)" : " (보증기간 없음)";
        return baseInfo + " [" + brand + "]" + warrantyInfo;
    }
}