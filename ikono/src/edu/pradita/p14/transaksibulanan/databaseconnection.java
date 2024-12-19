package transaksibulanan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseconnection {
    private static final String URL = "jdbc:mysql://localhost:3306/databulanan"; // Ganti "localhost" dan "3306" sesuai server Anda
    private static final String USER = "root";                              // Ganti sesuai username database
    private static final String PASSWORD = "1234";                      // Ganti sesuai password database

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Register MySQL Driver
            return DriverManager.getConnection(URL, USER, PASSWORD); // Koneksi ke database
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Koneksi ke database gagal: " + e.getMessage());
        }
        return null; // Kembalikan null jika koneksi gagal
    }
}