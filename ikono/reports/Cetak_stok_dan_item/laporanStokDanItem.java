package edu.pradita.p14.LaporanDaftarItemdanStok;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application {

    private ObservableList<Product> data = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Product Report");

        // Label untuk laporan
        Label reportLabel = new Label("Laporan Stok dan Item:");

        // TableView
        TableView<Product> tableView = new TableView<>();

        // Kolom-kolom
        TableColumn<Product, Integer> colProductId = new TableColumn<>("Product ID");
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));

        TableColumn<Product, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> colDescription = new TableColumn<>("Description");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Product, Double> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> colSupplierId = new TableColumn<>("Supplier ID");
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));

        TableColumn<Product, String> colCreatedAt = new TableColumn<>("Created At");
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        tableView.getColumns().addAll(colProductId, colName, colDescription, colPrice, colSupplierId, colCreatedAt);

        // Ambil data dari database
        loadDataFromDatabase();

        // FilteredList untuk filtering data
        FilteredList<Product> filteredData = new FilteredList<>(data, p -> true);

        // ComboBox untuk sorting
        Label sortLabel = new Label("Sort by:");
        ComboBox<String> sortOrder = new ComboBox<>();
        sortOrder.getItems().addAll("Product ID", "Name", "Price");
        sortOrder.setValue("Product ID");

        // Button untuk Update
        Button updateButton = new Button("Update");

        updateButton.setOnAction(e -> {
            String selectedSort = sortOrder.getValue();

            // Sorting berdasarkan pilihan
            SortedList<Product> sortedData = new SortedList<>(filteredData);
            sortedData.setComparator((product1, product2) -> {
                switch (selectedSort) {
                    case "Product ID":
                        return Integer.compare(product1.getProductId(), product2.getProductId());
                    case "Name":
                        return product1.getName().compareTo(product2.getName());
                    case "Price":
                        return Double.compare(product1.getPrice(), product2.getPrice());
                    default:
                        return 0;
                }
            });

            tableView.setItems(sortedData);
        });

        HBox filters = new HBox(10, sortLabel, sortOrder, updateButton);
        filters.setPadding(new Insets(10));

        // Set data ke tabel
        tableView.setItems(filteredData);

        // Layout
        VBox vbox = new VBox(10, reportLabel, filters, tableView);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Metode untuk mengambil data dari database
    private void loadDataFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/warehousedb";
        String user = "root";
        String password = "12345";

        String query = "SELECT * FROM products";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                data.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("supplier_id"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Koneksi database gagal: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Class Product
    public static class Product {
        private int productId;
        private String name;
        private String description;
        private double price;
        private int supplierId;
        private String createdAt;

        public Product(int productId, String name, String description, double price, int supplierId, String createdAt) {
            this.productId = productId;
            this.name = name;
            this.description = description;
            this.price = price;
            this.supplierId = supplierId;
            this.createdAt = createdAt;
        }

        public int getProductId() { return productId; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getPrice() { return price; }
        public int getSupplierId() { return supplierId; }
        public String getCreatedAt() { return createdAt; }
    }
}
