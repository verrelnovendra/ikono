package connector;

public class database_coa {
	    private String AccountNumber;
	    private String Category;
	    private String Description;
	    private double Balance;

	    public database_coa(String AccountNumber, String Category, String Description, double Balance) {
	        this.AccountNumber = AccountNumber;
	        this.Category = Category;
	        this.Description = Description;
	        this.Balance = Balance;
	    }

	    // Getter methods
	    public String getAccountNumber() { return AccountNumber; }
	    public String getCategory() { return Category; }
	    public String getDescription() { return Description; }
	    public double getBalance() { return Balance; }
	}

