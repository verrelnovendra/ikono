package vendor.connection.p14;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VendorConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/vendor"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "liamsql14090668";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Koneksi berhasil!");
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
        return connection;
    }

    public static void main(String[] args) {
        getConnection();
    }
}
