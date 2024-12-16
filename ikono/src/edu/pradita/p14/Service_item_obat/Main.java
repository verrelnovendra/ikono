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

public class Main extends Application {

    private TableView<MasterModel> tableView;
    private ObservableList<MasterModel> data;
    private FilteredList<MasterModel> filteredData;

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

        TableColumn<MasterModel, String> colCategory = new TableColumn<>("Category");
        colCategory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        TableColumn<MasterModel, String> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

        TableColumn<MasterModel, String> colQuantity = new TableColumn<>("Quantity");
        colQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());

        tableView.getColumns().addAll(colId, colName, colCategory, colPrice, colQuantity);
        tableView.setItems(filteredData); // Gunakan FilteredList untuk tabel

        // Form Input
        HBox form = new HBox(10);

        TextField txtId = new TextField();
        txtId.setPromptText("Enter ID");

        TextField txtName = new TextField();
        txtName.setPromptText("Enter Name");

        TextField txtCategory = new TextField();
        txtCategory.setPromptText("Enter Category");

        TextField txtPrice = new TextField();
        txtPrice.setPromptText("Enter Price");

        TextField txtQuantity = new TextField();
        txtQuantity.setPromptText("Enter Quantity");

        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> {
            String id = txtId.getText();
            String name = txtName.getText();
            String category = txtCategory.getText();
            String price = txtPrice.getText();
            String quantity = txtQuantity.getText();

            if (!id.isEmpty() && !name.isEmpty() && !category.isEmpty() && !price.isEmpty() && !quantity.isEmpty()) {
                data.add(new MasterModel(id, name, category, price, quantity));
                txtId.clear();
                txtName.clear();
                txtCategory.clear();
                txtPrice.clear();
                txtQuantity.clear();
            }
        });

        Button btnEdit = new Button("Edit");
        btnEdit.setOnAction(e -> {
            MasterModel selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                selectedItem.setId(txtId.getText());
                selectedItem.setName(txtName.getText());
                selectedItem.setCategory(txtCategory.getText());
                selectedItem.setPrice(txtPrice.getText());
                selectedItem.setQuantity(txtQuantity.getText());
                tableView.refresh(); // Refresh table to reflect changes
                txtId.clear();
                txtName.clear();
                txtCategory.clear();
                txtPrice.clear();
                txtQuantity.clear();
            } else {
                showAlert("No Selection", "Please select an item to edit.");
            }
        });

        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> {
            MasterModel selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                data.remove(selectedItem);
                txtId.clear();
                txtName.clear();
                txtCategory.clear();
                txtPrice.clear();
                txtQuantity.clear();
            } else {
                showAlert("No Selection", "Please select an item to delete.");
            }
        });

        form.getChildren().addAll(new Label("ID:"), txtId, new Label("Name:"), txtName, new Label("Category:"), txtCategory,
                new Label("Price:"), txtPrice, new Label("Quantity:"), txtQuantity, btnAdd, btnEdit, btnDelete);

        // Fitur Search dengan Filter Kolom
        HBox searchBox = new HBox(10);

        ComboBox<String> cmbColumn = new ComboBox<>();
        cmbColumn.setItems(FXCollections.observableArrayList("All", "ID", "Name", "Category", "Price", "Quantity"));
        cmbColumn.setValue("All"); // Default value

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Search...");

        Button btnReset = new Button("Reset");
        btnReset.setOnAction(e -> {
            txtSearch.clear(); // Kosongkan kotak pencarian
            cmbColumn.setValue("All"); // Reset ComboBox ke "All"
            filteredData.setPredicate(null); // Tampilkan semua data
        });

        txtSearch.setOnAction(e -> {
            String searchValue = txtSearch.getText();
            if (searchValue == null || searchValue.isEmpty()) {
                return;
            }

            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirm Search");
            confirmationDialog.setHeaderText(null);
            confirmationDialog.setContentText("Apply filter for search: \"" + searchValue + "\"?");
            confirmationDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    applySearchFilter(txtSearch.getText(), cmbColumn.getValue());
                } else {
                    txtSearch.clear(); // Kosongkan kotak pencarian jika dibatalkan
                }
            });
        });

        searchBox.getChildren().addAll(new Label("Filter by:"), cmbColumn, txtSearch, btnReset);

        // Tambahkan Listener untuk Pemilihan Baris di Tabel
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtId.setText(newValue.getId());
                txtName.setText(newValue.getName());
                txtCategory.setText(newValue.getCategory());
                txtPrice.setText(newValue.getPrice());
                txtQuantity.setText(newValue.getQuantity());
            }
        });

        // Tambahkan Komponen ke Layout Utama
        root.getChildren().addAll(new Label("Master Data - Service/Item/Obat"), searchBox, tableView, form);

        // Scene dan Stage
        Scene scene = new Scene(root, 1000, 500);
        primaryStage.setTitle("Master Data");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void applySearchFilter(String searchValue, String selectedColumn) {
        filteredData.setPredicate(item -> {
            String lowerCaseFilter = searchValue.toLowerCase();

            switch (selectedColumn) {
                case "ID":
                    return item.getId().toLowerCase().contains(lowerCaseFilter);
                case "Name":
                    return item.getName().toLowerCase().contains(lowerCaseFilter);
                case "Category":
                    return item.getCategory().toLowerCase().contains(lowerCaseFilter);
                case "Price":
                    return item.getPrice().toLowerCase().contains(lowerCaseFilter);
                case "Quantity":
                    return item.getQuantity().toLowerCase().contains(lowerCaseFilter);
                default:
                    return item.getId().toLowerCase().contains(lowerCaseFilter) ||
                            item.getName().toLowerCase().contains(lowerCaseFilter) ||
                            item.getCategory().toLowerCase().contains(lowerCaseFilter) ||
                            item.getPrice().toLowerCase().contains(lowerCaseFilter) ||
                            item.getQuantity().toLowerCase().contains(lowerCaseFilter);
            }
        });
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
