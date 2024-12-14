package inventoryPerWarehouse;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InventoryApp extends Application {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/pos_inventory";
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "khabuki1?"; // Replace with your MySQL password

    private Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        connectToDatabase();

        Label lblProductId = new Label("Product ID:");
        TextField txtProductId = new TextField();
        Label lblWarehouseId = new Label("Warehouse ID:");
        TextField txtWarehouseId = new TextField();
        Label lblQuantity = new Label("Quantity:");
        TextField txtQuantity = new TextField();
        Button btnAddInventory = new Button("Add Inventory");
        Button btnShowInventory = new Button("Show Inventory");
        Label lblInventoryId = new Label("Inventory ID:");
        TextField txtInventoryId = new TextField();
        Button btnDeleteInventory = new Button("Delete Inventory");
        TextArea txtOutput = new TextArea();
        txtOutput.setEditable(false);

        btnAddInventory.setOnAction(e -> {
            try {
                int productId = Integer.parseInt(txtProductId.getText());
                int warehouseId = Integer.parseInt(txtWarehouseId.getText());
                int quantity = Integer.parseInt(txtQuantity.getText());
                addInventory(productId, warehouseId, quantity);
                txtOutput.setText("Inventory added successfully.");
            } catch (Exception ex) {
                txtOutput.setText("Error: " + ex.getMessage());
            }
        });

        btnShowInventory.setOnAction(e -> {
            try {
                String inventory = getAllInventory();
                txtOutput.setText(inventory);
            } catch (Exception ex) {
                txtOutput.setText("Error: " + ex.getMessage());
            }
        });

        btnDeleteInventory.setOnAction(e -> {
            try {
                int inventoryId = Integer.parseInt(txtInventoryId.getText());
                deleteInventory(inventoryId);
                txtOutput.setText("Inventory deleted successfully.");
            } catch (Exception ex) {
                txtOutput.setText("Error: " + ex.getMessage());
            }
        });

        VBox layout = new VBox(10, lblProductId, txtProductId, lblWarehouseId, txtWarehouseId, lblQuantity, txtQuantity,
                btnAddInventory, btnShowInventory, lblInventoryId, txtInventoryId, btnDeleteInventory, txtOutput);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 400, 600);
        primaryStage.setTitle("Inventory Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void addInventory(int productId, int warehouseId, int quantity) throws SQLException {
        String query = "INSERT INTO inventory (product_id, warehouse_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, warehouseId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        }
    }

    private String getAllInventory() throws SQLException {
        StringBuilder result = new StringBuilder();
        String query = "SELECT inventory.inventory_id, products.name AS product_name, warehouses.name AS warehouse_name, " +
                       "inventory.quantity, inventory.last_updated " +
                       "FROM inventory " +
                       "JOIN products ON inventory.product_id = products.product_id " +
                       "JOIN warehouses ON inventory.warehouse_id = warehouses.warehouse_id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                result.append("Inventory ID: ").append(rs.getInt("inventory_id"))
                      .append(", Product Name: ").append(rs.getString("product_name"))
                      .append(", Warehouse Name: ").append(rs.getString("warehouse_name"))
                      .append(", Quantity: ").append(rs.getInt("quantity"))
                      .append(", Last Updated: ").append(rs.getTimestamp("last_updated"))
                      .append("\n");
            }
        }
        return result.toString();
    }

    private void deleteInventory(int inventoryId) throws SQLException {
        String query = "DELETE FROM inventory WHERE inventory_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, inventoryId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Inventory deleted successfully.");
            } else {
                System.out.println("Inventory not found.");
            }
        }
    }

    @Override
    public void stop() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        super.stop();
    }
}
