package store.domain.product;

public class LaptopProduct extends Electronics {
    private String processor;
    private int ramSize;

    public LaptopProduct(String name, int price, int warrantyPeriod, String brand, String processor, int ramSize) {
        super(name, price, warrantyPeriod, brand);
        this.processor = processor;
        this.ramSize = ramSize;
    }

    public String getProcessor() {
        return processor;
    }

    public int getRamSize() {
        return ramSize;
    }

    public boolean isGamingLaptop() {
        return ramSize >= 16;
    }

    @Override
    public String toString() {
        String baseInfo = super.toString();
        return baseInfo + " (" + processor + ", " + ramSize + "GB RAM)";
    }
}