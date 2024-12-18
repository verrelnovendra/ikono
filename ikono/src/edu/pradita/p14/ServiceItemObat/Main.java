package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application {

    private TableView<MasterModel> tableView;
    private ObservableList<MasterModel> data;
    private FilteredList<MasterModel> filteredData;

    // Database connection details
    private final String DB_URL = "jdbc:mysql://localhost:3306/SERVICE_ITEM_OBAT"; // Replace with your DB URL
    private final String DB_USER = "root"; // Replace with your DB username
    private final String DB_PASSWORD = "karv2020"; // Replace with your DB password

    @Override
    public void start(Stage primaryStage) {
        // Layout Utama
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Tabel Data
        tableView = new TableView<>();
        data = FXCollections.observableArrayList();
        filteredData = new FilteredList<>(data, p -> true); // FilteredList untuk fitur Search

        TableColumn<MasterModel, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());

        TableColumn<MasterModel, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<MasterModel, String> colQuantity = new TableColumn<>("Quantity");
        colQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());

        tableView.getColumns().addAll(colId, colName, colQuantity);
        tableView.setItems(filteredData); // Gunakan FilteredList untuk tabel

        // Form Input
        HBox form = new HBox(10);

        TextField txtId = new TextField();
        txtId.setPromptText("Enter ID");

        TextField txtName = new TextField();
        txtName.setPromptText("Enter Name");

        TextField txtQuantity = new TextField();
        txtQuantity.setPromptText("Enter Quantity");

        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> {
            String id = txtId.getText();
            String name = txtName.getText();
            String quantity = txtQuantity.getText();

            if (!id.isEmpty() && !name.isEmpty() && !quantity.isEmpty()) {
                // Add to list
                data.add(new MasterModel(id, name, quantity));

                // Add to database
                insertDataToDatabase(id, name, quantity);

                txtId.clear();
                txtName.clear();
                txtQuantity.clear();
            }
        });

        Button btnEdit = new Button("Edit");
        btnEdit.setOnAction(e -> {
            MasterModel selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String id = txtId.getText();
                String name = txtName.getText();
                String quantity = txtQuantity.getText();

                // Update in database
                updateDataInDatabase(selectedItem.getId(), id, name, quantity);

                // Update in table
                selectedItem.setId(id);
                selectedItem.setName(name);
                selectedItem.setQuantity(quantity);
                tableView.refresh();

                // Clear form fields
                txtId.clear();
                txtName.clear();
                txtQuantity.clear();
            }
        });

        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> {
            MasterModel selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Delete from database
                deleteDataFromDatabase(selectedItem.getId());

                // Remove from table
                data.remove(selectedItem);

                // Clear form fields
                txtId.clear();
                txtName.clear();
                txtQuantity.clear();
            }
        });

        form.getChildren().addAll(new Label("ID:"), txtId, new Label("Name:"), txtName, new Label("Quantity:"), txtQuantity, btnAdd, btnEdit, btnDelete);

        // Add components to layout
        root.getChildren().addAll(new Label("Master Data - Service/Item/Obat"), form, tableView);

        // Scene and Stage setup
        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle("Master Data");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to insert data into the database
    private void insertDataToDatabase(String id, String name, String quantity) {
        String query = "INSERT INTO master_data (id, name, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, quantity);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error inserting data into database: " + e.getMessage());
        }
    }

    // Method to update data in the database
    private void updateDataInDatabase(String oldId, String id, String name, String quantity) {
        String query = "UPDATE master_data SET id = ?, name = ?, quantity = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, quantity);
            stmt.setString(4, oldId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error updating data in database: " + e.getMessage());
        }
    }

    // Method to delete data from the database
    private void deleteDataFromDatabase(String id) {
        String query = "DELETE FROM master_data WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error deleting data from database: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
