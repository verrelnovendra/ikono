public class BankAccount {
    private String accountNumber;
    private String bankName;
    private double balance;
    private String description;

    public BankAccount(String accountNumber, String bankName, double balance, String description) {
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.balance = balance;
        this.description = description;
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

    public double getBalance() {
        return balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public boolean withdraw(double amount) {
        if(this.balance >= amount) {
            this.balance -= amount;
            return true;
        }

        return false;
    }
}
