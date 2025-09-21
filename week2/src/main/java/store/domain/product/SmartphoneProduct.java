package store.domain.product;

public class SmartphoneProduct extends Electronics {
    private String operatingSystem;
    private int storageCapacity;

    public SmartphoneProduct(String name, int price, int warrantyPeriod, String brand, String operatingSystem, int storageCapacity) {
        super(name, price, warrantyPeriod, brand);
        this.operatingSystem = operatingSystem;
        this.storageCapacity = storageCapacity;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public boolean isHighEndPhone() {
        return storageCapacity >= 256;
    }

    @Override
    public String toString() {
        String baseInfo = super.toString();
        return baseInfo + " (" + operatingSystem + ", " + storageCapacity + "GB)";
    }
}