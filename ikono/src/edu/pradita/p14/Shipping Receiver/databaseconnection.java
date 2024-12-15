package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseconnection {

	private static final String DB_URL = "jdbc:mysql://0.tcp.ap.ngrok.io:10725/pradita";
	static final String DB_USER = "jaki";
	private static final String DB_PASSWORD = "jaki123Z!";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection conn = databaseconnection.getConnection()) {
            if (conn != null) {
                System.out.println("Koneksi ke database berhasil!");
            }
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
    }
}






