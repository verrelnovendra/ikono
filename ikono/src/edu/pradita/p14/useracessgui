import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class UserAccessApp extends Application {

    private Map<String, String> userAccessMap = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User  Access Management");

        // Create UI elements
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        
        Label accessLabel = new Label("Access Level:");
        ComboBox<String> accessComboBox = new ComboBox<>();
        accessComboBox.getItems().addAll("Admin", "User ", "Guest");

        Button addButton = new Button("Add User");
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        // Set up the layout
        GridPane grid = new GridPane();
        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(accessLabel, 0, 1);
        grid.add(accessComboBox, 1, 1);
        grid.add(addButton, 1, 2);
        grid.add(outputArea, 0, 3, 2, 1);

        // Add button action
        addButton.setOnAction(e -> {
            String username = userField.getText();
            String accessLevel = accessComboBox.getValue();

            if (username.isEmpty() || accessLevel == null) {
                outputArea.setText("Please enter a username and select an access level.");
            } else {
                userAccessMap.put(username, accessLevel);
                outputArea.setText("User  added: " + username + " with access level: " + accessLevel);
                userField.clear();
                accessComboBox.getSelectionModel().clearSelection();
            }
        });

        // Set up the scene
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
