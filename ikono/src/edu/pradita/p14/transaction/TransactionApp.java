package transaction;
	
	import javafx.application.Application;
	import javafx.geometry.Insets;
	import javafx.scene.Scene;
	import javafx.scene.control.*;
	import javafx.scene.layout.VBox;
	import javafx.stage.Stage;

	import java.sql.*;

	public class TransactionApp extends Application {
		
	    private static final String DB_URL = "jdbc:mysql://localhost:3306/transaction";
	    private static final String DB_USER = "root"; 
	    private static final String DB_PASSWORD = "Ella4022"; 

	    private Connection connection;

	    public static void main(String[] args) {
	        launch(args);
	    }

	    @Override
	    public void start(Stage primaryStage) {
	        connectToDatabase();

	        Label lblUserId = new Label("User ID:");
	        TextField txtUserId = new TextField();
	        Label lblProductId = new Label("Product ID:");
	        TextField txtProductId = new TextField();
	        Label lblQuantity = new Label("Quantity:");
	        TextField txtQuantity = new TextField();
	        Label lblTotalPrice = new Label("Total Price:");
	        TextField txtTotalPrice = new TextField();
	        Button btnAddTransaction = new Button("Add Transaction");
	        Button btnShowTransactions = new Button("Show Transactions");
	        Label lblTransactionId = new Label("Transaction ID:");
	        TextField txtTransactionId = new TextField();
	        Button btnDeleteTransaction = new Button("Delete Transaction");
	        Label lblSearchTransactionId = new Label("Transaction ID:");
	        TextField txtSearchTransactionId = new TextField();
	        Button btnSearchTransaction = new Button("Search Transaction");
	        TextArea txtOutput = new TextArea();
	        txtOutput.setEditable(false);

	        btnAddTransaction.setOnAction(e -> {
	            try {
	                int userId = Integer.parseInt(txtUserId.getText());
	                int productId = Integer.parseInt(txtProductId.getText());
	                int quantity = Integer.parseInt(txtQuantity.getText());
	                double totalPrice = Double.parseDouble(txtTotalPrice.getText());
	                addTransaction(userId, productId, quantity, totalPrice);
	                txtOutput.setText("Transaction added successfully.");
	            } catch (Exception ex) {
	                txtOutput.setText("Error: " + ex.getMessage());
	            }
	        });

	        btnShowTransactions.setOnAction(e -> {
	            try {
	                String transactions = getAllTransactions();
	                txtOutput.setText(transactions);
	            } catch (Exception ex) {
	                txtOutput.setText("Error: " + ex.getMessage());
	            }
	        });

	        btnDeleteTransaction.setOnAction(e -> {
	            try {
	                int transactionId = Integer.parseInt(txtTransactionId.getText());
	                deleteTransaction(transactionId);
	                txtOutput.setText("Transaction deleted successfully.");
	            } catch (Exception ex) {
	                txtOutput.setText("Error: " + ex.getMessage());
	            }
	        });
	        
	        btnSearchTransaction.setOnAction(e -> {
	            try {
	                int transactionId = Integer.parseInt(txtSearchTransactionId.getText());
	                String result = searchTransaction(transactionId);
	                txtOutput.setText(result);
	            } catch (Exception ex) {
	                txtOutput.setText("Error: " + ex.getMessage());
	            }
	        });
	        
	        VBox layout = new VBox(10, lblUserId, txtUserId, lblProductId, txtProductId, lblQuantity, txtQuantity,
	                lblTotalPrice, txtTotalPrice, btnAddTransaction, btnShowTransactions,
	                lblTransactionId, txtTransactionId, btnDeleteTransaction,
	                lblSearchTransactionId, txtSearchTransactionId, btnSearchTransaction, txtOutput);
	        layout.setPadding(new Insets(15));

	        Scene scene = new Scene(layout, 400, 600);
	        primaryStage.setTitle("Transaction Management");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    }

	    private void connectToDatabase() {
	        try {
	            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	            System.out.println("Connected to the database successfully.");
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.exit(1);
	        }
	    }

	    private void addTransaction(int userId, int productId, int quantity, double totalPrice) throws SQLException {
	        String query = "INSERT INTO transactions (user_id, product_id, quantity, total_price) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setInt(1, userId);
	            stmt.setInt(2, productId);
	            stmt.setInt(3, quantity);
	            stmt.setDouble(4, totalPrice);
	            stmt.executeUpdate();
	        }
	    }

	    private String getAllTransactions() throws SQLException {
	        StringBuilder result = new StringBuilder();
	        String query = "SELECT * FROM transactions";
	        try (Statement stmt = connection.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {
	            while (rs.next()) {
	                result.append("ID: ").append(rs.getInt("transaction_id"))
	                      .append(", User ID: ").append(rs.getInt("user_id"))
	                      .append(", Product ID: ").append(rs.getInt("product_id"))
	                      .append(", Quantity: ").append(rs.getInt("quantity"))
	                      .append(", Total Price: ").append(rs.getDouble("total_price"))
	                      .append(", Status: ").append(rs.getString("payment_status"))
	                      .append("\n");
	            }
	        }
	        return result.toString();
	    }

	    private void deleteTransaction(int transactionId) throws SQLException {
	        String query = "DELETE FROM transactions WHERE transaction_id = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setInt(1, transactionId);
	            int rowsAffected = stmt.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Transaction deleted successfully.");
	            } else {
	                System.out.println("Transaction not found.");
	            }
	        }
	    }

	    private String searchTransaction(int transactionId) throws SQLException {
	        String query = "SELECT * FROM transactions WHERE transaction_id = ?";
	        StringBuilder result = new StringBuilder();
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setInt(1, transactionId);
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    result.append("ID: ").append(rs.getInt("transaction_id"))
	                          .append(", User ID: ").append(rs.getInt("user_id"))
	                          .append(", Product ID: ").append(rs.getInt("product_id"))
	                          .append(", Quantity: ").append(rs.getInt("quantity"))
	                          .append(", Total Price: ").append(rs.getDouble("total_price"))
	                          .append(", Status: ").append(rs.getString("payment_status"))
	                          .append("\n");
	                } else {
	                    result.append("Transaction not found.");
	                }
	            }
	        }
	        return result.toString();
	    }

	    @Override
	    public void stop() throws Exception {
	        if (connection != null && !connection.isClosed()) {
	            connection.close();
	        }
	        super.stop();
	    }
	}

