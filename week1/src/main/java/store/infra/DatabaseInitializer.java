package store.infra;

import store.domain.product.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import store.utils.ErrorMessages;

public class DatabaseInitializer {
    private final InMemoryDatabase database;
    private final String dataFilePath;

    public DatabaseInitializer(InMemoryDatabase database, String dataFilePath) {
        this.database = database;
        this.dataFilePath = dataFilePath;
    }

    public void initializeData() {
        try {
            loadProductsFromFile();
        } catch (IOException e) {
            throw new IllegalStateException(ErrorMessages.FAILED_INITIALIZE_INVENTORY);
        }
    }

    private void loadProductsFromFile() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(dataFilePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + dataFilePath);
        }

        String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        String[] lines = content.split("\n");

        for (String line : lines) {
            processLine(line);
        }
    }

    private void processLine(String line) {
        line = line.trim();
        if (line.isBlank() || line.startsWith("name")) {
            return;
        }

        String[] fields = line.split(",");
        if (fields.length < 4) {
            return;
        }

        String name = fields[0].trim();
        int price = Integer.parseInt(fields[1].trim());
        int quantity = Integer.parseInt(fields[2].trim());
        String type = fields[3].trim();

        Product product = createProductByType(name, price, quantity, type, fields);
        if (product != null) {
            database.insertProduct(product);
        }
    }

    private Product createProductByType(String name, int price, int quantity, String type, String[] fields) {
        Product product = null;

        switch (type) {
            case "SmartphoneProduct":
                int warrantyPeriod = Integer.parseInt(fields[4].trim());
                String brand = fields[5].trim();
                String os = fields[6].trim();
                int storage = Integer.parseInt(fields[7].trim());
                product = new SmartphoneProduct(name, price, warrantyPeriod, brand, os, storage);
                break;
            case "LaptopProduct":
                int laptopWarranty = Integer.parseInt(fields[4].trim());
                String laptopBrand = fields[5].trim();
                String processor = fields[6].trim();
                int ramSize = Integer.parseInt(fields[7].trim());
                product = new LaptopProduct(name, price, laptopWarranty, laptopBrand, processor, ramSize);
                break;
            case "FreshProduct":
                LocalDate expiryDate = LocalDate.parse(fields[4].trim());
                String freshGrade = fields[5].trim();
                product = new FreshProduct(name, price, expiryDate, freshGrade);
                break;
            case "ProcessedFoodProduct":
                LocalDate procMfgDate = LocalDate.parse(fields[4].trim());
                int procShelfLife = Integer.parseInt(fields[5].trim());
                String preservation = fields[6].trim();
                product = new ProcessedFoodProduct(name, price, procMfgDate, procShelfLife, preservation);
                break;
            case "AccessoryProduct":
                String size = fields[4].trim();
                String material = fields[5].trim();
                String targetGender = fields[6].trim();
                String category = fields[7].trim();
                product = new AccessoryProduct(name, price, size, material, targetGender, category);
                break;
            case "GeneralClothingProduct":
                String clothingSize = fields[4].trim();
                String clothingMaterial = fields[5].trim();
                String clothingGender = fields[6].trim();
                String season = fields[7].trim();
                String style = fields[8].trim();
                product = new GeneralClothingProduct(name, price, clothingSize, clothingMaterial, clothingGender, season, style);
                break;
        }

        if (product != null) {
            product.setStock(quantity);
        }

        return product;
    }
}