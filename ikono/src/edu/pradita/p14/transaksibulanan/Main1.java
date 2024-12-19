package transaksibulanan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;

public class Main1 {

    public static void main(String[] args) {
        try {
            Connection connection = databaseconnection.getConnection(); // Panggil koneksi
            if (connection != null) {
                System.out.println("Koneksi ke database berhasil!");

                // Query untuk mengambil data dari tabel pembelian
                String query = "SELECT tanggal, nama_barang, jumlah, harga FROM pembelian";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Header laporan
                DecimalFormat df = new DecimalFormat("#,###.00");
                System.out.println("==============================");
                System.out.println("  Laporan Pembelian Bulanan   ");
                System.out.println("==============================");
                System.out.printf("%-12s %-15s %-8s %-12s %-12s\n", "Tanggal", "Nama Barang", "Jumlah", "Harga", "Total");
                System.out.println("---------------------------------------------------");

                double totalKeseluruhan = 0.0;

                // Cetak hasil query
                while (resultSet.next()) {
                    String tanggal = resultSet.getString("tanggal");
                    String namaBarang = resultSet.getString("nama_barang");
                    int jumlah = resultSet.getInt("jumlah");
                    double harga = resultSet.getDouble("harga");
                    double totalHarga = jumlah * harga;

                    totalKeseluruhan += totalHarga;
                    System.out.printf("%-12s %-15s %-8d %-12s %-12s\n",
                            tanggal,
                            namaBarang,
                            jumlah,
                            df.format(harga),
                            df.format(totalHarga));
                }

                System.out.println("---------------------------------------------------");
                System.out.printf("%-35s %-12s\n", "Total Keseluruhan", df.format(totalKeseluruhan));
                System.out.println("==============================");

                // Tutup koneksi
                resultSet.close();
                statement.close();
                connection.close();
            } else {
                System.out.println("Koneksi ke database gagal!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}