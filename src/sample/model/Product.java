package sample.model;

import java.text.DecimalFormat;
import java.util.Objects;

import static java.lang.Float.parseFloat;

public class Product {

    private int productID; //Primary KEY, AutoINC, NOT NULL
    private String barcode;
    private String label; // NOT NULL
    private String nameProduct; // NOT NULL, UNIQUE
    private int stock; // // NOT NULL
    private float sellingPrice; // NOT NULL
    private float buyingPrice; // NOT NULL
    private float vat; // can be null if its null or less than zero we assign 0(zero) when we create new Product or update.
    private int quantity; // NOT NULL
    private int mainProduct;  // we set it as 1 if the product has sub product if not we set as 0 with trigger
    private int mainProductID; //connectedProductID  comes from connected_product DB

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getMainProductID() {
        return mainProductID;
    }

    public void setMainProductID(int mainProductID) {
        this.mainProductID = mainProductID;
    }

    public int getMainProduct() {
        return mainProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getProductID() == product.getProductID();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getProductID());
    }

    public void setMainProduct(int subProduct) {
        this.mainProduct = subProduct;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
            this.sellingPrice = sellingPrice;
    }

    public float getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(float buyingPrice) {

        this.buyingPrice = buyingPrice;
    }

    public float getVat() {

        return vat;
    }

    public void setVat(float vat) {
        this.vat = vat;
    }

    public int getQuantity() {
        return quantity;

    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }




}
