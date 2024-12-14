package application;

public class BankAccount {
    private String accountNumber;
    private String bankName;
    private String description;
    private double balance;

    public BankAccount(String accountNumber, String bankName, String description, double balance) {
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.description = description;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
