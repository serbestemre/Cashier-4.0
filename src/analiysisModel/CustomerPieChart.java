package analiysisModel;

public class CustomerPieChart {
    private String barcode;
    private String label;
    private String nameProduct;
    private int productID;
    private int quantity;
    private double totalSubPrice;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return getBarcode()+" "+getLabel()+" "+ getNameProduct()+ " "+ getProductID()+ " "+ getQuantity()+ " " + getTotalSubPrice();
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

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalSubPrice() {
        return totalSubPrice;
    }

    public void setTotalSubPrice(double totalSubPrice) {
        this.totalSubPrice = totalSubPrice;
    }
}
