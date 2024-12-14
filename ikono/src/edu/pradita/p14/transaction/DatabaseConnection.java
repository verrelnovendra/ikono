package transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
	
    public static Connection connect(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {    

        try (Connection conn = connect("jdbc:mysql://localhost:3306/transaction", "root", "Ella4022")) {
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
}
