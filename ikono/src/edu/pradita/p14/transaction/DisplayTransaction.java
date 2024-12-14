package transaction;

	import java.sql.*;

	public class DisplayTransaction {
	    public static void main(String[] args) {
	        String url = "jdbc:mysql://localhost:3306/transaction"; // Sesuaikan dengan nama database dan host Anda
	        String username = "root"; // Username MySQL Anda
	        String password = "Ella4022"; // Password MySQL Anda

	        String query = "SELECT transactions.transaction_id, users.username, products.product_name, transactions.transaction_date, transactions.quantity, transactions.total_price, transactions.payment_status " +
	                       "FROM transactions " +
	                       "JOIN users ON transactions.user_id = users.user_id " +
	                       "JOIN products ON transactions.product_id = products.product_id";

	        try {
	            Connection connection = DriverManager.getConnection(url, username, password);
	            
	            Statement statement = connection.createStatement();
	            
	            ResultSet resultSet = statement.executeQuery(query);

	            System.out.println("Transaction ID | Username | Product Name | Date | Quantity | Total Price | Payment Status");
	            System.out.println("-----------------------------------------------------------------------------------------------");
	            while (resultSet.next()) {
	                int transactionId = resultSet.getInt("transaction_id");
	                String usernameData = resultSet.getString("username");
	                String productName = resultSet.getString("product_name");
	                Timestamp transactionDate = resultSet.getTimestamp("transaction_date");
	                int quantity = resultSet.getInt("quantity");
	                double totalPrice = resultSet.getDouble("total_price");
	                String paymentStatus = resultSet.getString("payment_status");

	                System.out.printf("%13d | %-8s | %-12s | %-19s | %8d | %11.2f | %-14s\n",
	                                  transactionId, usernameData, productName, transactionDate, quantity, totalPrice, paymentStatus);
	            }
	            
	            resultSet.close();
	            statement.close();
	            connection.close();

	        } catch (SQLException e) {
	            
	            e.printStackTrace();
	        }
	    }
	}