package store.infra;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import store.domain.product.AccessoryProduct;
import store.domain.product.FreshProduct;
import store.domain.product.GeneralClothingProduct;
import store.domain.product.LaptopProduct;
import store.domain.product.ProcessedFoodProduct;
import store.domain.product.Product;
import store.domain.product.SmartphoneProduct;

public class ProductDataLoader {

    private String productDataPath;

    public ProductDataLoader(String productDataPath){
        this.productDataPath = productDataPath;
    }

    public Map<String, Product> loadProducts() throws IOException {
        Map<String, Product> productMap = new HashMap<>();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(productDataPath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + productDataPath);
        }

        String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        String[] lines = content.split("\n");

        for (String line : lines) {
            processLine(productMap, line);
        }
        return productMap;
    }

    private void processLine(Map<String, Product> productMap, String line) {
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

        createProductByType(productMap, name, price, quantity, type, fields);
    }


    private void createProductByType(Map<String, Product> productMap, String name, int price, int quantity, String type, String[] fields) {
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
            productMap.put(name, product);
        }
    }

}
