package connector;

import java.sql.Connection;
import java.sql.DriverManager; 
import java.sql.ResultSet;
import java.sql.Statement;

public class database_connection {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/pd_uas";
		String username = "Celine";
		String password = "12345";
		
		try { 
			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.print("Koneksi Berhasil!");
			
			Statement stmt = conn.createStatement();
			
			String sql = "SELECT AccountNumber, Category, Description, Balance FROM chartofaccounts";
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				System.out.println("Account Number: " + rs.getInt("accountnumber"));
				System.out.println("Category: " + rs.getString("category"));
				System.out.println("Description: " + rs.getInt("description"));
				System.out.println("Balance: " + rs.getString("balance"));
				System.out.println("-----------");
			}
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


