package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MutasiStokDAO {
    // Insert data mutasi stok ke database
    public void insert(MutasiStok mutasi) throws SQLException {
        String sql = "INSERT INTO mutasi_stok (kode_barang, nama_barang, jumlah, jenis_mutasi, lokasi_awal, lokasi_tujuan, keterangan) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mutasi.getKodeBarang());
            ps.setString(2, mutasi.getNamaBarang());
            ps.setInt(3, mutasi.getJumlah());
            ps.setString(4, mutasi.getJenisMutasi());
            ps.setString(5, mutasi.getLokasiAwal());
            ps.setString(6, mutasi.getLokasiTujuan());
            ps.setString(7, mutasi.getKeterangan());
            ps.executeUpdate();
        }
    }

    // Ambil semua data dari tabel mutasi_stok
    public List<MutasiStok> getAll() throws SQLException {
        String sql = "SELECT * FROM mutasi_stok";
        List<MutasiStok> list = new ArrayList<>();
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MutasiStok mutasi = new MutasiStok();
                mutasi.setIdMutasi(rs.getInt("id_mutasi"));
                mutasi.setKodeBarang(rs.getString("kode_barang"));
                mutasi.setNamaBarang(rs.getString("nama_barang"));
                mutasi.setJumlah(rs.getInt("jumlah"));
                mutasi.setJenisMutasi(rs.getString("jenis_mutasi"));
                mutasi.setLokasiAwal(rs.getString("lokasi_awal"));
                mutasi.setLokasiTujuan(rs.getString("lokasi_tujuan"));
                mutasi.setTanggalMutasi(rs.getString("tanggal_mutasi"));
                mutasi.setKeterangan(rs.getString("keterangan"));
                list.add(mutasi);
            }
        }
        return list;
    }
}
