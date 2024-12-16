package edu.pradita.p14.returpembelian;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("returpembelian.fxml"));
        Parent root = loader.load();

        // Set the title of the stage
        primaryStage.setTitle("Form Retur Pembelian");

        // Create a scene and set it on the stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
