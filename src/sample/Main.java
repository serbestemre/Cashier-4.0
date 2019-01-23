package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sample.dataModel.DataSource;

import sample.model.Product;

import javax.xml.crypto.Data;
import java.io.File;
import java.sql.Connection;

public class Main extends Application {
    @Override
    public void init() throws Exception {
        super.init();
        if(!DataSource.getInstance().openDatabase()){

            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DataSource.getInstance().closeDatabase();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{



      FXMLLoader loader =new FXMLLoader(getClass().getResource("anaPencere.fxml"));
      Parent root= loader.load();




      primaryStage.setTitle("Cashier 4.0");
        Image image = new Image("/sample/images/icon.png");

        primaryStage.getIcons().add(image);
        primaryStage.setScene(new Scene(root, 1000 , 600));
        primaryStage.show();





    }


    public static void main(String[] args) {
        launch(args);



    }




public File checkDestination(){
    File path=DataSource.getInstance().checkFileDestination();
    return path;

}

}