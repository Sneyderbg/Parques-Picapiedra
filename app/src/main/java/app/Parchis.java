package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Parchis extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println(getClass().getResource(""));
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));

        Scene scene = new Scene(root);
        
        primaryStage.setTitle("ok");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {

        Parchis.launch(args);

    }
    
}
