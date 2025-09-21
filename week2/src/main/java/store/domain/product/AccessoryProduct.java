package store.domain.product;

public class AccessoryProduct extends Clothing {
    private String category;

    public AccessoryProduct(String name, int price, String size, String material, String targetGender, String category) {
        super(name, price, size, material, targetGender);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public boolean isLuxuryItem() {
        return material.contains("금") || material.contains("은") || material.contains("다이아몬드");
    }

    @Override
    public String toString() {
        String baseInfo = super.toString();
        return baseInfo + " (" + category + ")";
    }
}