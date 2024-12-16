package transaksiPembelian;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.sql.*;

public class PurchaseManagementApp extends Application {

    // ObservableLists for table data
    private final ObservableList<PurchaseTransaction> transactions = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Purchase Management System");

        // Tabs for transactions
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(createTransactionsTab());

        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load data from the database
        loadTransactionsFromDatabase();
    }

    private Tab createTransactionsTab() {
        TableView<PurchaseTransaction> table = new TableView<>(transactions);

        TableColumn<PurchaseTransaction, Integer> idColumn = new TableColumn<>("Transaction ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().transactionIdProperty().asObject());

        TableColumn<PurchaseTransaction, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        TableColumn<PurchaseTransaction, Double> totalColumn = new TableColumn<>("Total Amount");
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty().asObject());

        table.getColumns().addAll(idColumn, dateColumn, totalColumn);

        VBox form = createTransactionForm();

        VBox layout = new VBox(10, table, form);
        layout.setPadding(new Insets(10));

        return new Tab("Transactions", layout);
    }

    private VBox createTransactionForm() {
        TextField transactionIdField = new TextField();
        transactionIdField.setPromptText("Transaction ID");

        TextField dateField = new TextField();
        dateField.setPromptText("Date (YYYY-MM-DD)");

        TextField totalField = new TextField();
        totalField.setPromptText("Total Amount");

        Button addButton = new Button("Add Transaction");
        addButton.setOnAction(e -> {
            try {
                int transactionId = Integer.parseInt(transactionIdField.getText());
                String date = dateField.getText();
                double totalAmount = Double.parseDouble(totalField.getText());

                PurchaseTransaction transaction = new PurchaseTransaction(transactionId, date, totalAmount);

                // Add to the database and ObservableList
                addTransactionToDatabase(transaction);
                transactions.add(transaction);

                transactionIdField.clear();
                dateField.clear();
                totalField.clear();
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for Transaction ID and Total Amount.");
            }
        });

        VBox form = new VBox(5, transactionIdField, dateField, totalField, addButton);
        return form;
    }

    private void loadTransactionsFromDatabase() {
        String query = "SELECT * FROM transactions";

        try (Connection conn = MySQLConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                transactions.add(new PurchaseTransaction(
                        rs.getInt("transaction_id"),
                        rs.getString("date"),
                        rs.getDouble("total_amount")
                ));
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load transactions: " + e.getMessage());
        }
    }

    private void addTransactionToDatabase(PurchaseTransaction transaction) {
        String query = "INSERT INTO transactions (transaction_id, date, total_amount) VALUES (?, ?, ?)";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, transaction.getTransactionId());
            stmt.setString(2, transaction.getDate());
            stmt.setDouble(3, transaction.getTotalAmount());
            stmt.executeUpdate();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add transaction: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

class MySQLConnection {
    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/transaksi";
        String username = "root";
        String password = "Scythia89.";

        try {
            // Make sure you're using a MySQL Connector/J version compatible with JDK 17
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
        return null;
    }
}

class PurchaseTransaction {
    private final IntegerProperty transactionId = new SimpleIntegerProperty();
    private final StringProperty date = new SimpleStringProperty();
    private final DoubleProperty totalAmount = new SimpleDoubleProperty();

    public PurchaseTransaction(int transactionId, String date, double totalAmount) {
        this.transactionId.set(transactionId);
        this.date.set(date);
        this.totalAmount.set(totalAmount);
    }

    public IntegerProperty transactionIdProperty() {
        return transactionId;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public DoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public int getTransactionId() {
        return transactionId.get();
    }

    public String getDate() {
        return date.get();
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }
}
