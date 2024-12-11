package edu.pradita.p14.connection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DatabaseConnection.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Database Connection Settings");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
