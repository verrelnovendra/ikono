package ChartOfAccount;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
import javafx.collections.FXCollections;

public class ChartOfAccountsApp extends Application {

    private static Connection connection;

    private TableView<Account> table;
    private TextField accountNumberField, descriptionField;
    private ComboBox<String> categoryComboBox; 
    private Button createButton, updateButton, deleteButton;

    public static void main(String[] args) {
        connectToDatabase();
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // UI Components
        table = new TableView<>();
        TableColumn<Account, Integer> accountNumberCol = new TableColumn<>("Account Number");
        accountNumberCol.setCellValueFactory(cellData -> cellData.getValue().accountNumberProperty().asObject());

        TableColumn<Account, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        TableColumn<Account, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        TableColumn<Account, Double> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(cellData -> cellData.getValue().balanceProperty().asObject());

        table.getColumns().addAll(accountNumberCol, categoryCol, descriptionCol, balanceCol);

        // Fields to input data
        accountNumberField = new TextField();
        accountNumberField.setPromptText("Account Number");

        // Category ComboBox (Dropdown)
        categoryComboBox = new ComboBox<>();
        categoryComboBox.setItems(FXCollections.observableArrayList("Assets", "Liabilities", "Equity", "Revenue", "Expenses"));
        categoryComboBox.setPromptText("Select Category");

        descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        // Buttons for CRUD operations
        createButton = new Button("Create");
        createButton.setOnAction(e -> createAccount());

        updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateAccount());

        deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteAccount());

        // Layout
        VBox inputLayout = new VBox(10, accountNumberField, categoryComboBox, descriptionField, createButton, updateButton, deleteButton);
        inputLayout.setPadding(new javafx.geometry.Insets(20));

        HBox layout = new HBox(30, table, inputLayout);
        layout.setPadding(new javafx.geometry.Insets(50));

        Scene scene = new Scene(layout, 800, 600);
        stage.setTitle("Chart of Accounts");
        stage.setScene(scene);
        stage.show();

        // Load data from the database
        loadAccounts();
    }

    // Connect to MySQL database
    private static void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ChartOfAccounts", "root", "merliah");
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            System.exit(1);
        }
    }

    // Load all accounts from the database and display in the table
    private void loadAccounts() {
        String sql = "SELECT * FROM ChartOfAccounts";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            table.getItems().clear();
            while (rs.next()) {
                int accountNumber = rs.getInt("AccountNumber");
                String category = rs.getString("Category");
                String description = rs.getString("Description");
                double balance = rs.getDouble("Balance");
                table.getItems().add(new Account(accountNumber, category, description, balance));
            }
        } catch (SQLException e) {
            showError("Error loading accounts: " + e.getMessage());
        }
    }

    // Create a new account
    private void createAccount() {
        String accountNumber = accountNumberField.getText();
        String category = categoryComboBox.getValue();
        String description = descriptionField.getText();

        if (accountNumber.isEmpty() || category == null || description.isEmpty()) {
            showError("Account Number, Category, and Description cannot be empty.");
            return;
        }

        // Ensure account number is an integer
        int accNumber;
        try {
            accNumber = Integer.parseInt(accountNumber);
        } catch (NumberFormatException e) {
            showError("Account Number must be an integer.");
            return;
        }

        String sql = "INSERT INTO ChartOfAccounts (AccountNumber, Category, Description) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, accNumber);
            ps.setString(2, category);
            ps.setString(3, description);
            ps.executeUpdate();
            loadAccounts(); // Reload the accounts
            accountNumberField.clear();
            categoryComboBox.setValue(null);
            descriptionField.clear();
            showInfo("Account created successfully.");
        } catch (SQLException e) {
            showError("Error creating account: " + e.getMessage());
        }
    }

    // Update an existing account
    private void updateAccount() {
        Account selectedAccount = table.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            showError("Please select an account to update.");
            return;
        }

        String category = categoryComboBox.getValue();
        String description = descriptionField.getText();

        if (category == null || description.isEmpty()) {
            showError("Category and Description cannot be empty.");
            return;
        }

        String sql = "UPDATE ChartOfAccounts SET Category = ?, Description = ? WHERE AccountNumber = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setString(2, description);
            ps.setInt(3, selectedAccount.getAccountNumber());
            ps.executeUpdate();
            loadAccounts(); // Reload the accounts
            categoryComboBox.setValue(null);
            descriptionField.clear();
            showInfo("Account updated successfully.");
        } catch (SQLException e) {
            showError("Error updating account: " + e.getMessage());
        }
    }

    // Delete an account
    private void deleteAccount() {
        Account selectedAccount = table.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            showError("Please select an account to delete.");
            return;
        }

        String sql = "DELETE FROM ChartOfAccounts WHERE AccountNumber = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, selectedAccount.getAccountNumber());
            ps.executeUpdate();
            loadAccounts(); // Reload the accounts
            showInfo("Account deleted successfully.");
        } catch (SQLException e) {
            showError("Error deleting account: " + e.getMessage());
        }
    }

    // Show error message in a popup
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show information message in a popup
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Account class to hold account data for the table
    public static class Account {
        private final SimpleIntegerProperty accountNumber;
        private final SimpleStringProperty category;
        private final SimpleStringProperty description;
        private final SimpleDoubleProperty balance;
        
        public Account(int accountNumber, String category, String description, double balance) {
            this.accountNumber = new SimpleIntegerProperty(accountNumber);
            this.category = new SimpleStringProperty(category);
            this.description = new SimpleStringProperty(description);
            this.balance = new SimpleDoubleProperty(balance);
        }

        public int getAccountNumber() {
            return accountNumber.get();
        }

        public SimpleIntegerProperty accountNumberProperty() {
            return accountNumber;
        }

        public String getCategory() {
            return category.get();
        }

        public SimpleStringProperty categoryProperty() {
            return category;
        }

        public String getDescription() {
            return description.get();
        }

        public SimpleStringProperty descriptionProperty() {
            return description;
        }
        
        public double getBalance() {
            return balance.get();
        }

        public SimpleDoubleProperty balanceProperty() {
            return balance;
        }
    }
}
