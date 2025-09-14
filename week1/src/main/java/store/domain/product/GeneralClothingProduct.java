package store.domain.product;

public class GeneralClothingProduct extends Clothing {
    private String season;
    private String style;

    public GeneralClothingProduct(String name, int price, String size, String material, String targetGender, String season, String style) {
        super(name, price, size, material, targetGender);
        this.season = season;
        this.style = style;
    }

    public String getSeason() {
        return season;
    }

    public String getStyle() {
        return style;
    }

    public boolean isSeasonalItem() {
        return !season.equals("사계절");
    }

    @Override
    public String toString() {
        String baseInfo = super.toString();
        return baseInfo + " (" + season + ", " + style + ")";
    }
}