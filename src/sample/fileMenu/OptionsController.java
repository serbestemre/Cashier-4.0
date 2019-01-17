package sample.fileMenu;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sample.dataModel.DataSource;

import javax.swing.filechooser.FileSystemView;
import javax.xml.crypto.Data;
import java.io.File;
import java.util.function.Predicate;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.model.Customer;

public class OptionsController {



    @FXML
    private Button buttonChangeFolderDest;

    @FXML
    private TextField tfChequeFileDest;

    @FXML
    private ComboBox comboBox;

    @FXML
    private JFXTextField tfSearchCustomer;

    final ObservableList<Customer> data=FXCollections.observableArrayList(DataSource.getInstance().getAllCustomers());
    public SortedList<Customer> sortedList;
    private FilteredList<Customer> filteredList;

    private Predicate<Customer> allCustomers;
    private Predicate<Customer> searchedCustomers;

    @FXML
    public void initialize(){

//        combobox.setItems(customers);



allCustomers=new Predicate<Customer>() {
    @Override
    public boolean test(Customer customer) {
        return true;
    }
};



if(tfSearchCustomer!=null && !tfSearchCustomer.getText().isEmpty()){

    tfSearchCustomer.textProperty().addListener((observable, oldValue, newValue) -> {


searchedCustomers=new Predicate<Customer>() {
    @Override
    public boolean test(Customer customer) {
        return customer.getTaxID().contains(newValue);
    }
};

    });

comboBox.setItems(comboBox.getItems().filtered(searchedCustomers));

    }else{

    //tüm listeyi set

    comboBox.getItems().filtered(allCustomers);
}

    }

    @FXML
    public void ChangeFileDestButton(){

    }


    public void getAllCustomersTable() {
        Task<ObservableList<Customer>> taskGetAllCustomer = new GetAllCustomers();
        comboBox.setItems(FXCollections.observableArrayList(DataSource.getInstance().getAllCustomers()));
        new Thread(taskGetAllCustomer).start();
        taskGetAllCustomer.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("çağırıldı");
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



