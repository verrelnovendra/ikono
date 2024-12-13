import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<BankAccount> bankAccounts = new ArrayList<BankAccount>();

    private BankAccount findAccount(String accountNumber) {
        for (BankAccount bankAccount : bankAccounts) {
            if (bankAccount.getAccountNumber().equals(accountNumber)) {
                return bankAccount;
            }
        }
        return null;
    }

    public OperationResult createBankAccount(String accountNumber, String bankName, double balance, String description) {
        if(findAccount(accountNumber) != null) {
            return new OperationResult(false, "Account already exists");
        }
        BankAccount newAccount = new BankAccount(accountNumber, bankName, balance, description);
        bankAccounts.add(newAccount);
        return new OperationResult(true, "Account created");
    }

    public OperationResult deposit(String accountNumber, double amount) {
        BankAccount account = findAccount(accountNumber);
        if(account != null) {
            account.deposit(amount);
            return new OperationResult(true, "Account deposited. New Balance is " + account.getBalance());
        }

        return new OperationResult(false, "Account not found");
    }

    public OperationResult withdraw(String accountNumber, double amount) {
        BankAccount account = findAccount(accountNumber);
        if(account != null) {
            account.withdraw(amount);
            return new OperationResult(true, "Account withdrawn. New Balance is " + account.getBalance());
        }
        return new OperationResult(false, "Account not found");
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }
}