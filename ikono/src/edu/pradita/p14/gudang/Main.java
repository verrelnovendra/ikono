package gudang;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnection.getConnection(); // Panggil koneksi
            if (connection != null) {
                System.out.println("Koneksi ke database berhasil!");

                // Query untuk mengambil data dari tabel `gudang`
                String query = "SELECT * FROM gudang";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Cetak hasil query
                while (resultSet.next()) {
                    System.out.println("ID Gudang: " + resultSet.getInt("id_gudang"));
                    System.out.println("Nama Gudang: " + resultSet.getString("nama_gudang"));
                    System.out.println("Kota: " + resultSet.getString("kota"));
                    System.out.println("Kapasitas: " + resultSet.getInt("kapasitas"));
                    System.out.println("Jumlah Barang: " + resultSet.getInt("jumlah_barang"));
                    System.out.println("Status: " + resultSet.getString("status"));
                    System.out.println("------------------------------");
                }

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



