package inventoryPerWarehouse;

import java.sql.*;

public class DisplayData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/pos_inventory"; // Update with your database name and host
        String username = "root"; // Your MySQL username
        String password = "khabuki1?"; // Your MySQL password

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            displayProducts(connection);
            displayWarehouses(connection);
            displayInventory(connection);
            displaySalesTransactions(connection);
            displaySuppliers(connection);
            displayCategories(connection);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayProducts(Connection connection) throws SQLException {
        String query = "SELECT * FROM products";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Products:");
            System.out.println("Product ID | Name       | Description         | Price  | Category ID | Supplier ID | Created At");
            System.out.println("--------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int categoryId = rs.getInt("category_id");
                int supplierId = rs.getInt("supplier_id");
                Timestamp createdAt = rs.getTimestamp("created_at");

                System.out.printf("%10d | %-10s | %-20s | %6.2f | %11d | %11d | %s\n",
                                  productId, name, description, price, categoryId, supplierId, createdAt);
            }
        }
    }

    private static void displayWarehouses(Connection connection) throws SQLException {
        String query = "SELECT * FROM warehouses";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Warehouses:");
            System.out.println("Warehouse ID | Name         | Location       | Created At");
            System.out.println("-----------------------------------------------------------------");
            while (rs.next()) {
                int warehouseId = rs.getInt("warehouse_id");
                String name = rs.getString("name");
                String location = rs.getString("location");
                Timestamp createdAt = rs.getTimestamp("created_at");

                System.out.printf("%12d | %-12s | %-15s | %s\n",
                                  warehouseId, name, location, createdAt);
            }
        }
    }

    private static void displayInventory(Connection connection) throws SQLException {
        String query = "SELECT inventory.inventory_id, products.name AS product_name, warehouses.name AS warehouse_name, " +
                       "inventory.quantity, inventory.last_updated " +
                       "FROM inventory " +
                       "JOIN products ON inventory.product_id = products.product_id " +
                       "JOIN warehouses ON inventory.warehouse_id = warehouses.warehouse_id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Inventory:");
            System.out.println("Inventory ID | Product Name | Warehouse Name | Quantity | Last Updated");
            System.out.println("--------------------------------------------------------------------------");
            while (rs.next()) {
                int inventoryId = rs.getInt("inventory_id");
                String productName = rs.getString("product_name");
                String warehouseName = rs.getString("warehouse_name");
                int quantity = rs.getInt("quantity");
                Timestamp lastUpdated = rs.getTimestamp("last_updated");

                System.out.printf("%12d | %-12s | %-15s | %8d | %s\n",
                                  inventoryId, productName, warehouseName, quantity, lastUpdated);
            }
        }
    }

    private static void displaySalesTransactions(Connection connection) throws SQLException {
        String query = "SELECT * FROM sales_transactions";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Sales Transactions:");
            System.out.println("Transaction ID | Product ID | Quantity | Price  | Transaction Date     | Warehouse ID");
            System.out.println("----------------------------------------------------------------------------------");
            while (rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                Timestamp transactionDate = rs.getTimestamp("transaction_date");
                int warehouseId = rs.getInt("warehouse_id");

                System.out.printf("%14d | %10d | %8d | %6.2f | %-19s | %12d\n",
                                  transactionId, productId, quantity, price, transactionDate, warehouseId);
            }
        }
    }

    private static void displaySuppliers(Connection connection) throws SQLException {
        String query = "SELECT * FROM suppliers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Suppliers:");
            System.out.println("Supplier ID | Name  | Contact Info               | Created At");
            System.out.println("----------------------------------------------------------------");
            while (rs.next()) {
                int supplierId = rs.getInt("supplier_id");
                String name = rs.getString("name");
                String contactInfo = rs.getString("contact_info");
                Timestamp createdAt = rs.getTimestamp("created_at");

                System.out.printf("%11d | %-5s | %-25s | %s\n",
                                  supplierId, name, contactInfo, createdAt);
            }
        }
    }

    private static void displayCategories(Connection connection) throws SQLException {
        String query = "SELECT * FROM categories";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Categories:");
            System.out.println("Category ID | Name      | Description");
            System.out.println("--------------------------------------");
            while (rs.next()) {
                int categoryId = rs.getInt("category_id");
                String name = rs.getString("name");
                String description = rs.getString("description");

                System.out.printf("%11d | %-10s | %s\n", categoryId, name, description);
            }
        }
    }
}
