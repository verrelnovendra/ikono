package ChartOfAccount;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Transaction {    
    // Fields: JavaFX Properties for TableView binding
    private IntegerProperty id = new SimpleIntegerProperty();
    private IntegerProperty code = new SimpleIntegerProperty();
    private DoubleProperty debit = new SimpleDoubleProperty();
    private DoubleProperty credit = new SimpleDoubleProperty();
    private StringProperty description = new SimpleStringProperty();

    // Constructor: Initialize fields with data passed to the constructor
    public Transaction(int TransactionID, int AccountNumber, double debit, double credit, String description) {
        this.id.set(TransactionID);
        this.code.set(AccountNumber);
        this.debit.set(debit);
        this.credit.set(credit);
        this.description.set(description);
    }

    // Getter and Setter methods for JavaFX Properties (for binding with TableView)
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty codeProperty() { return code; }
    public DoubleProperty debitProperty() { return debit; }
    public DoubleProperty creditProperty() { return credit; }
    public StringProperty descriptionProperty() { return description; }

    // Standard Getter and Setter methods (Optional but not directly needed for TableView binding)
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    
    public int getCode() { return code.get(); }
    public void setCode(int code) { this.code.set(code); }
    
    public double getDebit() { return debit.get(); }
    public void setDebit(double debit) { this.debit.set(debit); }

    public double getCredit() { return credit.get(); }
    public void setCredit(double credit) { this.credit.set(credit); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    // Method to load data into ObservableList for TableView
    public static ObservableList<Transaction> loadBalance() {
        ObservableList<Transaction> transactable = FXCollections.observableArrayList();

        String url = "jdbc:mysql://localhost:3306/chartofaccounts";
        String user = "root";
        String password = "merliah";
        String sql = "SELECT TransactionID, AccountNumber, Debit, Credit, Description FROM transaction";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through ResultSet to add each row into ObservableList
            while (rs.next()) {
                int id = rs.getInt("TransactionID");
                int code = rs.getInt("AccountNumber");
                double debit = rs.getDouble("Debit");
                double credit = rs.getDouble("Credit");
                String description = rs.getString("Description");

                // Add each Transaction object to the ObservableList
                transactable.add(new Transaction(id, code, debit, credit, description));
            }

        } catch (SQLException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
            e.printStackTrace(); // Log stack trace for better debugging
        }

        return transactable;  // Return the ObservableList populated with data
    }
}
