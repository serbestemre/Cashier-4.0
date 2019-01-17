package sample;

import analiysisModel.CustomerPieChart;
import com.jfoenix.controls.*;
import com.pepperonas.fxiconics.FxIconicsButton;
import com.pepperonas.fxiconics.FxIconicsLabel;
import com.pepperonas.fxiconics.MaterialColor;
import com.pepperonas.fxiconics.awf.FxFontAwesome;
import com.pepperonas.fxiconics.cmd.FxFontCommunity;
import com.pepperonas.fxiconics.gmd.FxFontGoogleMaterial;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.textfield.TextFields;
import sample.dataModel.DataSource;
import sample.duzenMenu.ProductDialogPaneController;
import sample.duzenMenu.Validator;
import sample.model.*;

import javax.sound.midi.Soundbank;
import javax.swing.filechooser.FileSystemView;
import javax.xml.crypto.Data;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.xml.bind.DatatypeConverter.parseInt;
import static javax.xml.bind.DatatypeConverter.parseString;
import static javax.xml.bind.DatatypeConverter.parseFloat;

public class Controller {


//SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    final ObservableList<Product> data = FXCollections.observableArrayList(DataSource.getInstance().getAllProducts());
    public SortedList<Product> sortedList;

    final ObservableList<Customer> dataCustomer = FXCollections.observableArrayList(DataSource.getInstance().getAllCustomers());

    ObservableList<Product> shoppingBasket = FXCollections.observableArrayList(); //we add it's item from tableShoppingBasket
    ArrayList deletionList = new ArrayList<>();
    ArrayList insertionQueue = new ArrayList();

    public float totalPrice = 0;
    public float totalVat = 0;
    public float totalCash = 0;


    //mainScreen Layout

    @FXML
    private BorderPane bpAnaPencere;

    @FXML
    private JFXToggleButton buttonTogglePrintList;

    @FXML
    private JFXButton buttonAdd;

    @FXML
    private JFXButton buttonDelete;
    @FXML
    private JFXButton buttonUpdate;

    @FXML
    private TableView tableProductInfo;

    @FXML
    public TableView tableShoppingBasket;

    @FXML
    public TableView tableCustomer;

    @FXML
    private Label labelTotalPrice;

    @FXML
    private JFXButton buttonClearCart;

    @FXML
    private Label labelDate;
    @FXML
    private Label labelTime;

    @FXML
    private JFXComboBox deneme;

    @FXML
    private JFXTextField textFieldCustomers;

    @FXML
    private ComboBox comboBoxCustomer;

    @FXML
    private Label labelTotalVAT;

    @FXML
    private Label labelChange;

    @FXML
    private JFXTextField tfCash;

    @FXML
    private JFXTextField tfSearchProduct;

    @FXML
    private JFXTextField tfSearchCustomer;

    @FXML
    private JFXToolbar toolBar;

    @FXML
    private HBox hBoxToolBarLeft;

    @FXML
    private HBox hBoxShoppingButtons;

    @FXML
    private VBox vbox;


    public DateTimeFormatter dateFormatter;
    public DateTimeFormatter timeFormatter;
    public String currentDate;
    public String currentTime;

    public DateTimeFormatter dateF;
    public DateTimeFormatter timeF;
    public String currentD;
    public String currentT;

    private Predicate<Product> allProducts;
    private Predicate<Product> searchedProducts;
    private FilteredList<Product> filteredList;


    @FXML
    public void initialize() {
        Validator validator = new Validator();
        comboBoxCustomer.setItems(dataCustomer);
        validator.enterOnlyFloat(tfCash);

        tableProductInfo.setItems(data);

/////// TOOL BAR FXICONS //////

    //****LABEL CUSTOMER
        FxIconicsLabel toolLabelCustomer =
                (FxIconicsLabel) new FxIconicsLabel.Builder(FxFontCommunity.Icons.cmd_account_multiple)
                        .size(24)
                        .color(MaterialColor.BLACK)
                        .build();

        Tooltip customersTip = new Tooltip();
        customersTip.setText("Customers");
        toolLabelCustomer.setTooltip(customersTip);

        toolLabelCustomer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    showCustomerDialogPane();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        toolLabelCustomer.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                toolLabelCustomer.setScaleX(0.9);
                toolLabelCustomer.setScaleY(0.9);
            }
        });

        toolLabelCustomer.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                toolLabelCustomer.setScaleX(1);
                toolLabelCustomer.setScaleY(1);
            }
        });
//___________________________________LABEL CUSTOMER ___________________________________________


    //********LABEL PRODUCT
        FxIconicsLabel toolLabelProduct =
                (FxIconicsLabel) new FxIconicsLabel.Builder(FxFontCommunity.Icons.cmd_shopping)
                        .size(24)
                        .color(MaterialColor.BLACK)
                        .build();

        Tooltip ProductTip = new Tooltip();
        ProductTip.setText("Products");
        toolLabelProduct.setTooltip(ProductTip);

        toolLabelProduct.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    showProductDialogPane();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        toolLabelProduct.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                toolLabelProduct.setScaleX(0.9);
                toolLabelProduct.setScaleY(0.9);

            }
        });

        toolLabelProduct.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                toolLabelProduct.setScaleX(1);
                toolLabelProduct.setScaleY(1);
            }
        });
//___________________________________LABEL PRODUCT ___________________________________________


///////////////////ADDD
       FxIconicsButton buttonFxAddToCart =
                (FxIconicsButton) new FxIconicsButton.Builder(FxFontCommunity.Icons.cmd_cart)
                        .size(20)
                        .color(MaterialColor.BLACK)
                        .text("ADD")
                        .build();
        Tooltip productsTip = new Tooltip();
        productsTip.setText("Add to Shopping List");
        buttonFxAddToCart.setTooltip(productsTip);
        buttonFxAddToCart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                try {
                    addtoShoppingBasket();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

//////////////// DROP
        FxIconicsButton buttonFxDropFromCart =
                (FxIconicsButton) new FxIconicsButton.Builder(FxFontCommunity.Icons.cmd_minus_circle)
                        .size(20)
                        .color(MaterialColor.RED_900)
                        .text("DROP")
                        .build();
        Tooltip dropTip = new Tooltip();
        dropTip.setText("Drop From Shopping List");
        buttonFxDropFromCart.setTooltip(dropTip);
        buttonFxDropFromCart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                    deleteFromShoppingBasket();

            }
        });


///////////CHANGE QUANTITY
        FxIconicsButton buttonFxChangeQuantity =
                (FxIconicsButton) new FxIconicsButton.Builder(FxFontCommunity.Icons.cmd_sync)
                        .size(20)
                        .color(MaterialColor.INDIGO_900)
                        .text("QUANTITY")
                        .build();
        Tooltip QuantityTip = new Tooltip();
        QuantityTip.setText("Change Quantity");
        buttonFxChangeQuantity.setTooltip(QuantityTip);
        buttonFxChangeQuantity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                try {
                    updateOnShoppinBasket();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        FxIconicsButton buttonFxClearCart =
                (FxIconicsButton) new FxIconicsButton.Builder(FxFontCommunity.Icons.cmd_cart_outline)
                        .size(20)
                        .color(MaterialColor.BLACK)
                        .text("CLEAR")
                        .build();
        Tooltip clearTrip = new Tooltip();
        clearTrip.setText("Clear Shopping List");
        buttonFxClearCart.setTooltip(clearTrip);
        buttonFxClearCart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            clearCartButton();

            }
        });



        hBoxToolBarLeft.getChildren().addAll(toolLabelProduct,toolLabelCustomer);
        hBoxShoppingButtons.getChildren().addAll(buttonFxAddToCart,buttonFxDropFromCart,buttonFxChangeQuantity,buttonFxClearCart);


/////////// TOOLBAR FXICONS ////////////

        tableShoppingBasket.requestFocus();



    //    AutoCompleteComboBoxListener completeComboBoxListener = new AutoCompleteComboBoxListener(comboBoxCustomer);

        //SEARCH PRODUCT TEXTFIELD PROPERTY
        FilteredList<Product> filteredList = new FilteredList<Product>(data, e -> true);
        if (tfSearchProduct != null || !tfSearchProduct.getText().isEmpty()) {
            tfSearchProduct.setOnKeyPressed(e -> {
                tfSearchProduct.textProperty().addListener(((observable, oldValue, newValue) -> {
                    filteredList.setPredicate((Predicate<? super Product>) product -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue.toLowerCase();
                        String labelandName = product.getLabel().toLowerCase() + " " + product.getNameProduct().toLowerCase();
                        if (product.getLabel().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (product.getNameProduct().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (labelandName.toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (product.getBarcode().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false;
                    });
                }));
                sortedList = new SortedList<Product>(filteredList);
                sortedList.comparatorProperty().bind(tableProductInfo.comparatorProperty());

                //  tableProduct.itemsProperty().bind(urunler);

                tableProductInfo.setItems(sortedList);

            });
        }

        dateF = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        timeF = DateTimeFormatter.ofPattern("HH.mm.ss");


        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        tableShoppingBasket.setItems(shoppingBasket);

        labelDate.setText(String.valueOf(LocalDate.now()));
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            labelTime.setText(LocalTime.now().format(timeFormatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();



    tfCash.textProperty().addListener(new ChangeListener<String>() {
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String  newValue) {
        try {
        totalCash= Float.parseFloat(newValue);
        calculateTotalPriceofShoppingBasket();

        }catch (NumberFormatException e){

        }
        if(newValue==null || newValue.isEmpty()){
            labelChange.setText("00.00");
        }



    }
});

        //here show total price of shopping basket
    shoppingBasket.addListener(new ListChangeListener<Product>() {
            @Override
            public void onChanged(Change<? extends Product> c) {
                calculateTotalPriceofShoppingBasket();


            }
        });


    }

    @FXML
    public Customer onSelectCustomer() {

            try{
            Customer selectedCustomer = (Customer) comboBoxCustomer.getSelectionModel().getSelectedItem();

                return selectedCustomer;
        }catch (Exception e){
                System.out.println("onSecetCustomer() "+ e);
                Customer selectedCs=new Customer();
                selectedCs.setCustomerID(0);
                return selectedCs;
            }


    }

    public void findProduct(){
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
                sortedList.comparatorProperty().bind(tableProductInfo.comparatorProperty());

                //  tableProduct.itemsProperty().bind(urunler);

                tableProductInfo.setItems(sortedList);

            });
        }

    }


//here we show all products into tableview left side of screen. Till we add barcode reader device we will use this method.
@FXML
public void getAllProductsTable(){

    Task<ObservableList<Product>> taskGetAllProducts=new GetAllProducts();
    // tableProduct.itemsProperty().bind(taskGetAllProducts.valueProperty());
    tableProductInfo.setItems(FXCollections.observableArrayList(DataSource.getInstance().getAllProducts()));

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

    @FXML
    public void clearCartButton(){
        tableShoppingBasket.getItems().clear();
        shoppingBasket.clear();
    }


    //this method shows Customer Dialog Pane below the edit menu item
    @FXML
    public void showCustomerDialogPane() throws IOException, SQLException {
        /*
        Dialog<ButtonType> dialog2 = new Dialog<ButtonType>();
        dialog2.initOwner(bpAnaPencere.getScene().getWindow());
        Parent root= FXMLLoader.load(getClass().getResource("/sample/duzenMenu/CustomerDialogPane.fxml"));

        dialog2.getDialogPane().setContent(root);*/

        Dialog<ButtonType> dialog2 = new Dialog<ButtonType>();
        dialog2.initOwner(bpAnaPencere.getScene().getWindow());
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/duzenMenu/CustomerDialogPane.fxml"));
        Parent dialogContent =fxmlLoader.load();
    //    MusterilerDialogPaneController musterilerDialogPaneController = fxmlLoader.<MusterilerDialogPaneController>getController();
    //    musterilerDialogPaneController.getAllCustomersTable();
        dialog2.getDialogPane().setContent(dialogContent);

        //ButtonType btn= new ButtonType("ADD NEW CUSTOMER");
        // dialog2.getDialogPane().getButtonTypes().add(btn);

        dialog2.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        //sonuc nesnesini gönderir
        Optional<ButtonType> result =dialog2.showAndWait();


        if(result.get()==ButtonType.CLOSE) {

        }
    }


 //kullanılmıyor
    @FXML
    public void showDailyAnalysis() throws IOException {


    Dialog<ButtonType> dialog2 = new Dialog<ButtonType>();
    dialog2.initOwner(bpAnaPencere.getScene().getWindow());
    FXMLLoader fxmlLoader=new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/sample/analysisMenu/dailyAnalysisDialogPane.fxml"));
    Parent dialogContent =fxmlLoader.load();

    dialog2.getDialogPane().setContent(dialogContent);


        dialog2.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        Optional<ButtonType> result =dialog2.showAndWait();


    }


    class GetProductInfo extends Task {

        @Override
        protected Object call() throws Exception {
            return FXCollections.observableArrayList(DataSource.getInstance().getAllProducts());
        }
    }

    public int returnTotalQuantity(Product product){

        Iterator shoppingbasket=shoppingBasket.iterator();
        int totalQuantity=0;
        while (shoppingbasket.hasNext()){
            Product sb= (Product) shoppingbasket.next();
            if(sb.getProductID()==product.getProductID()){
                totalQuantity=sb.getQuantity();
            }
        }

return totalQuantity;
    }

//here we add selected item from left table and add this products into shopping basket
@FXML
public void addtoShoppingBasket() throws IOException {
insertionQueue.clear();

        if(tableProductInfo.getSelectionModel().getSelectedItem()!=null) { // eğer bir ürün seçiliyse // if any product selected?
            Product selectedItem = (Product) tableProductInfo.getSelectionModel().getSelectedItem(); //
            int inputQuantity = getInputDialogPane(); // Kaç tane ürün eklenmek istiyor? // how many products user wants to buy?

            if (inputQuantity != 0) {

                int totalQ = returnTotalQuantity(selectedItem); // if this item added already in the shopping basket we are subsraction added quantity

                if (selectedItem.getStock() - totalQ >= inputQuantity) { // STOK KONTROL eğer eklenmek istenen ürün stokta varsa // Checking stock if its avaliable for selling
                    //selectedItem.setQuantity(inputQuantity);

                    if (selectedItem.getMainProduct() == 1) { // eğer sub ürünü varsa // if selected product has subProducts
                        //that means its main product and we need to check its sub products
                      //  System.out.println("main product ekleniyor");
                        //1)sub ürün listesini al, // get all subProducts of selectedItem
                        //2) sub ürün stok kontrollerini yap main.Quantity*sub.Quantity <= Sub.stock // check its stock
                        //3) eğer stokta varsa tabloya ekle yoksa uyarı ver // if any of subitems out of stock capasity then don't let user to buy the main product and its subProducts
                        ProductDialogPaneController productDialogPaneController = new ProductDialogPaneController();
                        ObservableList returnedList = FXCollections.observableArrayList();
                        returnedList = productDialogPaneController.getSubProducts(selectedItem.getProductID()); // getting all subproducts of main Product


                        Iterator<Product> productIterator = returnedList.iterator();
                        boolean inStock = true;
                        boolean subExist = false;
                        while (productIterator.hasNext()) {
                            Product currentSubProduct = (Product) productIterator.next();
                            int totalSubQ = returnTotalQuantity(currentSubProduct);
                            if ((currentSubProduct.getQuantity() * inputQuantity) > currentSubProduct.getStock() - totalSubQ) {
                                //eğer eklenmek istenen alt ürünün tanımlanan Miktarı*Alınmak istenen Main ürünün miktarı stok kapasitesinden fazla değilse
                                //sub products should multiply with main product quantity
                                inStock = false;
                                subExist = false;
                            }
                        }


                        if (inStock == true) {
                            if (shoppingBasket.contains(selectedItem)) {
                                Iterator mainIterator = shoppingBasket.iterator();
                                while (mainIterator.hasNext()) {
                                    Product oldMain = (Product) mainIterator.next();
                                    if (oldMain.getProductID() == selectedItem.getProductID()) {
                                        oldMain.setQuantity(oldMain.getQuantity() + inputQuantity);

                                    }

                                }
                                Iterator<Product> subItemIterator = returnedList.iterator();
                                while (subItemIterator.hasNext()) {
                                    Product newSubItem = (Product) subItemIterator.next();
                                    Iterator ShoppingIterator = shoppingBasket.iterator();
                                    while (ShoppingIterator.hasNext()) {
                                        Product oldSubItem = (Product) ShoppingIterator.next();
                                        if (oldSubItem.getProductID() == newSubItem.getProductID() && oldSubItem.getMainProduct() != 1) {
                                            oldSubItem.setQuantity((newSubItem.getQuantity() * inputQuantity) + oldSubItem.getQuantity());
                                            calculateTotalPriceofShoppingBasket();
                                        }
                                    }
                                }
                            } else {
                                selectedItem.setQuantity(inputQuantity);


                                shoppingBasket.add(selectedItem);
                                // we should multiply subItem Quantity with new InputQuantity
                                Iterator iterator = returnedList.iterator();
                                while (iterator.hasNext()) {
                                    Product newQforSubItem = (Product) iterator.next();
                                    newQforSubItem.setQuantity(inputQuantity * newQforSubItem.getQuantity());

                                    Iterator iteratorShopping = shoppingBasket.iterator();
                                    while (iteratorShopping.hasNext()) {
                                        Product existingProduct = (Product) iteratorShopping.next();


                                        if (newQforSubItem.getProductID() == existingProduct.getProductID()) {
                                            existingProduct.setQuantity(existingProduct.getQuantity() + newQforSubItem.getQuantity());
                                        }
                                        if (!insertionQueue.contains(newQforSubItem) && !shoppingBasket.contains(newQforSubItem)) {
                                            insertionQueue.add(newQforSubItem);
                                        }////////////////////// burayı çöz
                                    }
                                }

                                shoppingBasket.addAll(insertionQueue);
                            }

                        } else {
                        //    System.out.println("ALMAK İSTEDİĞİNİZ MAİN ÜRÜNLE BİRLİKTE ALINMASI GEREKEN ALT ÜRÜNLERDEN BİRİ VEYA BİRKAÇI STOKTA YOK!");


                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Insufficient Stock Capacity");
                            alert.setContentText("Ooops, some of sub products of"+selectedItem.getLabel()+" "+selectedItem.getNameProduct()+" aren't in stock");

                            alert.showAndWait();


                        }
                    } else { //eğer sub ürünü yoksa (selectedItem.getMainProduct==0)
                        //if its not main product in other word if its standart Product
                        //   if(selectedItem.getQuantity()==0) {
                        selectedItem.setQuantity(inputQuantity);
                        // }
                        Iterator standartProduct = shoppingBasket.iterator();
                        if (shoppingBasket.contains(selectedItem)) {

                            while (standartProduct.hasNext()) {
                                Product standart = (Product) standartProduct.next();
                                if (standart.getProductID() == selectedItem.getProductID()) {
                                //    System.out.println("standart ürün set q ");

                                    standart.setQuantity(standart.getQuantity() + inputQuantity);
                                    calculateTotalPriceofShoppingBasket();

                                    // tableShoppingBasket.getItems().add(standart);
                                }
                            }
                        } else { // shoppingbasket is empty
                          //  System.out.println("standart ürün eklendi?");
                            selectedItem.setQuantity(inputQuantity);
                            shoppingBasket.add(selectedItem);
                            //  tableShoppingBasket.getItems().add(selectedItem);
                        }
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Insufficient Stock Capacity");
                    alert.setContentText("Ooops, this product isn't in stock");

                    alert.showAndWait();
                    //the product you wanted to buy is out of stock capacity
                }

            }
        }
             else{ // eğer ürün listesinden bir ürün seçilmediyse
                  //  System.out.println("ALIŞ VERİŞ SEPETİNE EKLENECEK ÜRÜNÜ SEÇMEDİNİZ !");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong Attempt");
            alert.setContentText("You didn't choose any product");

            alert.showAndWait();
                    //no item selected to buy
                }


tableShoppingBasket.refresh();
    }

//we use this method to change quantity of added product into shopping table
@FXML
public void updateOnShoppinBasket() throws IOException {
    insertionQueue.clear();

    if(tableShoppingBasket.getSelectionModel().getSelectedItem()!=null) { // eğer bir ürün seçiliyse // if any product selected?
        Product selectedItem = (Product) tableShoppingBasket.getSelectionModel().getSelectedItem(); //
        int inputQuantity = getInputDialogPane(); // Kaç tane ürün eklenmek istiyor? // how many products user wants to buy?

        if (inputQuantity != 0) {

            // if this item added already in the shopping basket we are subsraction added quantity

            if (selectedItem.getStock() >= inputQuantity) { // STOK KONTROL eğer eklenmek istenen ürün stokta varsa // Checking stock if its avaliable for selling
                //selectedItem.setQuantity(inputQuantity);

                if (selectedItem.getMainProduct() == 1) { // eğer sub ürünü varsa // if selected product has subProducts
                    //that means its main product and we need to check its sub products


                    //1)sub ürün listesini al, // get all subProducts of selectedItem
                    //2) sub ürün stok kontrollerini yap main.Quantity*sub.Quantity <= Sub.stock // check its stock
                    //3) eğer stokta varsa tabloya ekle yoksa uyarı ver // if any of subitems out of stock capasity then don't let user to buy the main product and its subProducts
                    ProductDialogPaneController productDialogPaneController = new ProductDialogPaneController();
                    ObservableList returnedList = FXCollections.observableArrayList();
                    returnedList = productDialogPaneController.getSubProducts(selectedItem.getProductID()); // getting all subproducts of main Product


                    Iterator<Product> productIterator = returnedList.iterator();
                    boolean inStock = true;

                    while (productIterator.hasNext()) {
                        Product currentSubProduct = (Product) productIterator.next();
                        if ((inputQuantity) > currentSubProduct.getStock()) {
                            //eğer eklenmek istenen alt ürünün tanımlanan Miktarı*Alınmak istenen Main ürünün miktarı stok kapasitesinden fazla değilse
                            //sub products should multiply with main product quantity
                            inStock = false;

                        }
                    }


                    if (inStock == true) {
                        if (shoppingBasket.contains(selectedItem)) {
                            Iterator mainIterator = shoppingBasket.iterator();
                            while (mainIterator.hasNext()) {
                                Product oldMain = (Product) mainIterator.next();
                                if (oldMain.getProductID() == selectedItem.getProductID()) {
                                    oldMain.setQuantity(inputQuantity);
                                 //   System.out.println("main quantity updated");

                                }

                            }
                            Iterator<Product> subItemIterator = returnedList.iterator();
                            while (subItemIterator.hasNext()) {
                                Product newSubItem =subItemIterator.next();
                                Iterator ShoppingIterator = shoppingBasket.iterator();
                                while (ShoppingIterator.hasNext()) {
                                    Product oldSubItem = (Product) ShoppingIterator.next();
                                    if (oldSubItem.getProductID() == newSubItem.getProductID() && oldSubItem.getMainProduct() != 1) {
                                        oldSubItem.setQuantity(inputQuantity*newSubItem.getQuantity());
                                        calculateTotalPriceofShoppingBasket();
                                    }
                                }
                            }
                        }

                        /*
                        else {
                            selectedItem.setQuantity(inputQuantity);
                            System.out.println("yeni ürün");

                            shoppingBasket.add(selectedItem);
                            // we should multiply subItem Quantity with new InputQuantity
                            Iterator iterator = returnedList.iterator();
                            while (iterator.hasNext()) {
                                Product newQforSubItem = (Product) iterator.next();
                                newQforSubItem.setQuantity(inputQuantity * newQforSubItem.getQuantity());

                                Iterator iteratorShopping = shoppingBasket.iterator();
                                while (iteratorShopping.hasNext()) {
                                    Product existingProduct = (Product) iteratorShopping.next();


                                    if (newQforSubItem.getProductID() == existingProduct.getProductID()) {
                                        existingProduct.setQuantity(existingProduct.getQuantity() + newQforSubItem.getQuantity());
                                    }
                                    if (!insertionQueue.contains(newQforSubItem) && !shoppingBasket.contains(newQforSubItem)) {
                                        insertionQueue.add(newQforSubItem);
                                    }////////////////////// burayı çöz
                                }
                            }

                            shoppingBasket.addAll(insertionQueue);
                        }*/

                    } else {
                      //  System.out.println("Sub Product Stock Problem!");

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Insufficient Stock Capacity");
                        alert.setContentText("Ooops, some of sub products of"+selectedItem.getLabel()+" "+selectedItem.getNameProduct()+" aren't in stock");

                        alert.showAndWait();

                    }
                } else { //eğer sub ürünü yoksa (selectedItem.getMainProduct==0)
                    //if its not main product in other word if its standart Product
                    //   if(selectedItem.getQuantity()==0) {
                    selectedItem.setQuantity(inputQuantity);

                    // } System.out.println("sub ürünü olmayan standart ürün güncellendi");
                    Iterator standartProduct = shoppingBasket.iterator();

                    if(shoppingBasket.contains(selectedItem)) {
                        while (standartProduct.hasNext()) {
                            Product standart = (Product) standartProduct.next();
                            if (standart.getProductID() == selectedItem.getProductID()) {
                              //  System.out.println("standart ürün set Quantity");

                                standart.setQuantity(inputQuantity);
                                calculateTotalPriceofShoppingBasket();
                            }
                        }
                    }
                }

            } else {
             //   System.out.println("ALMAK İSTEDİĞİNİZ MAİN ÜRÜN STOKTA YETERİ KADAR YOK");
                //the product you wanted to buy is out of stock capacity

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Insufficient Stock Capacity");
                alert.setContentText("Ooops, this product isn't in stock");

                alert.showAndWait();
            }

        }  }
        else{ // eğer ürün listesinden bir ürün seçilmediyse
          //  System.out.println("GÜNCELLENECEK ÜRÜNÜ SEÇMEDİNİZ !");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Wrong Attempt!");
        alert.setContentText("You didn't choose any item to update");

        alert.showAndWait();
            //no item selected to buy
        }


    tableShoppingBasket.refresh();
}

public void calculateTotalPriceofShoppingBasket(){
    Iterator<Product> productIterator=shoppingBasket.iterator();
    if (shoppingBasket.isEmpty() || shoppingBasket==null){
        totalPrice=0;
        labelTotalPrice.setText("0.00");
    }
    totalPrice=0;
    totalVat=0;
    while(productIterator.hasNext()) {
        Product currentProductObject = productIterator.next();
        totalPrice+=currentProductObject.getSellingPrice()*currentProductObject.getQuantity();
        totalVat+=currentProductObject.getVat()*currentProductObject.getQuantity();
    }
    labelTotalPrice.setText(String.valueOf(totalPrice));
    labelTotalVAT.setText(String.valueOf(totalVat));
    labelChange.setText(String.valueOf(totalCash-(totalPrice+totalVat)));

}


//here we delete selected item from shopping basket tableview on the right side of the screen
@FXML
public void deleteFromShoppingBasket(){

        if(tableShoppingBasket.getSelectionModel().getSelectedItem()!=null) { // eğer seçilen bir ürün varsa silme işlemini başlat
            Product selectedProduct = (Product) tableShoppingBasket.getSelectionModel().getSelectedItem();
            //mainProductID özelliğini kullanabilmemiz için seçilen ürünü SubProduct Olarak tanımlıyoruz
            //bu sayede getMainID özelliğiyle eğer gerçekten subProduct ise geniş kapsamlı kontrol yapabileceğiz
            //eğer getMainID==0 ve getSubProduct==0 bu demektir ki seçilen ürün standart bir ürün.


      //      System.out.println("işlem yapılmak istenien ürün PID>>"+ selectedProduct.getProductID()+" mainID --->>  "+ selectedProduct.getMainProductID() + " subProductCode==>>>" +selectedProduct.getMainProduct());



         //   System.out.println(selectedProduct.getProductID() +" id ürün seçildi");
            if(selectedProduct.getMainProduct()==1){ // seçilen ürün mainProduct ise
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("You are going to delete a Main Product");
                alert.setContentText("If you will delete selected product, its sub products will be deleted as well");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){ // user wants to delete

                    ProductDialogPaneController productDialogPaneController = new ProductDialogPaneController();
                ObservableList returnedList=productDialogPaneController.getSubProducts(selectedProduct.getProductID());


                    Iterator shoppingItem = shoppingBasket.iterator();
                    while (shoppingItem.hasNext()){
                        Product shoppingProduct=(Product) shoppingItem.next();

                Iterator returnedItem=returnedList.iterator();
            while (returnedItem.hasNext()){
                Product subProduct = (Product) returnedItem.next();


            if(shoppingProduct.getProductID()==subProduct.getProductID()){ // eğer sub ürün shoppingbaskette bulunduysa
                if(shoppingProduct.getQuantity()==subProduct.getQuantity()*selectedProduct.getQuantity()){ // ve eğer sepetteki ürünün adeti eşitse subürünün miktarına
                    //bu demektir ki hepsini sil
                 //   System.out.println("shoppingProdct ID"+shoppingProduct.getProductID() + "Quantity" + shoppingProduct.getQuantity() + "== " + "subPRODUCT ID " + subProduct.getProductID() + "quantity " +subProduct.getQuantity());
                    deletionList.add(subProduct);

                }else{ //eğer shoppingQ > subQ dan bu demektir ki bu ürün ayrı olarak (Standart ürün olarak) ta sepete eklenmiş
                    //shoppingQ > rQ    || subitemlar sepetteki üründen fazla olamaz
                   // System.out.println("quantity set edildi çünkü sq > rq");
                shoppingProduct.setQuantity(shoppingProduct.getQuantity()-subProduct.getQuantity()*selectedProduct.getQuantity());
                }
            }
                } //while shopping end
            }//while returnedSubList end

                    deletionList.add(selectedProduct);
                    shoppingBasket.removeAll(deletionList);
                    deletionList.clear();
                  //  shoppingBasket.remove(selectedProduct);

                } else {
                }


            }else if(selectedProduct.getMainProduct()==0 && selectedProduct.getMainProductID()!=0){ // seçilen ürün subProduct ise
               // System.out.println("sub product silinmek istendi");

                Product mainProduct= DataSource.getInstance().getSearchedProduct(selectedProduct.getMainProductID());

                ProductDialogPaneController productDialogPaneController = new ProductDialogPaneController();

                Iterator shopping =shoppingBasket.iterator();
                while (shopping.hasNext()){
                    Product shoppingItem= (Product) shopping.next();
                    if(shoppingItem.getProductID()==mainProduct.getProductID()){
                        mainProduct.setQuantity(shoppingItem.getQuantity());
                    }
                }

                ObservableList returnedSubList=productDialogPaneController.getSubProducts(mainProduct.getProductID());
                int subQuantity;
                Iterator sublist =returnedSubList.iterator();
                while (sublist.hasNext()){
                    Product subProduct = (Product) sublist.next();

                    if(subProduct.getProductID()==selectedProduct.getProductID()){
                        subQuantity=subProduct.getQuantity()*mainProduct.getQuantity();
                    if(selectedProduct.getQuantity()>subQuantity){
                        selectedProduct.setQuantity(subQuantity);
                        if(selectedProduct.getQuantity()==0){
                            shoppingBasket.remove(selectedProduct);
                        }
                        tableShoppingBasket.refresh();
                  //      System.out.println("silinebilecek maksimum miktarda silindi ");
                    }else{
                 //       System.out.println("daha fazla sub ürün silinemez ");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText("All Available Products Deleted");
                        alert.setContentText("If you still have same product in shopping basket \nPlease make sure it is not sub product of any item in the shopping cart");

                        alert.showAndWait();


                    }

                    }
                }



            }else if(selectedProduct.getMainProductID()==0 && selectedProduct.getMainProduct()==0){ // seçilen ürün standart demektir

                shoppingBasket.remove(selectedProduct);

            }

        }else{//silinecek bir ürün seçilmedi

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Wrong Attempt");
            alert.setContentText("You didn't choose any item from shopping cart to delete");

            alert.showAndWait();
        }
}

//this method shows Products Dialog Pane below the edit menu item
@FXML
public void showProductDialogPane() throws IOException, SQLException {

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(bpAnaPencere.getScene().getWindow());
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/duzenMenu/ProductDialogPane.fxml"));
        Parent dialogContent =fxmlLoader.load();

     //   UrunlerDialogPaneController  urunlerDialogPaneController =fxmlLoader.<UrunlerDialogPaneController>getController();

    //    urunlerDialogPaneController.initialize();
        dialog.getDialogPane().setContent(dialogContent);


        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);


        //sonuc nesnesini gönderir
         Optional<ButtonType> sonuc =dialog.showAndWait();

         if(sonuc.get()==ButtonType.CLOSE) {
             getAllProductsTable();
         }
    }

    @FXML
    public void showCreateProductDialogPane() throws IOException {

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(bpAnaPencere.getScene().getWindow());
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/duzenMenu/CreateNewProduct.fxml"));
        Parent dialogContent =fxmlLoader.load();
        dialog.setTitle("Create a New Product");
        dialog.getDialogPane().setContent(dialogContent);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        //returns user's choice
        Optional<ButtonType> sonuc =dialog.showAndWait();

        if(sonuc.get()==ButtonType.CLOSE) {
            getAllProductsTable();
        }



    }

//here we show input dialog textfield when user add new product into shoppingbasket
@FXML
    public int getInputDialogPane() throws IOException {


        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.initOwner(bpAnaPencere.getScene().getWindow());
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

            tableShoppingBasket.refresh();
            return parseInt(quantityJFXTextField.getText());
        }
        // fxmlLoader.setLocation(getClass().getResource("/sample/duzenMenu/getInputDialogPane.fxml"));
        return 0;
    }

    @FXML
    public void contextChangeVat(){



       Product product= shoppingBasket.get(shoppingBasket.indexOf((Product)tableShoppingBasket.getSelectionModel().getSelectedItem()));

        try {
            product.setVat(showChangeVatDialogPane());
            tableShoppingBasket.refresh();
            calculateTotalPriceofShoppingBasket();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void paymentButton() throws IOException {
        Customer whoBuy =new Customer();

        if (comboBoxCustomer.getSelectionModel().getSelectedItem() == null) {
            whoBuy.setCustomerID(0);
        }else {
            whoBuy = (Customer) comboBoxCustomer.getSelectionModel().getSelectedItem();
        }

            // whoBuy.setCustomerID(14);
            if (!shoppingBasket.isEmpty()) {


                // Customer whoBuy=(Customer)combobox.selectedItem();
                // System.out.println("total PRICE === " + totalPrice);
                Sale sale = new Sale();

                //  Customer whoBuy=customerSelected();
                System.out.println(whoBuy.getCustomerID());
                Iterator<Product> productIterator = shoppingBasket.iterator();
                try {
                    currentDate = String.valueOf(LocalDate.now().format(dateFormatter));
                    currentTime = LocalTime.now().format(timeFormatter);
                    sale.setCustomerID(whoBuy.getCustomerID());
                    sale.setTotalPrice(totalPrice);
                    sale.setDate(currentDate);
                    float totalPrice = Float.parseFloat(labelTotalPrice.getText());

                    DataSource.getInstance().createNewSale(whoBuy.getCustomerID(), totalPrice, totalVat, currentDate, currentTime); // add date

                    while (productIterator.hasNext()) {
                        Product currentProduct = productIterator.next();

                        DetailofSale detailofSale = new DetailofSale();

                        int productID = currentProduct.getProductID();
                        detailofSale.setProductID(currentProduct.getProductID());
                        float instantPrice = currentProduct.getSellingPrice();
                        detailofSale.setInstantPrice(currentProduct.getSellingPrice());
                        int quantity = currentProduct.getQuantity();
                        detailofSale.setQuantity(currentProduct.getQuantity());
                        float subTotal = (currentProduct.getQuantity() * currentProduct.getSellingPrice());
                        detailofSale.setSubTotal(currentProduct.getQuantity() * currentProduct.getSellingPrice());
                        int saleID = DataSource.getInstance().returnLastAddedSaleID(currentDate, currentTime, whoBuy.getCustomerID());
                        detailofSale.setSaleID(sale.getSaleID());
                        float instantVat = currentProduct.getVat();

                        DataSource.getInstance().createDetailofSale(saleID, productID, instantPrice, instantVat, quantity, subTotal); // adding all products into detail_of_sale from shopping basket


                    }


                    if (buttonTogglePrintList.isDisableAnimation()) {

                    } else {
                        writeCheque(shoppingBasket);
                    }


                    shoppingBasket.clear();
                    tableShoppingBasket.getItems().clear();
                } catch (NullPointerException e) {
                    System.out.println();

                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Your shopping cart seems empty!");
                alert.setContentText("What would you like to buy ?");


                alert.showAndWait();
            }


    }
public void writeCheque(ObservableList shoppingList) throws IOException {


    File home = FileSystemView.getFileSystemView().getHomeDirectory();
        Customer selectedCustomer= onSelectCustomer();

        Stage window=new Stage();
  //  File home = FileSystemView.getFileSystemView().getHomeDirectory();


    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("JavaFX Projects");
   File defaultDirectory = new File("C:/");
    chooser.setInitialDirectory(defaultDirectory);
    File selectedDirectory = chooser.showDialog(window);

    System.out.println("selected dir="+selectedDirectory.toString());

    currentD= String.valueOf(LocalDate.now().format(dateF));
    currentT=LocalTime.now().format(timeF);


        String fileName=currentD+" "+currentT+".txt";


/*
        Stage window=new Stage();

    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("JavaFX Projects");
    File chooserInitialDirectory=chooser.getInitialDirectory();
    File home = FileSystemView.getFileSystemView().getHomeDirectory();
    File defaultDirectory = new File("E:/");
    chooser.setInitialDirectory(home);
    File selectedDirectory = chooser.showDialog(window);
*/

   // System.out.println("initialDir="+home.getAbsoluteFile());

   // System.out.println(fileName.toString());

    String customerInfo;

    if(selectedCustomer!=null){
         customerInfo=selectedCustomer.getName()+"-"+selectedCustomer.getSurname()+"-"+selectedCustomer.getTaxID()+currentD;
    }else{
         customerInfo="UnknownCustomer-"+currentD.toString();
    }


   // File file=new File(home.getAbsolutePath()+"\\"+customerInfo+".txt");

    File file=new File(selectedDirectory.getAbsolutePath()+"\\"+customerInfo+".txt");
    System.out.println("ADRESİNE FATURA TXT OLUŞTURULDU" + selectedDirectory.getAbsolutePath()+"\\"+customerInfo+".txt");


try {
    try (BufferedWriter cheque = new BufferedWriter(new FileWriter(file));) {

        Iterator iterator = shoppingList.iterator();

        cheque.write("CUSTOMER:");
        cheque.newLine();
        cheque.newLine();
        cheque.write("DATE:" + currentDate);
        cheque.newLine();
        cheque.newLine();
        cheque.write("HOUR:" + currentTime);
        cheque.newLine();
        cheque.newLine();
        cheque.write("BARCODE\t\tLABEL\t\tNAME\t\tQUANTITY\tUNIT PRICE\tUNIT VAT\tSUB TOTAL PRICE");

        cheque.newLine();
        cheque.newLine();
        while (iterator.hasNext()) {
            Product product = (Product) iterator.next();

            cheque.write(product.getBarcode() + "\t" + product.getLabel() + "\t" + product.getNameProduct() + "\t\t" + product.getQuantity() + "\t\t" + product.getSellingPrice() + "\t\t" + product.getVat() + "\t\t" + product.getSellingPrice() * product.getQuantity());
            cheque.newLine();

        }

        cheque.newLine();
        System.lineSeparator();
        cheque.newLine();

        cheque.write("TOTAL VAT:" + totalVat + System.lineSeparator() + "TOTAL PRICE:" + totalPrice);


    }
}catch (Exception e){
    e.printStackTrace();
    System.out.println("hata yakalandı");
}

}

@FXML
public void cancelShoppingButton(){

        tableShoppingBasket.getItems().clear();
}

    @FXML
    public Customer customerSelected(){

        if (comboBoxCustomer.getValue()!=null){
            Customer selectedCustomer = (Customer) comboBoxCustomer.getSelectionModel().getSelectedItem();
            System.out.println("seçilen müşteri  id " + selectedCustomer.getCustomerID() );
            return selectedCustomer;


        }else{
            System.out.println("ödeme yapıcak müşteri seçilmedi");
            return null;
        }


    }

    @FXML
    public float showChangeVatDialogPane() throws IOException {


        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.initOwner(bpAnaPencere.getScene().getWindow());
/*
        FXMLLoader fxmlLoader =new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/duzenMenu/getInputDialogPane.fxml"));
*/

        DialogPane dialogPane=new DialogPane();

        JFXTextField vatJFXTextField=new JFXTextField();
        vatJFXTextField.setPromptText("New VAT");
        vatJFXTextField.setLabelFloat(true);
        vatJFXTextField.setMaxHeight(200);
        vatJFXTextField.setMaxWidth(300);
        vatJFXTextField.setText("1");
        vatJFXTextField.setPadding(new Insets(10,0,0,0));

        Validator validator = new Validator();

        validator.enterOnlyFloat(vatJFXTextField);

        VBox vBox=new VBox();
        //    vBox.setSpacing(10);
        vBox.getChildren().addAll(vatJFXTextField);
        dialogPane.setContent(vBox);
        //    dialog.getDialogPane().setContent(fxmlLoader.load());
        dialog.getDialogPane().setContent(dialogPane);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        //sonuc nesnesini gönderir
        Optional<ButtonType> result =dialog.showAndWait();

        if(result.get()==ButtonType.OK){

            tableShoppingBasket.refresh();
            return parseFloat(vatJFXTextField.getText());
        }
        // fxmlLoader.setLocation(getClass().getResource("/sample/duzenMenu/getInputDialogPane.fxml"));
        return 0;
    }

    @FXML
    public void showtoGetFileNameDialogPane() throws IOException {
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(bpAnaPencere.getScene().getWindow());
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/FileName.fxml"));
        Parent dialogContent =fxmlLoader.load();
        dialog.setTitle("File Name");
        dialog.getDialogPane().setContent(dialogContent);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        //returns user's choice
        Optional<ButtonType> sonuc =dialog.showAndWait();

        if(sonuc.get()==ButtonType.APPLY) {

        }



    }

//kullanılmıyor
    @FXML
    public void showOptionsDialogPane() throws IOException {

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(bpAnaPencere.getScene().getWindow());
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/fileMenu/Options.fxml"));
        Parent dialogContent =fxmlLoader.load();
        dialog.setTitle("Options");
        dialog.getDialogPane().setContent(dialogContent);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        //returns user's choice
        Optional<ButtonType> sonuc =dialog.showAndWait();

        if(sonuc.get()==ButtonType.CLOSE) {

        }



    }

    @FXML
    public void getAllCustomersTable() {
        Task<ObservableList<Customer>> taskGetAllCustomer = new GetAllCustomers();
        tableCustomer.setItems(FXCollections.observableArrayList(DataSource.getInstance().getAllCustomers()));
        new Thread(taskGetAllCustomer).start();
        taskGetAllCustomer.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                tableCustomer.refresh();
            }
        });
    }

    class GetAllCustomers extends Task {

        @Override
        protected ObservableList<Customer> call() throws Exception {
            return FXCollections.observableArrayList(DataSource.getInstance().getAllCustomers());
        }
    }




}
