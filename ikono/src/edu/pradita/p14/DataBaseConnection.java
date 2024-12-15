package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    
    private static final String URL = "jdbc:mysql://0.tcp.ap.ngrok.io:10725/pradita";
    private static final String USER = "jaki";
    private static final String PASSWORD = "jaki123Z!";
    
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void main(String[] args) {
        try (Connection conn = DataBaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Koneksi ke database 1 berhasil!");
            }
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
    }
}
