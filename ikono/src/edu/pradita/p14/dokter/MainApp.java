package dokter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        DoctorController controller = new DoctorController();

        Scene scene = new Scene(controller.getView(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Master Data");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
