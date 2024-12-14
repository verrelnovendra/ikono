package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private final List<BankAccount> bankAccounts = new ArrayList<>();
    private Connection connection;

    public Bank() {
        initializeDatabaseConnection();
        loadBankAccounts();
    }

    private void initializeDatabaseConnection() {
        try {
            if(connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_module", "root", "root");
                System.out.println("Database connection established.");
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to database" + e.getMessage());
        }
    }

    // Loads existing bank accounts from the database
    private void loadBankAccounts() {
        if (connection == null) {
            System.err.println("Cannot load accounts: No database connection established.");
            return;
        }

        String sql = "SELECT * FROM Bank";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BankAccount bankAccount = new BankAccount(
                        rs.getString("accountNumber"),
                        rs.getString("bankName"),
                        rs.getString("description"),
                        rs.getDouble("balance")
                );
                bankAccounts.add(bankAccount);
            }
            System.out.println("Loaded bank accounts from the database.");
        } catch (SQLException e) {
            System.err.println("Error loading bank accounts: " + e.getMessage());
        }
    }

    // Checks if an account exists in the database
    private boolean accountExists(String accountNumber) {
        if (connection == null) {
            System.err.println("Cannot check account: No database connection established.");
            return false;
        }

        String sql = "SELECT 1 FROM Bank WHERE accountNumber = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking account existence: " + e.getMessage());
        }
        return false;
    }

    // Creates a new bank account
    public boolean createBankAccount(String accountNumber, String bankName, String description, double initialBalance) {
        if (connection == null) {
            System.err.println("Cannot create account: No database connection established.");
            return false;
        }

        if (accountExists(accountNumber)) {
            System.err.println("Account already exists.");
            return false;
        }

        String insertSQL = "INSERT INTO Bank (accountNumber, bankName, description, balance) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, bankName);
            pstmt.setString(3, description);
            pstmt.setDouble(4, initialBalance);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                BankAccount newAccount = new BankAccount(accountNumber, bankName, description, initialBalance);
                bankAccounts.add(newAccount);
                System.out.println("Bank account created successfully.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating bank account: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteBankAccount(String accountNumber) {
        if (connection == null) {
            System.err.println("Cannot delete account: No database connection established.");
            return false;
        }

        if (!accountExists(accountNumber)) {
            System.err.println("Account does not exist.");
            return false;
        }

        String deleteSQL = "DELETE FROM Bank WHERE accountNumber = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setString(1, accountNumber);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bank account deleted successfully.");
                bankAccounts.removeIf(account -> account.getAccountNumber().equals(accountNumber));
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error deleting account: " + e.getMessage());
        }
        return false;
    }

    public boolean updateBankAccount(String refAccountNumber, BankAccount updatedBankAccount) {
        if (connection == null) {
            System.err.println("Cannot update account: No database connection established.");
        }

        if (!accountExists(refAccountNumber)) {
            System.err.println("Account does not exist.");
            return false;
        }

        String updateSQL = "UPDATE Bank SET accountNumber = ?, bankName = ?, description = ?, balance = ? WHERE accountNumber = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, updatedBankAccount.getAccountNumber());
            pstmt.setString(2, updatedBankAccount.getBankName());
            pstmt.setString(3, updatedBankAccount.getDescription());
            pstmt.setDouble(4, updatedBankAccount.getBalance());
            pstmt.setString(5, refAccountNumber);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                bankAccounts.stream()
                        .filter(account -> account.getAccountNumber().equals(refAccountNumber))
                        .forEach( account -> {
                            account.setAccountNumber(updatedBankAccount.getAccountNumber());
                            account.setBankName(updatedBankAccount.getBankName());
                            account.setDescription(updatedBankAccount.getDescription());
                            account.setBalance(updatedBankAccount.getBalance());
                        });
                System.out.println("Bank account updated successfully.");
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error updating account: " + e.getMessage());
        }

        return false;
    }

    public List<BankAccount> getBankAccounts() {
        return new ArrayList<>(bankAccounts);
    }
}
