package Main; // Adjust the package name to match your project structure

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file from the resources folder
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/rack_view.fxml"));

        // Create a scene with the loaded layout
        Scene scene = new Scene(loader.load(), 600, 400);

        // Set the scene on the primary stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rack Management System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
