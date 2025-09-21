package store.domain.product;

public abstract class Clothing extends Product {
    protected String size;
    protected String material;
    protected String targetGender;

    public Clothing(String name, int price, String size, String material, String targetGender) {
        super(name, price);
        this.size = size;
        this.material = material;
        this.targetGender = targetGender;
    }

    public String getSize() {
        return size;
    }

    public String getMaterial() {
        return material;
    }

    public String getTargetGender() {
        return targetGender;
    }

    public boolean isPremiumMaterial() {
        return material.contains("실크") || material.contains("캐시미어") || material.contains("울");
    }

    @Override
    public String toString() {
        String baseInfo = super.toString();
        return baseInfo + " [" + size + ", " + material + ", " + targetGender + "]";
    }
}