package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SalesReturns extends Application {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/database";
    private static final String USER = "your_username";
    private static final String PASS = "your_password";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Form Retur Penjualan");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        Label rmaLabel = new Label("RMA Number:");
        TextField rmaField = new TextField();
        grid.add(rmaLabel, 0, 0);
        grid.add(rmaField, 1, 0);

        Label receiptLabel = new Label("Receipt Number:");
        TextField receiptField = new TextField();
        grid.add(receiptLabel, 0, 1);
        grid.add(receiptField, 1, 1);

        Label customerLabel = new Label("Customer Name:");
        TextField customerField = new TextField();
        grid.add(customerLabel, 0, 2);
        grid.add(customerField, 1, 2);

        Label itemIdLabel = new Label("Item ID:");
        TextField itemIdField = new TextField();
        grid.add(itemIdLabel, 0, 3);
        grid.add(itemIdField, 1, 3);

        Label descriptionLabel = new Label("Description:");
        TextField descriptionField = new TextField();
        grid.add(descriptionLabel, 0, 4);
        grid.add(descriptionField, 1, 4);

        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();
        grid.add(quantityLabel, 0, 5);
        grid.add(quantityField, 1, 5);

        Label reasonLabel = new Label("Return Reason:");
        ComboBox<String> reasonComboBox = new ComboBox<>();
        reasonComboBox.getItems().addAll("Defective", "Wrong Item", "Not as Described", "Other");
        grid.add(reasonLabel, 0, 6);
        grid.add(reasonComboBox, 1, 6);

        Label refundLabel = new Label("Refund Method:");
        ComboBox<String> refundComboBox = new ComboBox<>();
        refundComboBox.getItems().addAll("Original Payment Method", "Store Credit");
        grid.add(refundLabel, 0, 7);
        grid.add(refundComboBox, 1, 7);

        Label commentsLabel = new Label("Comments:");
        TextArea commentsArea = new TextArea();
        grid.add(commentsLabel, 0, 8);
        grid.add(commentsArea, 1, 8, 1, 3);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> handleSubmit(rmaField, receiptField, customerField, itemIdField,
                descriptionField, quantityField, reasonComboBox, refundComboBox, commentsArea));
        grid.add(submitButton, 1, 11);

        Scene scene = new Scene(grid, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleSubmit(TextField rmaField, TextField receiptField, TextField customerField,
                              TextField itemIdField, TextField descriptionField, TextField quantityField,
                              ComboBox<String> reasonComboBox, ComboBox<String> refundComboBox,
                              TextArea commentsArea) {

        if (rmaField.getText().isEmpty() || receiptField.getText().isEmpty() ||
                customerField.getText().isEmpty() || itemIdField.getText().isEmpty() ||
                descriptionField.getText().isEmpty() || quantityField.getText().isEmpty() ||
                reasonComboBox.getValue() == null || refundComboBox.getValue() == null) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO returns_sales (rma_number, receipt_number, customer_name, item_id, " +
                    "description, quantity, return_reason, refund_method, comments) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, rmaField.getText());
            preparedStatement.setString(2, receiptField.getText());
            preparedStatement.setString(3, customerField.getText());
            preparedStatement.setString(4, itemIdField.getText());
            preparedStatement.setString(5, descriptionField.getText());
            preparedStatement.setInt(6, Integer.parseInt(quantityField.getText()));
            preparedStatement.setString(7, reasonComboBox.getValue());
            preparedStatement.setString(8, refundComboBox.getValue());
            preparedStatement.setString(9, commentsArea.getText());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Return record saved successfully!");
                clearFields(rmaField, receiptField, customerField, itemIdField,
                        descriptionField, quantityField, reasonComboBox, refundComboBox, commentsArea);
            } else {
                showAlert("Error", "Failed to save return record.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields(TextField rmaField, TextField receiptField, TextField customerField,
                             TextField itemIdField, TextField descriptionField, TextField quantityField,
                             ComboBox<String> reasonComboBox, ComboBox<String> refundComboBox,
                             TextArea commentsArea) {
        rmaField.clear();
        receiptField.clear();
        customerField.clear();
        itemIdField.clear();
        descriptionField.clear();
        quantityField.clear();
        reasonComboBox.getSelectionModel().clearSelection();
        refundComboBox.getSelectionModel().clearSelection();
        commentsArea.clear();
    }
}
