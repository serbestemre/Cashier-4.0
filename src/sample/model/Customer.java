package sample.model;

public class Customer {

    private int customerID; // Primary Key, Auto INC, Not NULL
    private String name; // // NOT NULL
    private String surname; // NOT NULL
    private String email;
    private String phone;
    private String  taxID;// NOT NULL
    private String address;

    public Customer() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getTaxID()+" " +getName()+" " +getSurname();
    }

    public void setName(String name) {

            this.name = name;


    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {

            this.surname = surname;

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {

            this.phone = phone;

    }

    public String  getTaxID() {
        return taxID;
    }

    public void setTaxID(String  taxID) {

            this.taxID = taxID;

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {

            this.address = address;

    }

}
