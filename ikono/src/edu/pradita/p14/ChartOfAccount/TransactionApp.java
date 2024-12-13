package ChartOfAccount;

import java.sql.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TransactionApp extends Application {
    private TableView<Transaction> tableView = new TableView<>();
    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
    private Connection connection;

    @Override
    public void start(Stage stage) {
        // Initialize Database Connection
        initializeDatabase();

        // Create table columns and bind them to Transaction properties
        TableColumn<Transaction, Integer> idColumn = new TableColumn<>("TransactionID");
        TableColumn<Transaction, String> codeColumn = new TableColumn<>("Code");
        TableColumn<Transaction, Double> debitColumn = new TableColumn<>("Debit");
        TableColumn<Transaction, Double> creditColumn = new TableColumn<>("Credit");
        TableColumn<Transaction, String> descColumn = new TableColumn<>("Description");

        // Add columns to the table
        tableView.getColumns().addAll(idColumn, codeColumn, debitColumn, creditColumn, descColumn);

        // Bind columns to Transaction properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        debitColumn.setCellValueFactory(new PropertyValueFactory<>("debit"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("credit"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Text fields for adding/updating transactions
        TextField codeField = new TextField();
        codeField.setPromptText("Enter Code");

        TextField debitField = new TextField();
        debitField.setPromptText("Enter Debit");

        TextField creditField = new TextField();
        creditField.setPromptText("Enter Credit");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Enter Description");

        // Buttons for actions
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addTransaction(codeField, debitField, creditField, descriptionField));

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateTransaction(codeField, debitField, creditField, descriptionField));

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteTransaction());

        // Layout
        VBox layout = new VBox(10, tableView, codeField, debitField, creditField, descriptionField, addButton, updateButton, deleteButton);
        layout.setPadding(new Insets(10));
        Scene scene = new Scene(layout, 800, 600);

        // Load transactions into the table
        loadTransactions();

        stage.setTitle("Transaction Management");
        stage.setScene(scene);
        stage.show();
    }

    // Load transactions from the database
    void loadTransactions() {
        transactionList.clear();
        String query = "SELECT * FROM transaction";  
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("Executing query: " + query);  // Debugging line to confirm query

            while (resultSet.next()) {
                int id = resultSet.getInt("TransactionID");  
                int code = resultSet.getInt("AccountNumber");
                double debit = resultSet.getDouble("Debit");
                double credit = resultSet.getDouble("Credit");

                String description = resultSet.getString("Description");

                System.out.println("Row: " + id + ", " + code + ", " + debit + ", " + credit + ", " + description);

                transactionList.add(new Transaction(id, code, debit, credit, description));
            }
            tableView.setItems(transactionList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", e.getMessage());
        }
    }

    // Add a new transaction
    void addTransaction(TextField codeField, TextField debitField, TextField creditField, TextField descriptionField) {
        try {
            int code = Integer.parseInt(codeField.getText()); // Convert code to int
            double debit = Double.parseDouble(debitField.getText()); // Convert debit to double
            double credit = Double.parseDouble(creditField.getText()); // Convert credit to double
            String description = descriptionField.getText();

            String getBalanceSql = "SELECT Balance FROM chartofaccounts WHERE AccountNumber = ?";
            double currentBalance = 0;
            try (PreparedStatement ps = connection.prepareStatement(getBalanceSql)) {
                ps.setInt(1, code); 
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    currentBalance = rs.getDouble("Balance");
                }
            } catch (SQLException e) {
                showError("Error retrieving balance: " + e.getMessage());
                return;
            }

            // Calculate the new balance
            double updatedBalance = currentBalance + (debit - credit);

            if (description.isEmpty()) {
                showError("Description cannot be empty.");
                return;
            }
            
            String sql = "INSERT INTO transaction (AccountNumber, Debit, Credit, Description) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, code); 
                ps.setDouble(2, debit);
                ps.setDouble(3, credit);
                ps.setString(4, description);
                ps.executeUpdate();
                loadTransactions(); 
                codeField.clear();
                debitField.clear();
                creditField.clear();
                descriptionField.clear();
                showInfo("Transaction created successfully.");
                String sql1 = "UPDATE chartofaccounts SET Balance = ? WHERE AccountNumber = ?";
                try (PreparedStatement ps1 = connection.prepareStatement(sql1)) {
                	ps1.setDouble(1,  updatedBalance);
                	ps1.setInt(2, code);
                	ps1.executeUpdate();
                } catch (SQLException e) {
                    showError("Error inserting in CoA balance: " + e.getMessage());
                }
            } catch (SQLException e) {
                showError("Error creating transaction: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            showError("Account number, debit, and credit must be valid numbers.");
            }
        }

    // Update an existing transaction
    void updateTransaction(TextField codeField, TextField debitField, TextField creditField, TextField descriptionField) {
        Transaction selectedTransaction = tableView.getSelectionModel().getSelectedItem();

        if (selectedTransaction == null) {
            showError("Please select a transaction to update.");
            return;
        }

        // Validate inputs
        try {
            int code = Integer.parseInt(codeField.getText());
            double debit = Double.parseDouble(debitField.getText());
            double credit = Double.parseDouble(creditField.getText());
            String description = descriptionField.getText().trim();

            if (description.isEmpty()) {
                showError("Description cannot be empty.");
                return;
            }

            double updatedBalance = debit - credit;

            // Retrieve balances for the previous and new accounts
            double previousAccountBalance = getAccountBalance(selectedTransaction.getCode());
            double newAccountBalance = getAccountBalance(code);

            // Adjust balances
            double balanceAfterPrevious = previousAccountBalance - (selectedTransaction.getDebit() - selectedTransaction.getCredit());
            double balanceAfterNew = newAccountBalance + (debit - credit);

            // Begin transaction
            connection.setAutoCommit(false);

            // Update transaction record
            String updateTransactionSql = "UPDATE transaction SET AccountNumber = ?, Debit = ?, Credit = ?, Description = ? WHERE TransactionID = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateTransactionSql)) {
                ps.setInt(1, code);
                ps.setDouble(2, debit);
                ps.setDouble(3, credit);
                ps.setString(4, description);
                ps.setInt(5, selectedTransaction.getId());
                ps.executeUpdate();
            }

            // Update account balances
            updateAccountBalance(selectedTransaction.getCode(), balanceAfterPrevious);
            updateAccountBalance(code, balanceAfterNew);

            // Commit transaction
            connection.commit();

            // Clear fields and refresh table
            loadTransactions();
            codeField.clear();
            debitField.clear();
            creditField.clear();
            descriptionField.clear();
            showInfo("Transaction updated successfully.");
        } catch (NumberFormatException e) {
            showError("Account number, debit, and credit must be valid numbers.");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                showError("Error rolling back transaction: " + rollbackEx.getMessage());
            }
            showError("Error updating transaction: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                showError("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    // Helper method to retrieve account balance
    private double getAccountBalance(int accountNumber) throws SQLException {
        String sql = "SELECT Balance FROM chartofaccounts WHERE AccountNumber = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Balance");
                }
            }
        }
        return 0.0;
    }

    // Helper method to update account balance
    private void updateAccountBalance(int accountNumber, double newBalance) throws SQLException {
        String sql = "UPDATE chartofaccounts SET Balance = ? WHERE AccountNumber = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, accountNumber);
            ps.executeUpdate();
        }
    }

    // Delete the selected transaction
    void deleteTransaction() {
        Transaction selectedTransaction = tableView.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showError("Please select a transaction to delete.");
            return;
        }

        // Retrieve the current balance from the chartofaccounts table
        String getBalanceSql = "SELECT Balance FROM chartofaccounts WHERE AccountNumber = ?";
        double currentBalance = 0;
        try (PreparedStatement ps = connection.prepareStatement(getBalanceSql)) {
            ps.setInt(1, selectedTransaction.getCode()); 
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentBalance = rs.getDouble("Balance");
            }
        } catch (SQLException e) {
            showError("Error retrieving balance: " + e.getMessage());
            return;
        }

        // Calculate the new balance
        double balanceAfter = currentBalance - (selectedTransaction.getDebit() - selectedTransaction.getCredit());

        // Update the balance in the chartofaccounts table
        String updateBalanceSql = "UPDATE chartofaccounts SET Balance = ? WHERE AccountNumber = ?";
        try (PreparedStatement ps = connection.prepareStatement(updateBalanceSql)) {
            ps.setDouble(1, balanceAfter);
            ps.setInt(2, selectedTransaction.getCode()); // Assuming getCode() returns AccountNumber
            ps.executeUpdate();
        } catch (SQLException e) {
            showError("Error updating balance in CoA database: " + e.getMessage());
            return;
        }

        // Delete the transaction from the Transaction table
        String deleteTransactionSql = "DELETE FROM transaction WHERE TransactionID = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteTransactionSql)) {
            ps.setInt(1, selectedTransaction.getId()); // Assuming getId() returns TransactionID
            ps.executeUpdate();
            loadTransactions(); // Reload the table view
            showInfo("Transaction deleted successfully.");
        } catch (SQLException e) {
            showError("Error deleting transaction: " + e.getMessage());
        }
    }

    // Show an alert with an error message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Initialize the database connection
    private void initializeDatabase() {
        try {
            setConnection(DriverManager.getConnection("jdbc:mysql://localhost:3306/chartofaccounts", "root", "merliah"));
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
