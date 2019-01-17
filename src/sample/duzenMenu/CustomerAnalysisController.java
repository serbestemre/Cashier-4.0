package sample.duzenMenu;

import analiysisModel.CustomerPieChart;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import sample.dataModel.DataSource;
import sample.model.Customer;
import sample.model.DetailofSale;
import sample.model.Product;
import sample.model.Sale;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;


public class CustomerAnalysisController {
    public int customerID;
    public int saleID;

    @FXML
    private Label labelSales;


    @FXML
    private TableView tableSales;

    @FXML
    private TableView tableDetails;
    @FXML
    private TableView tableProductDetails;

    @FXML
    private PieChart pieChart;


    @FXML
    private DialogPane dpCustomerAnalysis;

    @FXML
    private VBox vBoxPieChart;


    @FXML
    private TableColumn<ArrayList,Integer> columnNumber;

    @FXML
    public void initialize(){

        CustomerDialogPaneController aCs=new CustomerDialogPaneController();

       labelSales.setText("("+aCs.getAnalysingCustomer().getTaxID()+")"+aCs.getAnalysingCustomer().getName()+ " "+ aCs.getAnalysingCustomer().getSurname()+"'s SALES");


       customerID =aCs.getAnalysingCustomer().getCustomerID();

       getAllSalesOfAnalysingCustomer();


       //selecting first sale on table sales and getting details of first sale for tableDetails
       tableSales.getSelectionModel().selectFirst();
        Sale selectedItem= (Sale) tableSales.getSelectionModel().getSelectedItem();
        saleID=selectedItem.getSaleID();
       tableDetails.setItems(FXCollections.observableArrayList(DataSource.getInstance().getDeailsOfSalesForAnalysingCustomer(saleID)));

        tableDetails.getSelectionModel().selectFirst();
       DetailofSale detailofSale= (DetailofSale) tableDetails.getSelectionModel().getSelectedItem();
        Product searchedProduct=DataSource.getInstance().getSearchedProduct(detailofSale.getProductID());
        tableProductDetails.getItems().add(searchedProduct);





        ArrayList<CustomerPieChart>dbInfo= DataSource.getInstance().getPieDataofGivenCustomer(customerID);


        String firstProduct=" ";
        double firstPValue=0;
        String secondProduct=" ";
        double secondPValue=0;
        String thirdProduct=" ";
        double thirdPValue=0;
        String fourthProduct=" ";
        double fourthPValue=0;
        String fifthproduct=" ";
        double fifthPValue=0;
        String otherProducts="OTHER PRODUCTS";
        double otherPValue=0;

        Iterator iterator = dbInfo.iterator();
        int i =1;
        double totalValue=0;
        while (iterator.hasNext()){
            CustomerPieChart customerPieChart= (CustomerPieChart) iterator.next();
            System.out.println("i döngü başlangıç değeri="+i);
        if(i==1){
            firstProduct=customerPieChart.getBarcode()+" "+customerPieChart.getLabel()+" "+customerPieChart.getNameProduct();
            firstPValue=customerPieChart.getTotalSubPrice();
        }else if(i==2){
            secondProduct=customerPieChart.getBarcode()+" "+customerPieChart.getLabel()+" "+customerPieChart.getNameProduct();
            secondPValue=customerPieChart.getTotalSubPrice();
        }else if(i==3){
            thirdProduct=customerPieChart.getBarcode()+" "+customerPieChart.getLabel()+" "+customerPieChart.getNameProduct();
            thirdPValue=customerPieChart.getTotalSubPrice();
        }else if(i==4){
            fourthProduct=customerPieChart.getBarcode()+" "+customerPieChart.getLabel()+" "+customerPieChart.getNameProduct();
            fourthPValue=customerPieChart.getTotalSubPrice();
        }else if(i==5){
            fifthproduct=customerPieChart.getBarcode()+" "+customerPieChart.getLabel()+" "+customerPieChart.getNameProduct();
            fifthPValue=customerPieChart.getTotalSubPrice();
        }else{ // others
            otherPValue+=customerPieChart.getTotalSubPrice();
        }
            totalValue+=customerPieChart.getQuantity();
            System.out.println("i döngü bitiş değeri="+i);
        i++;
            System.out.println("i ++ değeri="+i);


        }


        ObservableList<PieChart.Data>pieChartData= FXCollections.observableArrayList(
                new PieChart.Data(firstProduct,firstPValue),
                new PieChart.Data(secondProduct,secondPValue),
                new PieChart.Data(thirdProduct,thirdPValue),
                new PieChart.Data(fourthProduct,fourthPValue),
                new PieChart.Data(fifthproduct,fifthPValue),
                new PieChart.Data(otherProducts,otherPValue));

        pieChart.setData(pieChartData);


        final Label caption = new Label("");
        caption.setTextFill(Color.BLACK);
        caption.setStyle("-fx-font: 14 arial;");


        DoubleBinding total = Bindings.createDoubleBinding(() ->
                pieChartData.stream().collect(Collectors.summingDouble(PieChart.Data::getPieValue)), pieChartData);



        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    e -> {
                        caption.setTranslateX(e.getSceneX());
                        caption.setTranslateY(e.getSceneY());
                        String text = String.format("%.1f%%", 100*data.getPieValue()/total.get()) ;
                        caption.setText(text);
                    }
            );
        }

        dpCustomerAnalysis.getChildren().add(caption);
    }


@FXML
public void getDetailsOfProduct(){
        tableProductDetails.getItems().clear();

        ObservableList productList=FXCollections.observableArrayList();
if(tableDetails.getSelectionModel().getSelectedItem()!=null) {
    DetailofSale detailofSale = (DetailofSale) tableDetails.getSelectionModel().getSelectedItem();
    int productID= detailofSale.getProductID();
    Product searchedProduct=DataSource.getInstance().getSearchedProduct(productID);



    productList.add(searchedProduct);

    System.out.println("searched pId"+searchedProduct.getProductID());
    tableProductDetails.getItems().add(searchedProduct);
}else{
    tableProductDetails.getItems().clear();
}
   // tableProductDetails.setItems(productList);

}


    @FXML
    public void getAllSalesOfAnalysingCustomer() {
        Task<ObservableList<Customer>> taskGetAllCustomer = new GetAllSales();
        tableSales.setItems(FXCollections.observableArrayList(DataSource.getInstance().getSalesOfAnalysingCustomer(customerID)));
        new Thread(taskGetAllCustomer).start();
        taskGetAllCustomer.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                tableSales.refresh();
            }
        });
    }

    class GetAllSales extends Task {

        @Override
        protected ObservableList<Customer> call() throws Exception {
            return FXCollections.observableArrayList(DataSource.getInstance().getAllCustomers());
        }
    }


    @FXML
    public void getAllDetailsOfSaleForAnalysingCustomer() {
        Task<ObservableList<DetailofSale>> taskGetAllDetails = new GetAllDetails();
        if(tableSales.getSelectionModel().getSelectedItem()!=null){
        Sale selectedItem= (Sale) tableSales.getSelectionModel().getSelectedItem();
            saleID=selectedItem.getSaleID();
        }else
            {
            Sale firstSale= (Sale) tableSales.getItems().get(0);
                saleID=firstSale.getSaleID();
            }

        tableDetails.setItems(FXCollections.observableArrayList(DataSource.getInstance().getDeailsOfSalesForAnalysingCustomer(saleID)));
        new Thread(taskGetAllDetails).start();
        taskGetAllDetails.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                tableDetails.refresh();
            }
        });
    }

    class GetAllDetails extends Task {

        @Override
        protected ObservableList<DetailofSale> call() throws Exception {
            Sale selectedItem= (Sale) tableSales.getSelectionModel().getSelectedItem();
            int saleID=selectedItem.getSaleID();
            return FXCollections.observableArrayList(DataSource.getInstance().getDeailsOfSalesForAnalysingCustomer(saleID));
        }
    }







}
