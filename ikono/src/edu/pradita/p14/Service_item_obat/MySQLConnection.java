package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/studentdb";
        String username = "root";
        String password = "karv2020";

        try {
            // Membuat koneksi ke database
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Koneksi berhasil!");

            // Membuat statement
            Statement stmt = conn.createStatement();

            // Mengeksekusi query
            String sql = "SELECT * FROM students";
            ResultSet rs = stmt.executeQuery(sql);

            // Menampilkan hasil query
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Major: " + rs.getString("major"));
                System.out.println("-------------");
            }

            // Menutup koneksi
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}