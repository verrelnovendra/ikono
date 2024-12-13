package ChartOfAccount;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	public static void main(String[] args) {
		  try {
	            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chartofaccounts", "root", "merliah");
	            System.out.println("Connected to the database.");
	        } catch (SQLException e) {
	            System.out.println("Database connection failed: " + e.getMessage());
	            System.exit(1);
	        }
	    }
	}

