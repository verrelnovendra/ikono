package rumah_sakit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== Sistem Informasi Poli =====");

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                // Menampilkan Data Poli
                System.out.println("\nDaftar Poli:");
                String queryPoli = "SELECT * FROM poli";
                try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(queryPoli)) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nama = rs.getString("nama");
                        String deskripsi = rs.getString("deskripsi");
                        System.out.println("ID: " + id + ", Nama: " + nama + ", Deskripsi: " + deskripsi);
                    }
                }

                // Menampilkan Data Dokter
                System.out.println("\nDaftar Dokter:");
                String queryDokter = "SELECT * FROM dokter";
                try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(queryDokter)) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nama = rs.getString("nama");
                        String spesialisasi = rs.getString("spesialisasi");
                        int poliId = rs.getInt("poli_id");
                        System.out.println("ID: " + id + ", Nama: " + nama + ", Spesialisasi: " + spesialisasi + ", Poli ID: " + poliId);
                    }
                }

                // Menampilkan Data Suster
                System.out.println("\nDaftar Suster:");
                String querySuster = "SELECT * FROM suster";
                try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(querySuster)) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nama = rs.getString("nama");
                        String bagian = rs.getString("bagian");
                        int poliId = rs.getInt("poli_id");
                        System.out.println("ID: " + id + ", Nama: " + nama + ", Bagian: " + bagian + ", Poli ID: " + poliId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
