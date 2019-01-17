package sample.duzenMenu;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.shape.Path;
import sample.dataModel.DataSource;
import sample.model.Customer;
import sample.model.Product;

import javax.sound.midi.Soundbank;
import javax.xml.crypto.Data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.EventListener;
import java.util.Observable;
import java.util.Optional;
import java.util.concurrent.CompletionService;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

public class CustomerDialogPaneController {


    //we you this list when we are sorting and filtering CUSTOMERS for search textfield
    final ObservableList<Customer> data=FXCollections.observableArrayList(DataSource.getInstance().getAllCustomers());
    public SortedList<Customer> sortedList;
    public static Customer analysingCustomer=new Customer();

    public Customer getAnalysingCustomer() {
        return analysingCustomer;
    }

    public void setAnalysingCustomer(Customer analysingCustomer) {
        this.analysingCustomer = analysingCustomer;
    }

    boolean editing=false;  // to control button visibilty or disability. during the editing process delete and edit button set disable true in method itemSelected();

    @FXML
    private DialogPane dpCustomer;

    @FXML
    private JFXButton buttonCreateCustomer;


    @FXML
    private JFXButton buttonEditCustomer;


    @FXML
    private JFXButton buttonDeleteCustomer;

    @FXML
    private JFXButton buttonCancelCustomer;

    @FXML
    private JFXButton buttonUpdateCustomer;

    @FXML
    private TableView tableCustomer;


    @FXML
    private JFXTextField tfName;

    @FXML
    private JFXTextField tfSurname;

    @FXML
    private JFXTextField tfPhone;

    @FXML
    private JFXTextField tfTaxID;

    @FXML
    private JFXTextArea tfAddress;

    @FXML
    private JFXTextField tfEmail;

    @FXML
    private JFXTextField tfSearchCustomer;

    @FXML
    private JFXButton buttonAnalysis;



    @FXML
    public void initialize() {
//when the program starts if there is no selected item program will stop running.To handle with this problem we disable edit and delete buttons here.
        Validator validator = new Validator();

        tableCustomer.setItems(data);

        buttonCreateCustomer.setDisable(false);
        buttonEditCustomer.setDisable(true);
        buttonDeleteCustomer.setDisable(true);

        validator.JfxTextFieldisEmptyValidator(tfName);
        validator.JfxTextFieldisEmptyValidator(tfSurname);
        validator.JfxTextFieldisEmptyValidator(tfTaxID);



        validator.enterOnlyDigits(tfTaxID);
        validator.enterOnlyDigits(tfPhone);

        //SEARCH PRODUCT TEXTFIELD PROPERTY
        FilteredList<Customer> filteredList = new FilteredList<Customer>(data, e-> true);
        if (tfSearchCustomer != null || !tfSearchCustomer.getText().isEmpty()) {
            tfSearchCustomer.setOnKeyPressed(e -> {
                tfSearchCustomer.textProperty().addListener(((observable, oldValue, newValue) -> {
                    filteredList.setPredicate((Predicate<? super Customer>) customer -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();
                        String customerName=customer.getName().toLowerCase()+" "+customer.getSurname().toLowerCase();
                        if (customer.getName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (customer.getSurname().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        else if(customerName.toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                }));
                sortedList = new SortedList<Customer>(filteredList);
                sortedList.comparatorProperty().bind(tableCustomer.comparatorProperty());

                //  tableProduct.itemsProperty().bind(urunler);

                tableCustomer.setItems(sortedList);

            });
        }


    }


    //here firstly we check database constraints  and if it is ok then we send given information to CreateNewCustomer method and we create new customer.
    @FXML
    public void callCreateNewCustomerButton(){

        Validator validator=new Validator();
        boolean email=true;
        if(!tfEmail.getText().isEmpty()){
        if(validator.validateEmail(tfEmail.getText().trim())!=true) {
            email=false;
            }
        }

        if(email==true) {
            Customer customer = new Customer();

            customer.setName(tfName.getText());
            customer.setSurname(tfSurname.getText());
            customer.setPhone(tfPhone.getText());
            customer.setTaxID(tfTaxID.getText());
            customer.setAddress(tfAddress.getText());

            if ((customer.getName().isEmpty()) || (customer.getSurname().isEmpty() || (customer.getTaxID().isEmpty()))) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Missing or invalid registry!");
                alert.setContentText("Please fill required fields...");

                alert.showAndWait();

                tfName.requestFocus();
                tfSurname.requestFocus();
                tfPhone.requestFocus();
                tfAddress.requestFocus();
                tfTaxID.requestFocus();
                dpCustomer.requestFocus();


            } else {
                createNewCustomer();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid e-mail input");
            alert.setContentText("You are free to enter e-mail address");

            alert.showAndWait();
        }
    }


    public void createNewCustomer() {
        try {

            //here we assign this addNewCustomer method into a task.
            Task<Boolean> taskAddNewCustomer = new Task() {
                @Override
                protected Object call() throws Exception {
                    Customer customer =new Customer();

                    customer.setName(tfName.getText().toUpperCase());
                    customer.setSurname(tfSurname.getText().toUpperCase());
                    customer.setPhone(tfPhone.getText().trim());
                    customer.setTaxID(tfTaxID.getText().trim());
                    customer.setAddress(tfAddress.getText().trim());
                    customer.setEmail(tfEmail.getText().toLowerCase().trim());

                    return DataSource.getInstance().addNewCustomer(customer.getName(),customer.getSurname(),customer.getEmail(),customer.getPhone(),customer.getAddress(),customer.getTaxID());



                }
            };


            taskAddNewCustomer.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    tfName.clear();
                    tfSurname.clear();
                    tfEmail.clear();
                    tfPhone.clear();
                    tfAddress.clear();
                    tfTaxID.clear();

                    tableCustomer.refresh();

                 getAllCustomersTable();

                }
            });

            new Thread(taskAddNewCustomer).start();
        } catch (Exception e) {


            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("This Customer might be created before \n Phone & TaxID has to be unique ");
            alert.showAndWait();
        }
    }


    @FXML
    public void editCustomerButton() {
        editing=true;
        buttonCancelCustomer.setVisible(true);
        buttonUpdateCustomer.setVisible(true);
        buttonCreateCustomer.setDisable(true);
        buttonAnalysis.setDisable(true);

        buttonCancelCustomer.setDisable(false);
        buttonUpdateCustomer.setDisable(false);

        buttonDeleteCustomer.setDisable(true);
        buttonEditCustomer.setDisable(true);

        tableCustomer.setDisable(true);
        dpCustomer.requestFocus();

        Customer selectedCustomer = (Customer) tableCustomer.getSelectionModel().getSelectedItem();


        tfName.setText(selectedCustomer.getName().toUpperCase());
        tfSurname.setText(selectedCustomer.getSurname().toUpperCase());
        tfTaxID.setText(selectedCustomer.getTaxID());
        tfPhone.setText(selectedCustomer.getPhone());
        tfAddress.setText(selectedCustomer.getAddress());
        tfEmail.setText(selectedCustomer.getEmail());



    }

    @FXML
    public void deleteCustomer(){


        if(tableCustomer.getSelectionModel().getSelectedItem()!=null) {
            Customer selectedCustomer = (Customer) tableCustomer.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("DELETE");
            alert.setContentText("Are you sure to delete " + selectedCustomer.getName() + " " + selectedCustomer.getSurname() + " ?");

            Optional<ButtonType> result = alert.showAndWait();


            if (result.get() == ButtonType.OK) {
                DataSource.getInstance().deleteCustomer(selectedCustomer.getCustomerID());
               getAllCustomersTable();
                tableCustomer.refresh();

                buttonDeleteCustomer.setDisable(true);
                buttonEditCustomer.setDisable(true);
                buttonCreateCustomer.setDisable(false);

                buttonUpdateCustomer.setVisible(false);
                buttonCancelCustomer.setVisible(false);
                dpCustomer.requestFocus();


            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong attempt!");
            alert.setContentText("No item selected");
            alert.showAndWait();
        }

    }


    //this method return selected customer's ID from CustomerTableview /and its database table
    @FXML
    public int itemSelected(){

if(tableCustomer.getSelectionModel().getSelectedItem()!=null){
         Customer selectedCustomer = (Customer) tableCustomer.getSelectionModel().getSelectedItem();

         if(editing==true){
             buttonCreateCustomer.setDisable(true);
             buttonEditCustomer.setDisable(true);
             buttonDeleteCustomer.setDisable(true);
             buttonAnalysis.setDisable(true);


             buttonCancelCustomer.setDisable(true);
             buttonCancelCustomer.setVisible(false);


         }else {
             buttonAnalysis.setDisable(false);
             buttonDeleteCustomer.setDisable(false);
             buttonEditCustomer.setDisable(false);
             buttonCreateCustomer.setDisable(false);

         }

        int id=selectedCustomer.getCustomerID();
        return id;}
        else{
    return -1;
        }

    }


    @FXML
    public void cancelButton(){

        buttonCreateCustomer.setDisable(false);
        buttonEditCustomer.setDisable(true);
        buttonDeleteCustomer.setDisable(true);

        buttonAnalysis.setDisable(false);
        buttonCancelCustomer.setVisible(false);
        buttonUpdateCustomer.setVisible(false);

        tableCustomer.setDisable(false);


        tfName.clear();
        tfSurname.clear();
        tfPhone.clear();
        tfAddress.clear();
        tfTaxID.clear();
        tfEmail.clear();

        dpCustomer.requestFocus();

        editing=false;

    }
    //set all customers into tableview in dialogPane for customers

    @FXML
    public void updateButton(){


        Validator validator=new Validator();
        boolean email=true;
        if(!tfEmail.getText().isEmpty()){
            if(validator.validateEmail(tfEmail.getText().trim())!=true) {
                email=false;
            }
        }

        if(email==true){


        Customer selectedCustomer= (Customer) tableCustomer.getSelectionModel().getSelectedItem();
        if(!tfName.getText().isEmpty() && tfName.getText()!=null && !tfSurname.getText().isEmpty() && tfSurname.getText()!=null && !tfTaxID.getText().isEmpty() && tfTaxID.getText()!=null){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        // alert.setHeaderText("DELETE");
        alert.setContentText("Are you sure to update " + selectedCustomer.getName() +" " + selectedCustomer.getSurname()+"' information with new inputs?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            buttonAnalysis.setDisable(false);
            buttonCreateCustomer.setDisable(false);
            buttonEditCustomer.setDisable(true);
            buttonDeleteCustomer.setDisable(true);
            buttonCancelCustomer.setVisible(false);
            buttonUpdateCustomer.setVisible(false);

            tableCustomer.setDisable(false);

            Task<Boolean> taskUpdate = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    //  final Customer selectedCustomer= (Customer) tableCustomer.getSelectionModel().getSelectedItem();




                    return DataSource.getInstance().updateCustomer(itemSelected(), tfName.getText().trim().toUpperCase(), tfSurname.getText().trim().toUpperCase(), tfEmail.getText().trim().toLowerCase(),tfPhone.getText(), tfAddress.getText().toUpperCase(), tfTaxID.getText());
                }
            };

            taskUpdate.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    if (taskUpdate.valueProperty().get()) {
                        System.out.println("task update run ");
                        Customer newCustomer =new Customer();

                        selectedCustomer.setName(tfName.getText().toUpperCase().trim());
                        selectedCustomer.setSurname(tfSurname.getText().toUpperCase().trim());
                        selectedCustomer.setPhone(tfPhone.getText());
                        selectedCustomer.setTaxID(tfTaxID.getText());
                        selectedCustomer.setAddress(tfAddress.getText().toUpperCase().trim());
                        selectedCustomer.setEmail(tfEmail.getText().toLowerCase().trim());

                        getAllCustomersTable();

                        tableCustomer.refresh();


                        tfName.clear();
                        tfSurname.clear();
                        tfEmail.clear();
                        tfPhone.clear();
                        tfAddress.clear();
                        tfTaxID.clear();
                        tfEmail.clear();

                        editing=false;
                        dpCustomer.requestFocus();

                        buttonCreateCustomer.setDisable(false);
                    }
                }
            });

            new Thread(taskUpdate).start();
        }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Validation Error");
            alert.setContentText("You need to fill all specified fields");

            alert.showAndWait();
        }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid e-mail input");
            alert.setContentText("You are free to enter e-mail address");

            alert.showAndWait();
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



    public void showCustomerAnalysis() throws IOException, SQLException {

        if(tableCustomer.getSelectionModel().getSelectedItem()!=null) {

            setAnalysingCustomer((Customer) tableCustomer.getSelectionModel().getSelectedItem());


            // System.out.println("customerDialogPaneController="+analysingCustomer.getCustomerID());

            Dialog<ButtonType> dialog = new Dialog<ButtonType>();
            dialog.initOwner(dpCustomer.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/duzenMenu/CustomerAnalysis.fxml"));
            Parent dialogContent = fxmlLoader.load();
            //   UrunlerDialogPaneController  urunlerDialogPaneController =fxmlLoader.<UrunlerDialogPaneController>getController();

            //    urunlerDialogPaneController.initialize();
            dialog.getDialogPane().setContent(dialogContent);


            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);


            //sonuc nesnesini gönderir
            Optional<ButtonType> sonuc = dialog.showAndWait();

            if (sonuc.get() == ButtonType.CLOSE) {

            }


        }
    }

class GetAllCustomers extends Task {

    @Override
    protected ObservableList<Customer> call() throws Exception {
        return FXCollections.observableArrayList(DataSource.getInstance().getAllCustomers());
    }
}
}