package edu.pradita.p14.asuransi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Asuransi.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Asuransi");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
