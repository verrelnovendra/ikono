package GUI;

import controllers.RackSystemController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Rack;

import java.util.List;

public class RackGUI extends Application {
    private RackSystemController controller;
    private TableView<Rack> rackTable;
    private ObservableList<Rack> rackList;

    @Override
    public void start(Stage primaryStage) {
        controller = new RackSystemController();
        primaryStage.setTitle("Rack Management System - JavaFX");

        // Create a BorderPane layout
        BorderPane root = new BorderPane();

        // Table for displaying racks
        rackTable = new TableView<>();
        TableColumn<Rack, Integer> idColumn = new TableColumn<>("Rack ID");
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getRackId()));

        TableColumn<Rack, String> nameColumn = new TableColumn<>("Rack Name");
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRackName()));

        TableColumn<Rack, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocation()));

        rackTable.getColumns().addAll(idColumn, nameColumn, locationColumn);

        loadRacks(); // Load data into table

        root.setCenter(rackTable);

        // Input fields for adding racks
        HBox inputPanel = new HBox(10);
        inputPanel.setPadding(new Insets(10));
        TextField nameField = new TextField();
        nameField.setPromptText("Rack Name");
        TextField locationField = new TextField();
        locationField.setPromptText("Location");
        Button addButton = new Button("Add Rack");

        inputPanel.getChildren().addAll(new Label("Name:"), nameField, new Label("Location:"), locationField, addButton);
        root.setBottom(inputPanel);

        // Add rack action
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String location = locationField.getText();

            if (controller.addRack(name, location)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Rack added successfully!");
                loadRacks(); // Refresh table
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add rack.");
            }
        });

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadRacks() {
        List<Rack> racks = controller.getAllRacks();
        rackList = FXCollections.observableArrayList(racks);
        rackTable.setItems(rackList);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
