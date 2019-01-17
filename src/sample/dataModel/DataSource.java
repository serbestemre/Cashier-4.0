package sample.dataModel;


import analiysisModel.CustomerPieChart;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.sqlite.SQLiteException;
import sample.model.*;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.Float.parseFloat;


public class DataSource {

    private DateTimeFormatter formatter;

    public static final String DB_NAME = "src/marketimv2.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;

    ///TABLE CUSTOMER
    public static final String TABLE_CUSTOMER = "customer";
    public static final String COLUMN_CUSTOMERID = "customerID";
    public static final String COLUMN_NAME = "name";  //value1
    public static final String COLUMN_SURNAME = "surname"; //value2
    public static final String COLUMN_EMAIL= "email";
    public static final String COLUMN_PHONE = "phone"; //value3
    public static final String COLUMN_ADDRESS = "address"; //value4
    public static final String COLUMN_TAXID = "taxID"; //value5

    //TABLE PRODUCT
    public static final String TABLE_PRODUCT = "product";
    public static final String COLUMN_PRODUCTID = "productID";
    public static final String COLUMN_BARCODE="barcode";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_NAME_PRODUCT = "nameProduct";
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_SELLING_PRICE = "sellingPrice";
    public static final String COLUMN_BUYING_PRICE = "buyingPrice";
    public static final String COLUMN_VAT = "vat";
    public static final String COLUMN_MAIN_PRODUCT ="mainProduct";
    public static final String COLUMN_MAINPRODUCTID="mainProductID";

    ///TABLE SALE
    public static final String TABLE_SALE="sale";
    public static final String COLUMN_SALEID="saleID";
    public static final String COLUMN_CUSTOMERID_SALE="customerID"; // foreignKey
    public static final String COLUMN_TOTAL_PRICE="totalPrice";
    public static final String COLUMN_TOTAL_VAT="totalVat";
    public static final String COLUMN_DATE="date";
    public static final String COLUMN_TIME="time";

    ///TABLE DETAIL_OF_SALE
    public static final String TABLE_DETAIL_OF_SALE="detail_of_sale";
    public static final String COLUMN_SALEID_DETAILOFSALE="saleID";
    public static final String COLUMN_PRODUCTID_DETAILOFSALE="productID"; // foreign key
    public static final String COLUMN_INSTANT_PRICE="instantPrice"; // price of product when it sold
    public static final String COLUMN_INSTANT_VAT="instantVat";
    public static final String COLUMN_QUANTITY_DETAILOFSALE="quantity";
    public static final String COLUMN_SUBTOTAL="subTotal";

    public static final String TABLE_CONNECTED_PRODUCT="connected_product";
    public static final String COLUMN_CONNECTEDID="connectedID";
    public static final String COLUMN_CONNECTED_PRODUCTID="connectedProductID";
    public static final String COLUMN_SUB_PRODUCTID="subProductID";
    public static final String COLUMN_SUB_QUANTITY="subProductQuantity";

    public static final String TABLE_OPTION="option";
    public static final String COLUMN_OPTIONID="optionID";
    public static final String COLUMN_HOME_PATH="homePath";
    public static final String COLUMN_SELECTEDPATH="selectedPath";

    private DataSource() {
        formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }


    private static DataSource instance = new DataSource();

     public static DataSource getInstance() {
        return instance;

    }

    private Connection connection;

    public boolean openDatabase() {

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);

            return true;

        } catch (SQLException e) {
            System.out.println("Could not connected to the database " + e.toString());
            return false;
        }
    }

    public void closeDatabase() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("The database could not closed");

        }
    }

    public boolean addNewCustomer(String name, String surname, String email, String phone, String address, String taxID) throws SQLException {

        try {

            Statement statement = connection.createStatement();

            statement.execute("insert into " + TABLE_CUSTOMER + "("+COLUMN_NAME+","+COLUMN_SURNAME+","+COLUMN_EMAIL+","+COLUMN_PHONE+","+COLUMN_ADDRESS+","+COLUMN_TAXID+") values(" + "'" + name + "'," + "'" + surname + "'," + "'" + email + "'," + "'" + phone + "'," + "'" + address + "' , " + "'" + taxID + "')");
/*          PreparedStatement statement=connection.prepareStatement(sqlStatement);
            statement.setString(1,name);
            statement.setString(2,surname);
            statement.setString(3,phone);
            statement.setString(4,address);
            statement.setInt(5,taxID);
            statement.execute(sqlStatement);*/
            System.out.println("datasource kayıt işlemi tamamlandı");
            return true;


        } catch (Exception e) {/*
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("This Customer might be created before \n Phone & TaxID has to be unique ");
            alert.showAndWait();*/
            return false;
        }


        //connection.commit();

    }

    public boolean addNewProduct(String barcode,String label, String nameProduct, int stock, float sellingPrice, float buyingPrice, float vat) throws SQLException {
//


        //String sqlStatemet="INSERT INTO ? (?,?,?,?,?,?) values(?,?,?,?,?,?)";


        //


        //try {


        //PreparedStatement statement = connection.prepareStatement(sqlStatemet);


        //


        //statement.setString(1,TABLE_PRODUCT);


        //


        //statement.setString(2,COLUMN_LABEL);


        //statement.setString(3,COLUMN_NAME_PRODUCT);


        //statement.setString(4,COLUMN_STOCK);


        //statement.setString(5,COLUMN_SELLING_PRICE);


        //statement.setString(6,COLUMN_BUYING_PRICE);


        //statement.setString(7,COLUMN_VAT);


        //


        //statement.setString(8,label);


        //statement.setString(9,nameProduct);


        //statement.setInt(10,stock);


        //statement.setFloat(11,sellingPrice);


        //statement.setFloat(12,buyingPrice);


        //statement.setFloat(13,vat);


        //statement.execute(sqlStatemet);


        //return true;


        //


        //


        //}catch (SQLException e){


        //


        //System.out.println(e.toString() + " ürün eklenemedi!!!");


        //return false;


        //


        //}
        try {
Statement statement=connection.createStatement();
        statement.execute("insert into " + TABLE_PRODUCT + "("+COLUMN_BARCODE+","+COLUMN_LABEL+","+COLUMN_NAME_PRODUCT+","+COLUMN_STOCK+","+COLUMN_SELLING_PRICE+","+COLUMN_BUYING_PRICE+","+COLUMN_VAT+") values(" + "'" + barcode + "'," + "'" + label + "'," + "'" + nameProduct + "'," + "'" + stock + "'," + "'" + sellingPrice + "'," + "'" + buyingPrice + "',"+ "'"+vat+"')");
        System.out.println("product eklendi");

        return true;


    } catch (SQLException sqlException) {
            System.out.println(nameProduct+" zaten mevcut   " +sqlException);

        return false;
    }
    }

    public boolean updateCustomer(int customerID, String name, String surname, String email, String phone, String address, String taxid) {


        try {

            Statement statement = connection.createStatement();

            int updatedRows=statement.executeUpdate("UPDATE " + TABLE_CUSTOMER + " SET "+COLUMN_NAME+"='"+name+"', "+COLUMN_SURNAME+"='"+surname+"',"+COLUMN_EMAIL+"='"+email+"',"+COLUMN_PHONE+"='"+phone+"',"+COLUMN_ADDRESS+"='"+address+"',"+COLUMN_TAXID+"='"+taxid+"' WHERE "+ COLUMN_CUSTOMERID+"="+customerID);
                return true;

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("customer couldnt updated ");
            return false;
        }
    }

    public boolean updateProduct(int productID, String barcode, String label, String nameProduct, int stock, float sellingPrice, float buyingPrice, float vat,int subProduct){

        try {

            Statement statement = connection.createStatement();

            int updatedRows=statement.executeUpdate("UPDATE " + TABLE_PRODUCT + " SET "+  COLUMN_BARCODE+"='"+barcode+"', "+  COLUMN_LABEL+"='"+label+"', "+COLUMN_NAME_PRODUCT+"='"+nameProduct+"',"+COLUMN_STOCK+"='"+stock+"',"+COLUMN_SELLING_PRICE+"='"+sellingPrice+"',"+COLUMN_BUYING_PRICE+"='"+buyingPrice+"',"+ COLUMN_VAT+"='"+vat+"',"+ COLUMN_MAIN_PRODUCT +"='"+subProduct+"' WHERE "+ COLUMN_PRODUCTID+"="+productID);
            return true;

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("product couldnt updated ");
            return false;
        }
    }

    public boolean deleteCustomer(int CustomerID) {

        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM " + TABLE_CUSTOMER + " WHERE " + COLUMN_CUSTOMERID + "=" + CustomerID + ";");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean deleteProduct(int productID){
        System.out.println("gelen productID=>>>"+productID);
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM " + TABLE_PRODUCT + " WHERE " + COLUMN_PRODUCTID + "=" + productID + ";");
            System.out.println("gelen productID=>>>"+productID);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }

    public ArrayList<Customer> getAllCustomers() {

        StringBuilder sb = new StringBuilder("Select * FROM ");
        sb.append(TABLE_CUSTOMER);

        ArrayList<Customer> allCustomers = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());


                while (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerID(rs.getInt(COLUMN_CUSTOMERID));
                    customer.setName(rs.getString(COLUMN_NAME));
                    customer.setSurname(rs.getString(COLUMN_SURNAME));
                    customer.setPhone(rs.getString(COLUMN_PHONE));
                    customer.setAddress(rs.getString(COLUMN_ADDRESS));
                    customer.setTaxID(rs.getString(COLUMN_TAXID));
                    customer.setEmail(rs.getString(COLUMN_EMAIL));
                    allCustomers.add(customer);
                }
                allCustomers.remove(0);
                return allCustomers;

        } catch (SQLException e) {

            System.out.println("sorgu başarısız " + e.getMessage());
            return null;
        }

    }

    public ArrayList<Product> getAllProducts() {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_PRODUCT);


            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sb.toString());

                    ArrayList<Product> allProducts = new ArrayList<>();

                    while (rs.next()) {

                        Product product = new Product();
                        product.setBarcode(rs.getString(COLUMN_BARCODE));
                        product.setProductID(rs.getInt(COLUMN_PRODUCTID));
                        product.setLabel(rs.getString(COLUMN_LABEL));
                        product.setNameProduct(rs.getString(COLUMN_NAME_PRODUCT));
                        product.setStock(rs.getInt(COLUMN_STOCK));
                        product.setSellingPrice(rs.getFloat(COLUMN_SELLING_PRICE));
                        product.setBuyingPrice(rs.getFloat(COLUMN_BUYING_PRICE));
                        product.setVat(rs.getFloat(COLUMN_VAT));
                        product.setMainProduct(rs.getInt(COLUMN_MAIN_PRODUCT));
                        product.setMainProductID(rs.getInt(COLUMN_MAINPRODUCTID));
                        allProducts.add(product);

                    }

                    return allProducts;

            } catch (SQLException e) {

                System.out.println("PRODUCT SORGU BAŞARISIZ " + e.getMessage());

                return null;
            }

        }

    public Product getSearchedProduct(int searchedProductID){

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_PRODUCT);
        sb.append(" WHERE ");
        sb.append(COLUMN_PRODUCTID);
        sb.append("= ");
        sb.append(searchedProductID);


        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());
            Product product = new Product();
            while (rs.next()) {
                product.setProductID(rs.getInt(COLUMN_PRODUCTID));
                product.setLabel(rs.getString(COLUMN_LABEL));
                product.setNameProduct(rs.getString(COLUMN_NAME_PRODUCT));
                product.setStock(rs.getInt(COLUMN_STOCK));
                product.setSellingPrice(rs.getFloat(COLUMN_SELLING_PRICE));
                product.setBuyingPrice(rs.getFloat(COLUMN_BUYING_PRICE));
                product.setVat(rs.getFloat(COLUMN_VAT));
                product.setMainProduct(rs.getInt(COLUMN_MAIN_PRODUCT));
                product.setMainProductID(rs.getInt(COLUMN_MAINPRODUCTID));
                product.setBarcode(rs.getString(COLUMN_BARCODE));
            }
            return product;
        } catch (SQLException e) {
            System.out.println("SEARCHED PRODUCT SORGUSU BAŞARISIZ ÇÜNKÜ >>> " + e.getMessage());
            return null;
        }
    }

    public ArrayList<ConnectedProduct> getAllSubProducts(int editingProductID){

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_CONNECTED_PRODUCT);
        sb.append(" WHERE ");
        sb.append(COLUMN_CONNECTED_PRODUCTID);
        sb.append("= ");
        sb.append(editingProductID);


        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());

            ArrayList<ConnectedProduct> allSubProducts = new ArrayList<>();

            while (rs.next()) {


                ConnectedProduct connectedProduct = new ConnectedProduct();

                connectedProduct.setConnectedID(rs.getInt(COLUMN_CONNECTEDID));
                connectedProduct.setConnectedProductID(rs.getInt(COLUMN_CONNECTED_PRODUCTID));
                connectedProduct.setSubProductID(rs.getInt(COLUMN_SUB_PRODUCTID));
                connectedProduct.setSubQuantity(rs.getInt(COLUMN_SUB_QUANTITY));


                allSubProducts.add(connectedProduct);

            }

            return allSubProducts;

        } catch (SQLException e) {


            System.out.println("SUB PRODUCTS SORGUSU BAŞARISIZ ÇÜNKÜ >>> " + e.getMessage());

            return null;
        }

    }

    public boolean createNewSale(int customerID, float totalPrice,float totalVat, String currentDate, String currentTime) {

        try {
            Statement statement=connection.createStatement();
            System.out.println("gelen currentdate=" + currentDate);
            statement.execute("insert into " + TABLE_SALE + "("+COLUMN_CUSTOMERID_SALE+","+COLUMN_TOTAL_PRICE+","+COLUMN_TOTAL_VAT+","+COLUMN_DATE+","+ COLUMN_TIME +" ) values(" + "'" + customerID + "'," + "'" + totalPrice+"',"+"'" + totalVat+"',"+"'"+currentDate+"',"+"'"+currentTime+"')");
            System.out.println("new sale created");
return true;

        } catch (SQLException sqlException) {
            System.out.println(sqlException+" sale couldnt created");

return false;
        }
    }


    //we need to getSale ID from database when its created first time to save its details into detail_of_shopping db table
    public int returnLastAddedSaleID(String shoppingDate, String shoppingTime, int customerID){

        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(COLUMN_SALEID);
        sb.append( " FROM " );
        sb.append(TABLE_SALE);
        sb.append( " WHERE ");
        sb.append(COLUMN_CUSTOMERID+"= "+ customerID);
        sb.append(" and ");
        sb.append(COLUMN_DATE+ "='"+ shoppingDate+"'");
        sb.append(" and ");
        sb.append(COLUMN_TIME+ "='"+ shoppingTime+"'");
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());

            return rs.getInt(COLUMN_SALEID);
        }catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }



    }



    public boolean createDetailofSale(int saleID, int productID, float instantPrice, float instantVat, int quantity, float subTotal) {



        try {
            Statement statement=connection.createStatement();
            statement.execute("insert into " + TABLE_DETAIL_OF_SALE + "("+COLUMN_SALEID+","+COLUMN_PRODUCTID_DETAILOFSALE+","+COLUMN_INSTANT_PRICE+","+COLUMN_INSTANT_VAT+","+COLUMN_QUANTITY_DETAILOFSALE+","+COLUMN_SUBTOTAL+") values(" + "'" + saleID + "'," + "'" + productID + "'," + "'" + instantPrice + "'," +"'" + instantVat + "'," + "'" + quantity + "'," + "'" + subTotal + "'"+ ")");
            System.out.println("details added into table");

            return true;


        } catch (SQLException sqlException) {
            System.out.println("details couldnt add "+ sqlException.toString());

            return false;
        }



    }

    public boolean addSubProducts(int connectedProductID, int subProductID, int quantity) {



        try {
            Statement statement=connection.createStatement();
            statement.execute("insert into " + TABLE_CONNECTED_PRODUCT + "("+COLUMN_CONNECTED_PRODUCTID+","+COLUMN_SUB_PRODUCTID+","+COLUMN_SUB_QUANTITY+") values(" + "'" + connectedProductID + "'," + "'" + subProductID + "'," + "'" + quantity + "')");
            System.out.println("SUB PRODUCTS ADDED");

            return true;


        } catch (SQLException sqlException) {
            System.out.println("sub productssss couldnt add "+ sqlException.toString());

            return false;
        }



    }

    public boolean deleteSubProducts(int connectedProductID) {

        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM " + TABLE_CONNECTED_PRODUCT + " WHERE " + COLUMN_CONNECTED_PRODUCTID + "=" + connectedProductID + ";");

            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }



    }

    public boolean setMainPIDasZeroForOldSubItems(ObservableList oldSubProducts) {

        try {

            Statement statement = connection.createStatement();

            Iterator iterator=oldSubProducts.iterator();
            while (iterator.hasNext()){
                Product oldSub= (Product) iterator.next();

                int updatedMainProductID=statement.executeUpdate("UPDATE " + TABLE_PRODUCT + " SET "+  COLUMN_MAINPRODUCTID+"='"+0+"'  WHERE "+ COLUMN_PRODUCTID+"="+oldSub.getProductID());

            }

            return true;

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("oldsub product couldnt updated!!! ");
            return false;
        }
    }

    public File checkFileDestination() {

        StringBuilder sb = new StringBuilder("SELECT ");

        int id=1;

        sb.append(COLUMN_SELECTEDPATH);
        sb.append(" FROM ");
        sb.append(TABLE_OPTION);
        sb.append(" WHERE ");
        sb.append(COLUMN_OPTIONID);
        sb.append("=");
        sb.append(id+";");



        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());

            if(!rs.wasNull()){

                File selectedPath= new File(rs.getString(COLUMN_SELECTEDPATH));
                return selectedPath;

            }
            System.out.println("rs.wasnull true döndü");
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("hata verdi");
            return null;

        }



    }

    public ArrayList<Sale> getSalesOfAnalysingCustomer(int customerID) {

        StringBuilder sb = new StringBuilder("Select * FROM ");
        sb.append(TABLE_SALE);
        sb.append(" WHERE ");
        sb.append(COLUMN_CUSTOMERID+ "=");
        sb.append(customerID);

        ArrayList<Sale> allSales = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());

            int i=0;

            while (rs.next()) {
                i++;
                Sale sale = new Sale();
               sale.setSaleID(rs.getInt(COLUMN_SALEID));
               sale.setCustomerID(rs.getInt(COLUMN_CUSTOMERID_SALE));
               sale.setTotalPrice(rs.getFloat(COLUMN_TOTAL_PRICE));
               sale.setTotalVat(rs.getFloat(COLUMN_TOTAL_VAT));
               sale.setDate(rs.getString(COLUMN_DATE));
               sale.setTime(rs.getString(COLUMN_TIME));

               sale.setNumber(i); // to number sale of rows
                allSales.add(sale);
            }

            return allSales;

        } catch (SQLException e) {

            System.out.println(" sale sorgusu başarısız " + e.getMessage());
            return null;
        }

    }

    public ArrayList<DetailofSale> getDeailsOfSalesForAnalysingCustomer(int saleID) {

        StringBuilder sb = new StringBuilder("Select * FROM ");
        sb.append(TABLE_DETAIL_OF_SALE);
        sb.append(" WHERE ");
        sb.append(COLUMN_SALEID_DETAILOFSALE+ "=");
        sb.append(saleID);

        ArrayList<DetailofSale> allDetails = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());

            int i=0;

            while (rs.next()) {
                i++;
                DetailofSale detailofSale = new DetailofSale();
               detailofSale.setProductID(rs.getInt(COLUMN_PRODUCTID));
               detailofSale.setSubTotal(rs.getFloat(COLUMN_SUBTOTAL));
               detailofSale.setQuantity(rs.getInt(COLUMN_QUANTITY_DETAILOFSALE));
               detailofSale.setInstantPrice(rs.getFloat(COLUMN_INSTANT_PRICE));
               detailofSale.setInstantVat(rs.getFloat(COLUMN_INSTANT_VAT));

               detailofSale.setNumber(i);

                allDetails.add(detailofSale);
            }

            return allDetails;

        } catch (SQLException e) {

            System.out.println(" detail of sale sorgusu başarısız " + e.getMessage());
            return null;
        }

    }




    ///customer analysis begin//


    public ArrayList<CustomerPieChart> getPieDataofGivenCustomer(int customerID){

        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(COLUMN_BARCODE);
        sb.append(", ");
        sb.append(TABLE_PRODUCT+"."+COLUMN_LABEL);
        sb.append(", ");
        sb.append(TABLE_PRODUCT+"."+COLUMN_NAME_PRODUCT);
        sb.append(", ");
        sb.append(TABLE_DETAIL_OF_SALE+"."+COLUMN_PRODUCTID_DETAILOFSALE);
        sb.append(", SUM(");
        sb.append(COLUMN_QUANTITY_DETAILOFSALE);
        sb.append(") as quantity,");
        sb.append(" SUM(");

    //    SELECT barcode,product.productID,product.label,product.nameProduct, detail_of_sale.productID

        sb.append(COLUMN_SUBTOTAL);
        sb.append(") as subTotal FROM ");
        sb.append(TABLE_DETAIL_OF_SALE);
        sb.append(" INNER JOIN ");
        sb.append(TABLE_SALE);
        sb.append(", ");
        sb.append(TABLE_PRODUCT);
        sb.append(" ON ");
        sb.append(TABLE_SALE+"."+COLUMN_SALEID);
        sb.append(" = ");
        sb.append(TABLE_DETAIL_OF_SALE+"."+COLUMN_SALEID_DETAILOFSALE);
        sb.append(" AND ");
        sb.append(TABLE_PRODUCT+"."+COLUMN_PRODUCTID);
        sb.append(" = ");
        sb.append(TABLE_DETAIL_OF_SALE+"."+COLUMN_PRODUCTID_DETAILOFSALE);
        sb.append(" AND ");
        sb.append(TABLE_SALE+"."+COLUMN_CUSTOMERID);
        sb.append(" = ");
        sb.append(customerID);
        sb.append(" GROUP BY ");
        sb.append(TABLE_DETAIL_OF_SALE+"."+COLUMN_PRODUCTID_DETAILOFSALE);
        sb.append(" ORDER BY subTotal DESC;");


        System.out.println(sb.toString());

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());

            ArrayList<CustomerPieChart> allPieChartData = new ArrayList<>();

            while (rs.next()) {

                CustomerPieChart pieChartData=new CustomerPieChart();

                pieChartData.setBarcode(rs.getString(COLUMN_BARCODE));
                pieChartData.setLabel(rs.getString(COLUMN_LABEL));
                pieChartData.setNameProduct(rs.getString(COLUMN_NAME_PRODUCT));
                pieChartData.setProductID(rs.getInt(COLUMN_PRODUCTID_DETAILOFSALE));
                pieChartData.setQuantity(rs.getInt(COLUMN_QUANTITY_DETAILOFSALE));
                pieChartData.setTotalSubPrice(rs.getDouble(COLUMN_SUBTOTAL));


                System.out.println(pieChartData.toString());


                allPieChartData.add(pieChartData);
            }

            return allPieChartData;

        } catch (SQLException e) {

            System.out.println("PieChart Analiz SORGU BAŞARISIZ " + e.getMessage());

            return null;
        }

    }


    //customer analysis end//


}
