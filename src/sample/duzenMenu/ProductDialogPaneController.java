package sample.duzenMenu;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.pepperonas.fxiconics.FxIconicsLabel;
import com.pepperonas.fxiconics.MaterialColor;
import com.pepperonas.fxiconics.cmd.FxFontCommunity;
import com.pepperonas.fxiconics.gmd.FxFontGoogleMaterial;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import sample.dataModel.DataSource;
import sample.model.ConnectedProduct;
import sample.model.Product;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import static javax.xml.bind.DatatypeConverter.parseInt;
import static sun.misc.FloatingDecimal.parseFloat;


public class ProductDialogPaneController {
    ObservableList<Product> subProductList = FXCollections.observableArrayList();

    public Product editingProduct = new Product();
    final ObservableList<Product> data=FXCollections.observableArrayList(DataSource.getInstance().getAllProducts());
    public SortedList<Product> sortedList;
    boolean editing=false; //if no item editing then setvisible edit button and delete button its using on method itemSelected();


    @FXML
    private TableView tableProduct;

    @FXML
    private TableView tableSubProduct;

    @FXML
    private JFXTextField tfLabel;

    @FXML
    private JFXTextField tfNameProduct;

    @FXML
    private JFXTextField tfStock;

    @FXML
    private JFXTextField tfSellingPrice;

    @FXML
    private JFXTextField tfBuyingPrice;

    @FXML
    private JFXTextField tfVAT;

    @FXML
    private JFXButton buttonDeleteProduct;

    @FXML
    private JFXButton buttonEditProduct;

    @FXML
    private JFXButton buttonCreateProduct;


    @FXML
    private JFXButton buttonCancel;

    @FXML
    private JFXButton buttonUpdateProduct;

    @FXML
    private JFXTextField tfSearchProduct;

    @FXML
    private JFXButton buttonAddSubProduct;

    @FXML
    private JFXButton buttonDeleteSubProduct;

    @FXML
    private DialogPane dpProductDialogPane;

    @FXML
    private JFXButton buttonUpdateSubProductQuantity;

    @FXML
    private  Label labelSubProducList;

    @FXML
    private JFXTextField tfBarcode;

    float newVAT=0;

    @FXML
    public void initialize() {
//when the program starts if there is no selected item program will stop running.To handle with this problem we disable edit and delete buttons here.

        tableProduct.setItems(data);

        buttonCreateProduct.setDisable(false);

        Validator validator = new Validator();

        validator.JfxTextFieldisEmptyValidator(tfLabel);
        validator.JfxTextFieldisEmptyValidator(tfNameProduct);
        validator.JfxTextFieldisEmptyValidator(tfStock);
        validator.JfxTextFieldisEmptyValidator(tfBuyingPrice);
        validator.JfxTextFieldisEmptyValidator(tfSellingPrice);
        validator.JfxTextFieldisEmptyValidator(tfBarcode);
        validator.enterOnlyDigits(tfStock);
        validator.enterOnlyFloat(tfBuyingPrice);
        validator.enterOnlyFloat(tfSellingPrice);
        validator.enterOnlyFloat(tfVAT);

        //SEARCH PRODUCT TEXTFIELD PROPERTY
        FilteredList<Product> filteredList = new FilteredList<Product>(data, e-> true);
        if (tfSearchProduct != null || !tfSearchProduct.getText().isEmpty()) {
            tfSearchProduct.setOnKeyPressed(e -> {
                tfSearchProduct.textProperty().addListener(((observable, oldValue, newValue) -> {
                    filteredList.setPredicate((Predicate<? super Product>) product -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue.toLowerCase();
                        String labelandName=product.getLabel().toLowerCase()+" "+product.getNameProduct().toLowerCase();
                        if (product.getLabel().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (product.getNameProduct().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        else if(labelandName.toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(product.getBarcode().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                }));
                sortedList = new SortedList<Product>(filteredList);
                sortedList.comparatorProperty().bind(tableProduct.comparatorProperty());

                //  tableProduct.itemsProperty().bind(urunler);

              tableProduct.setItems(sortedList);

            });
        }




        FxIconicsLabel labelHeader =
                (FxIconicsLabel) new FxIconicsLabel.Builder(FxFontCommunity.Icons.cmd_shopping)
                        .size(24)
                        .color(MaterialColor.BLACK)
                        .text("PRODUCTS",20,ContentDisplay.RIGHT)
                        .build();




    dpProductDialogPane.setHeader(labelHeader);
 //   dpProductDialogPane.headerProperty().





    }

    @FXML
    public void callCreateNewProduct() {

        Product product = new Product();

        try {
            int stock = Integer.parseInt(tfStock.getText());

            if (stock < 0) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Invalid Stock Number!");
                alert.setContentText("Please enter a possitive number...");
                alert.showAndWait();

                tfStock.requestFocus();
                tfStock.requestFocus();
                dpProductDialogPane.requestFocus();
            }

            product.setSellingPrice(parseFloat(tfSellingPrice.getText().trim()));
            product.setBuyingPrice(parseFloat(tfBuyingPrice.getText().trim()));
            product.setStock(parseInt(tfStock.getText().trim()));


            if(tfBarcode.getText().trim().isEmpty() || tfLabel.getText().trim().isEmpty() || tfNameProduct.getText().trim().isEmpty() || tfStock.getText().trim().isEmpty() || tfSellingPrice.getText().trim().isEmpty()||tfBuyingPrice.getText().trim().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Missing or invalid registry!");
                alert.setContentText("Please fill required fields...");
                tfStock.requestFocus();
                alert.showAndWait();

            }else{

                product.setBarcode(tfBarcode.getText().trim());
                product.setLabel(tfLabel.getText().trim());
                product.setNameProduct(tfNameProduct.getText().trim());


                if(tfVAT.getText().trim().isEmpty() || tfVAT.getText().trim()==null){

                    float sellingPrice= parseFloat(tfSellingPrice.getText().trim());
                    newVAT=sellingPrice*0.18f;

                    newVAT = (float) (Math.round(newVAT * 100.0) / 100.0);
                    product.setVat(newVAT);

                }else{
                    product.setVat(parseFloat(tfVAT.getText().trim()));
                    newVAT= parseFloat(tfVAT.getText().trim());
                }


                createNewProduct();
            }


            } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Number Entered!");
            alert.setContentText("Please enter a possitive number...");

            tfBarcode.requestFocus();
            tfLabel.requestFocus();
            tfNameProduct.requestFocus();
            tfSellingPrice.requestFocus();
            tfBuyingPrice.requestFocus();
            tfStock.requestFocus();
            dpProductDialogPane.requestFocus();

            alert.showAndWait();

        }
    }

    public void createNewProduct(){

        try {

            Task<Boolean> taskAddNewProduct = new Task() {
                @Override
                protected Object call() throws Exception {
                    Product product =new Product();

                    product.setBarcode(tfBarcode.getText().toUpperCase().trim());
                    product.setLabel(tfLabel.getText().toUpperCase().trim());
                    product.setNameProduct(tfNameProduct.getText().toUpperCase().trim());
                    product.setStock(parseInt(tfStock.getText().trim()));
                    product.setSellingPrice(parseFloat(tfSellingPrice.getText().trim()));
                    product.setBuyingPrice(parseFloat(tfBuyingPrice.getText().trim()));
                    product.setVat(newVAT);

                    return DataSource.getInstance().addNewProduct(product.getBarcode(),product.getLabel(),product.getNameProduct(),product.getStock(),product.getSellingPrice(),product.getBuyingPrice(),product.getVat());

                }
            };
            taskAddNewProduct.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    tfBarcode.clear();
                    tfSellingPrice.clear();
                    tfBuyingPrice.clear();
                    tfLabel.clear();
                    tfNameProduct.clear();
                    tfStock.clear();
                    tfVAT.clear();
                    tableProduct.getSelectionModel().selectLast();

                    getAllProductsTable();
                }
            });

            new Thread(taskAddNewProduct).start();

        }catch (NumberFormatException e){
            System.out.println("numberFormatException " + e);

        }

    }

    @FXML
    public void deleteProduct() {

        if (tableProduct.getSelectionModel().getSelectedItem() != null){

            Product deletingProduct= (Product) tableProduct.getSelectionModel().getSelectedItem();
            if(deletingProduct.getMainProductID()==0) {

                Product selectedProduct = (Product) tableProduct.getSelectionModel().getSelectedItem();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("DELETE");
                alert.setContentText("Are you sure to delete " + selectedProduct.getLabel() + " " + selectedProduct.getNameProduct() + " ?");
                Optional<ButtonType> result = alert.showAndWait();


                if (result.get() == ButtonType.OK) {
                    DataSource.getInstance().deleteProduct(itemSelected()); // deleting selected product
                    getAllProductsTable();
                    buttonDeleteProduct.setDisable(true);
                    buttonEditProduct.setDisable(true);
                    buttonCreateProduct.setDisable(false);

                    dpProductDialogPane.requestFocus();
                }

            }else{

               Product mainP = DataSource.getInstance().getSearchedProduct(deletingProduct.getMainProductID());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("DELETION EXCEPTION");
                alert.setContentText("This product could not deleted because \n" +
                       "The product you have tried to delete is Sub Product of "+ mainP.getBarcode()+ " " +mainP.getLabel()+" "+ mainP.getNameProduct());
                Optional<ButtonType> result = alert.showAndWait();
            }
    }
    }

    @FXML
    public void editProductButton() {


                editing=true; // will be false when click on cancel or update button

                buttonCancel.setVisible(true);
                buttonCancel.setDisable(false);
                buttonUpdateProduct.setVisible(true);
                buttonUpdateProduct.setDisable(false);

                labelSubProducList.setDisable(false);
                tableSubProduct.setDisable(false);
                buttonAddSubProduct.setDisable(false);
                buttonAddSubProduct.setVisible(true);
                buttonDeleteSubProduct.setDisable(false);
                buttonDeleteSubProduct.setVisible(true);
                buttonUpdateSubProductQuantity.setDisable(false);
                buttonUpdateSubProductQuantity.setVisible(true);

                buttonCreateProduct.setDisable(true);
                buttonDeleteProduct.setDisable(true);
                buttonEditProduct.setDisable(true);

                tableSubProduct.getItems().clear();



                editingProduct= (Product) tableProduct.getSelectionModel().getSelectedItem();   ///<<<< EDITING PRODUCT <<<<<<<

                tfBarcode.setText(editingProduct.getBarcode().toUpperCase().trim());
                tfLabel.setText(editingProduct.getLabel().toUpperCase().trim());
                tfNameProduct.setText(editingProduct.getNameProduct().toUpperCase().trim());
                tfStock.setText(String.valueOf(editingProduct.getStock()).trim());
                tfSellingPrice.setText(String.valueOf(editingProduct.getSellingPrice()).trim());
                tfBuyingPrice.setText(String.valueOf(editingProduct.getBuyingPrice()).trim());
                tfVAT.setText(String.valueOf(editingProduct.getVat()).trim());
                subProductList.clear();

                subProductList=getSubProducts(editingProduct.getProductID());
                tableSubProduct.setItems(subProductList);



    }

    @FXML
    public void updateButton(){




        if(!tfBarcode.getText().trim().isEmpty()&& tfBarcode.getText().trim()!=null && !tfLabel.getText().trim().isEmpty()&& tfLabel.getText().trim()!=null && !tfNameProduct.getText().trim().isEmpty() && tfNameProduct.getText()!=null && !tfStock.getText().trim().toString().isEmpty() && tfStock.getText().trim().toString()!=null && !tfSellingPrice.getText().trim().toString().isEmpty() && tfSellingPrice.getText().toString()!=null && !tfBuyingPrice.getText().trim().toString().isEmpty() && tfBuyingPrice.getText().trim().toString()!=null && !tfBarcode.getText().trim().toString().isEmpty() && tfBarcode.getText().trim().toString()!=null) {

            Product selectedProduct = editingProduct;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("UPDATE");
            alert.setContentText("Are you sure to update " + selectedProduct.getLabel() + " " + selectedProduct.getNameProduct() + " with new inputs?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                tableProduct.setDisable(false);
                buttonCreateProduct.setDisable(false);
                buttonEditProduct.setDisable(true);
                buttonDeleteProduct.setDisable(true);
                buttonCancel.setVisible(false);
                buttonUpdateProduct.setVisible(false);

                Task<Boolean> taskUpdateProduct = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        int stock = parseInt(tfStock.getText());

                float sellingPrice = parseFloat(tfSellingPrice.getText().trim());
                        float buyingPrice = parseFloat(tfBuyingPrice.getText());

                        if(tfVAT.getText().trim().isEmpty() || tfVAT.getText().trim()==null){


                            newVAT=sellingPrice*0.18f;

                            newVAT = (float) (Math.round(newVAT * 100.0) / 100.0);
                            editingProduct.setVat(newVAT);

                        }else{
                            editingProduct.setVat(parseFloat(tfVAT.getText().trim()));
                            newVAT= parseFloat(tfVAT.getText().trim());
                        }



                        if(subProductList!=null && !subProductList.isEmpty()&& editingProduct.getMainProduct()==0){ // adding subProducst first time
                            Iterator<Product> spIterator = subProductList.iterator();

                            while(spIterator.hasNext()){
                                Product listItem = spIterator.next();
                                int connectedProductID=editingProduct.getProductID(); // this is mainProduct (editing Product)
                                int subProductID=listItem.getProductID();
                                int quantity=listItem.getQuantity();
                                DataSource.getInstance().addSubProducts(connectedProductID,subProductID,quantity);
                                editingProduct.setMainProduct(1);
                            }
                        }else if(subProductList!=null && !subProductList.isEmpty() && editingProduct.getMainProduct()==1){// it means user updating subProduct list

                            //we will use this list if any sub item deleted from sub list we will update its mainProductID column on product(DB) as 0
                            //cause if its deleted from sublist that means its not subproduct anymore.. :)

                            DataSource.getInstance().deleteSubProducts(editingProduct.getProductID()); // here first we delete old tupples from database because it is more safe rather than updating directly

                            Iterator<Product> spIterator = subProductList.iterator();

                            while(spIterator.hasNext()){
                                Product listItem = spIterator.next();
                                int connectedProductID=editingProduct.getProductID(); // this is mainProduct (editing Product)
                                int subProductID=listItem.getProductID();
                                int quantity=listItem.getQuantity();
                                DataSource.getInstance().addSubProducts(connectedProductID,subProductID,quantity); // here we add new subProducs List


                                System.out.println(listItem.getBarcode()+listItem.getNameProduct()+ " sub list items");

                                editingProduct.setMainProduct(1);


                            }
                        }else if(subProductList==null || subProductList.isEmpty() && editingProduct.getMainProduct()==1){

                            DataSource.getInstance().deleteSubProducts(editingProduct.getProductID()); // DELETING ALL SUB PRODUCTS THOSE CONNECTED WITH EDITING PRODUCT
                            editingProduct.setMainProduct(0);
                            editingProduct.setMainProductID(0);

                        }

                        return DataSource.getInstance().updateProduct(editingProduct.getProductID(), tfBarcode.getText().trim().toUpperCase(), tfLabel.getText().toUpperCase(), tfNameProduct.getText().toUpperCase(), stock, sellingPrice, buyingPrice, newVAT,editingProduct.getMainProduct());
                   }
                };

                taskUpdateProduct.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {



                        buttonCancel.setVisible(false);
                        buttonCancel.setDisable(true);
                        buttonUpdateProduct.setVisible(false);
                        buttonUpdateProduct.setDisable(true);

                        labelSubProducList.setDisable(true);
                        tableSubProduct.setDisable(true);
                        buttonAddSubProduct.setDisable(true);
                        buttonAddSubProduct.setVisible(false);
                        buttonDeleteSubProduct.setDisable(true);
                        buttonDeleteSubProduct.setVisible(false);
                        buttonUpdateSubProductQuantity.setDisable(true);
                        buttonUpdateSubProductQuantity.setVisible(false);

                        buttonCreateProduct.setDisable(false);
                        buttonDeleteProduct.setDisable(false);
                        buttonEditProduct.setDisable(false);

                        tableProduct.setDisable(false);

                        dpProductDialogPane.requestFocus();
                        tableSubProduct.getItems().clear();
                        labelSubProducList.setText("Sub Products List");
                        if (taskUpdateProduct.valueProperty().get()) {

                            Product newCustomer = new Product();

                            selectedProduct.setLabel(tfLabel.getText().toUpperCase().trim());
                            selectedProduct.setNameProduct(tfNameProduct.getText().toUpperCase().trim());
                            selectedProduct.setStock(parseInt(tfStock.getText().trim()));
                            selectedProduct.setSellingPrice(parseFloat(tfSellingPrice.getText().trim()));
                            selectedProduct.setBuyingPrice(parseFloat(tfBuyingPrice.getText().trim()));
                            selectedProduct.setBarcode(tfBarcode.getText().toUpperCase().trim());


                            getAllProductsTable();
                            tfBarcode.clear();
                            tableProduct.refresh();
                            tfLabel.clear();
                            tfNameProduct.clear();
                            tfStock.clear();
                            tfSellingPrice.clear();
                            tfBuyingPrice.clear();
                            tfVAT.clear();
                            tableSubProduct.getItems().clear();


                            editing=false;
                            subProductList.clear();


                        }

                    }

                });

                new Thread(taskUpdateProduct).start();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Validation Error");
            alert.setContentText("You have to fill all specified fields");

            alert.show();
        }
    }

    @FXML
    public void addSubProduct() throws IOException {
        Product selectedItem=(Product) tableProduct.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){

            if (selectedItem!=editingProduct){


                if (!subProductList.contains(selectedItem)){
                 //   System.out.println("seçilen item subliste yok ");
                    int inputQuantity=getInputDialogPane();
                    if(inputQuantity>0){

                            selectedItem.setQuantity(inputQuantity);
                            subProductList.add(selectedItem);
                            // tableSubProduct.getItems().add(selectedItem);

                    }else{
                        /*
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Input");
                        alert.setContentText("Please enter a positive number");
                        alert.showAndWait();*/
                    }
                }
                else{  // if added item aldready exist in the list we delete old one from list and table and we add back the item excat same postion and update its info
                  //  System.out.println("item daha önce olmadığı için else kısmında eklendi");
                    int index=subProductList.indexOf(selectedItem);
                    Product existingProductItem=(Product) tableSubProduct.getItems().get(index);
                    selectedItem.setQuantity(getInputDialogPane()+(existingProductItem.getQuantity()));
                    subProductList.remove(index);
                    subProductList.add(index,selectedItem);
                    tableSubProduct.refresh();
                }
            }else{
              //  System.out.println("YOU CANNOT ADDED MAIN PRODUCT AS A SUB PRODUCT SAME TIME");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid attempt!");
                alert.setContentText("You cannot define this product as a sub product of itself");
                alert.showAndWait();

            } }

        else{

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("Wrong Attempt");
            alert.setContentText("No Item Selected");
            alert.showAndWait();
        }
    }

    @FXML
    public void updateSubProduct() throws IOException {

        if(tableSubProduct.getSelectionModel().getSelectedItem()!=null){

            Product selectedSubItem= (Product) tableSubProduct.getSelectionModel().getSelectedItem();

            int inputQuantity = getInputDialogPane();
                selectedSubItem.setQuantity(inputQuantity);//we change selected item quantity by using dialogpane.
                int index = subProductList.indexOf(selectedSubItem); // we get selected item index in observable list to update for calculating new total price
                subProductList.remove(index); // we delete old item
                subProductList.add(index, selectedSubItem); // and we add updated item in same index
                    }


    }

    @FXML
    public void dropSubProduct(){
        if(tableSubProduct.getSelectionModel().getSelectedItem()!=null) {
            subProductList.remove(tableSubProduct.getSelectionModel().getSelectedItem());
            tableSubProduct.getItems().remove(tableProduct.getSelectionModel().getSelectedItem());
                    }else{

        }
    }


    @FXML
    public void cancelButton(){
        editing=false;

        buttonCancel.setVisible(false);
        buttonCancel.setDisable(true);
        buttonUpdateProduct.setVisible(false);
        buttonUpdateProduct.setDisable(true);

        labelSubProducList.setDisable(true);
        tableSubProduct.setDisable(true);
        buttonAddSubProduct.setDisable(true);
        buttonAddSubProduct.setVisible(false);
        buttonDeleteSubProduct.setDisable(true);
        buttonDeleteSubProduct.setVisible(false);
        buttonUpdateSubProductQuantity.setDisable(true);
        buttonUpdateSubProductQuantity.setVisible(false);

        buttonCreateProduct.setDisable(false);
        buttonDeleteProduct.setDisable(false);
        buttonEditProduct.setDisable(false);

        dpProductDialogPane.requestFocus();
        tableSubProduct.getItems().clear();
        labelSubProducList.setText("Sub Products List");

        tableProduct.setDisable(false);


        tfLabel.clear();
        tfNameProduct.clear();
        tfStock.clear();
        tfSellingPrice.clear();
        tfBuyingPrice.clear();
        tfVAT.clear();
        tfBarcode.clear();

    }

    @FXML
    public int itemSelected(){
        if(tableProduct.getSelectionModel().getSelectedItem()!=null){
            Product selectedProduct = (Product) tableProduct.getSelectionModel().getSelectedItem();
            if(editing==true) {

                buttonDeleteProduct.setDisable(true);
                buttonEditProduct.setDisable(true);
                tableSubProduct.setDisable(false);
            }else{
                buttonDeleteProduct.setDisable(false);
                buttonEditProduct.setDisable(false);


                labelSubProducList.setText(selectedProduct.getLabel()+ " "+selectedProduct.getNameProduct()+ " Sub Products List");
                subProductList=getSubProducts(selectedProduct.getProductID());
                tableSubProduct.setItems(subProductList);


            }

            int id=selectedProduct.getProductID();
            return id;}
        else{
            return -1;
        }

    }

    @FXML
    public int getInputDialogPane() throws IOException {
        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.initOwner(dpProductDialogPane.getScene().getWindow());
/*
        FXMLLoader fxmlLoader =new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/duzenMenu/getInputDialogPane.fxml"));
*/
        DialogPane dialogPane=new DialogPane();

        JFXTextField quantityJFXTextField=new JFXTextField();
        quantityJFXTextField.setPromptText("Quantity");
        quantityJFXTextField.setLabelFloat(true);
        quantityJFXTextField.setMaxHeight(200);
        quantityJFXTextField.setMaxWidth(300);
        quantityJFXTextField.setText("1");
        quantityJFXTextField.setPadding(new Insets(10,0,0,0));

        Validator validator = new Validator();

        validator.enterOnlyDigits(quantityJFXTextField);

        VBox vBox=new VBox();
        //    vBox.setSpacing(10);
        vBox.getChildren().addAll(quantityJFXTextField);
        dialogPane.setContent(vBox);
        //    dialog.getDialogPane().setContent(fxmlLoader.load());
        dialog.getDialogPane().setContent(dialogPane);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        //sonuc nesnesini gönderir
        Optional<ButtonType> result =dialog.showAndWait();

        if(result.get()==ButtonType.OK){

            tableSubProduct.refresh();
            return parseInt(quantityJFXTextField.getText());
        }
        // fxmlLoader.setLocation(getClass().getResource("/sample/duzenMenu/getInputDialogPane.fxml"));
        return 0;
    }


    //here we get all products from product Database
    @FXML
    public void getAllProductsTable(){

        Task<ObservableList<Product>> taskGetAllProducts=new GetAllProducts();
       // tableProduct.itemsProperty().bind(taskGetAllProducts.valueProperty());
        tableProduct.setItems(FXCollections.observableArrayList(DataSource.getInstance().getAllProducts()));

        new Thread(taskGetAllProducts).start();
        taskGetAllProducts.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

            }
        });

    }


    class GetAllProducts extends Task {

        @Override
        protected Object call() throws Exception {
            return FXCollections.observableArrayList(DataSource.getInstance().getAllProducts());
        }
    }

    public ObservableList<Product> getSubProducts(int ID){

        ObservableList<ConnectedProduct> connectedProductsINFOS =FXCollections.observableArrayList();

        ObservableList<Product> showingList =FXCollections.observableArrayList();

        connectedProductsINFOS.addAll((DataSource.getInstance().getAllSubProducts(ID)));
        //this method returns all if there exists sub products of given item by using its (product id) from connected_products table


        Iterator<ConnectedProduct> iterator = connectedProductsINFOS.iterator();
        showingList.clear();

        while(iterator.hasNext()){
            // Product product = new Product();
            ConnectedProduct connectedProduct = iterator.next();

//            System.out.println("CONNECTEDPRODUCSINFO>>> connectedProductID>"+ connectedProduct.getConnectedProductID()+ "subPID >>" +connectedProduct.getSubProductID());

            Product returnedProduct= DataSource.getInstance().getSearchedProduct(connectedProduct.getSubProductID());
//this method returns detail of added subProducts from products table
            Product subProduct =new Product();
            subProduct.setProductID(returnedProduct.getProductID());
            subProduct.setLabel(returnedProduct.getLabel());
            subProduct.setNameProduct(returnedProduct.getNameProduct());
            subProduct.setStock(returnedProduct.getStock());
            subProduct.setSellingPrice(returnedProduct.getSellingPrice());
            subProduct.setBuyingPrice(returnedProduct.getBuyingPrice());
            subProduct.setVat(returnedProduct.getVat());
            subProduct.setQuantity(connectedProduct.getSubQuantity()); ////// subQ PRODUCT'S QUANTITIY
            subProduct.setMainProduct(returnedProduct.getMainProduct());
            subProduct.setMainProductID(connectedProduct.getConnectedProductID());
            subProduct.setBarcode(returnedProduct.getBarcode());


            showingList.add(subProduct);
            //   System.out.println("SHOWING LIST ITEM ADDED    PRODUCTID=>"+product.getProductID()+ "   LABEL=>"+ product.getLabel()+ "  NAME=>"+ product.getNameProduct()+ "    QUANTITY=>" +product.getQuantity()+" ");

        }


        return showingList;
    }





}
