package sample.duzenMenu;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import sample.dataModel.DataSource;
import sample.model.Customer;
import sample.model.Product;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import static javax.xml.bind.DatatypeConverter.parseInt;
import static sun.misc.FloatingDecimal.parseFloat;


public class CreateNewProductController {


    public void setSubPID(int subPID) {
        this.subPID = subPID;
    }

    int subPID;


    @FXML
    private DialogPane dpCreateProduct;

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

    @FXML JFXTextField tfBarcode;

    @FXML
    private JFXTextField tfVAT;

    @FXML
    private JFXButton buttonSave;

    @FXML
    private JFXButton buttonClear;


float newVAT=0;
    @FXML
    public void initialize(){

        Validator validator = new Validator();

        validator.JfxTextFieldisEmptyValidator(tfLabel);
        validator.JfxTextFieldisEmptyValidator(tfNameProduct);
        validator.JfxTextFieldisEmptyValidator(tfStock);
        validator.JfxTextFieldisEmptyValidator(tfBuyingPrice);
        validator.JfxTextFieldisEmptyValidator(tfSellingPrice);
        validator.enterOnlyDigits(tfStock);
        validator.enterOnlyFloat(tfBuyingPrice);
        validator.enterOnlyFloat(tfSellingPrice);
        validator.enterOnlyFloat(tfVAT);


    }

    @FXML
    public void clearButton(){
        tfLabel.clear();
        tfNameProduct.clear();
        tfStock.clear();
        tfSellingPrice.clear();
        tfBuyingPrice.clear();
        tfVAT.clear();


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
                dpCreateProduct.requestFocus();
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
            dpCreateProduct.requestFocus();

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



                }
            });

            new Thread(taskAddNewProduct).start();

        }catch (NumberFormatException e){
            System.out.println("numberFormatException " + e);

        }

    }

}
