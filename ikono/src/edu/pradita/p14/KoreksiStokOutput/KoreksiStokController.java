package KoreksiStokOutput;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KoreksiStokController {

    @FXML
    private ComboBox<Barang> cmbBarang;

    @FXML
    private Label lblNamaBarang;

    @FXML
    private Label lblStokSaatIni;

    @FXML
    private Label lblSatuan;

    @FXML
    private TextField txtJumlahKoreksi;

    @FXML
    private Button btnKoreksi;

    private ObservableList<Barang> listBarang = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadDataBarang();

        cmbBarang.setItems(listBarang);
        cmbBarang.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tampilkanDetailBarang(newVal);
            }
        });

        btnKoreksi.setOnAction(event -> {
            koreksiStok();
        });
    }

    private void loadDataBarang() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM stok";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Barang barang = new Barang(rs.getInt("id_barang"), rs.getString("nama_barang"), rs.getInt("stok"), rs.getString("satuan"));
                listBarang.add(barang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memuat data barang: " + e.getMessage());
        }
    }

    private void tampilkanDetailBarang(Barang barang) {
        lblNamaBarang.setText("Nama Barang: " + barang.getNamaBarang());
        lblStokSaatIni.setText("Stok Saat Ini: " + barang.getStok());
        lblSatuan.setText("Satuan: " + barang.getSatuan());
    }

    private void koreksiStok() {
        Barang selectedBarang = cmbBarang.getSelectionModel().getSelectedItem();
        if (selectedBarang == null) {
            showAlert("Error", "Pilih barang terlebih dahulu.");
            return;
        }

        int jumlahKoreksi;
        try {
            jumlahKoreksi = Integer.parseInt(txtJumlahKoreksi.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Masukkan jumlah koreksi yang valid.");
            return;
        }

        if (jumlahKoreksi <= 0) {
            showAlert("Error", "Jumlah koreksi harus lebih dari 0.");
            return;
        }

        if (jumlahKoreksi > selectedBarang.getStok()) {
            showAlert("Error", "Jumlah koreksi melebihi stok yang tersedia.");
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Update stok di tabel stok
            String updateStokSql = "UPDATE stok SET stok = stok - ? WHERE id_barang = ?";
            PreparedStatement updateStokStmt = conn.prepareStatement(updateStokSql);
            updateStokStmt.setInt(1, jumlahKoreksi);
            updateStokStmt.setInt(2, selectedBarang.getIdBarang());
            updateStokStmt.executeUpdate();

            // Insert log koreksi stok
            String insertLogSql = "INSERT INTO log_koreksi_stok (tanggal, id_barang, jumlah_sebelum, jumlah_koreksi, jumlah_sesudah, keterangan, user) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertLogStmt = conn.prepareStatement(insertLogSql);
            insertLogStmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            insertLogStmt.setInt(2, selectedBarang.getIdBarang());
            insertLogStmt.setInt(3, selectedBarang.getStok());
            insertLogStmt.setInt(4, jumlahKoreksi);
            insertLogStmt.setInt(5, selectedBarang.getStok() - jumlahKoreksi);
            insertLogStmt.setString(6, "Koreksi Stok Output");
            insertLogStmt.setString(7, "User"); // Ganti dengan user yang sedang login (simulasi)
            insertLogStmt.executeUpdate();

            conn.commit(); // Commit transaction

            // Update stok di object Barang (untuk tampilan di UI)
            selectedBarang.setStok(selectedBarang.getStok() - jumlahKoreksi);
            tampilkanDetailBarang(selectedBarang);

            showAlert("Sukses", "Stok berhasil dikoreksi.");
            txtJumlahKoreksi.clear();

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                    showAlert("Error", "Gagal melakukan koreksi stok: " + e.getMessage() + "\nTransaksi di-rollback.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert("Error", "Gagal melakukan rollback transaksi: " + ex.getMessage());
                }
            } else {
                showAlert("Error", "Gagal melakukan koreksi stok: " + e.getMessage() + "\nKoneksi database gagal.");
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}