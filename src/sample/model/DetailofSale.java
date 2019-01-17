package sample.model;

public class DetailofSale {
    int detailID; // Primary Key, AutoINC, NOT NULL
    int saleID; //NOT NULL
    int productID; //NOT NULL
    float instantPrice; //NOT NULL
    float instantVat;
    int quantity; //NOT NULL
    float subTotal; //NOT NULL

    int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public float getInstantVat() {
        return instantVat;
    }

    public void setInstantVat(float instantVat) {
        this.instantVat = instantVat;
    }

    public int getDetailID() {
        return detailID;
    }

    public void setDetailID(int detailID) {
        this.detailID = detailID;
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public float getInstantPrice() {
        return instantPrice;
    }

    public void setInstantPrice(float instantPrice) {
        this.instantPrice = instantPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }
}
