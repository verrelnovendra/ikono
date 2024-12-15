package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class Main extends Application {
    private TableView<MutasiStok> table;
    private ObservableList<MutasiStok> data;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mutasi Stok");

        // Form Input
        TextField kodeBarangField = new TextField();
        TextField namaBarangField = new TextField();
        TextField jumlahField = new TextField();

        ComboBox<String> jenisMutasiField = new ComboBox<>();
        jenisMutasiField.setItems(FXCollections.observableArrayList("Masuk", "Keluar", "Transfer"));
        jenisMutasiField.setPromptText("Pilih jenis mutasi");
        Tooltip jenisTooltip = new Tooltip("Jenis mutasi:\n- Masuk: Barang masuk ke stok\n- Keluar: Barang keluar dari stok\n- Transfer: Barang dipindahkan antar lokasi");
        jenisMutasiField.setTooltip(jenisTooltip);

        TextField lokasiAwalField = new TextField();
        TextField lokasiTujuanField = new TextField();
        TextField keteranganField = new TextField();

        Button saveButton = new Button("Simpan");
        saveButton.setOnAction(e -> {
            try {
                saveMutasiStok(
                        kodeBarangField.getText(),
                        namaBarangField.getText(),
                        Integer.parseInt(jumlahField.getText()),
                        jenisMutasiField.getValue(),
                        lokasiAwalField.getText(),
                        lokasiTujuanField.getText(),
                        keteranganField.getText()
                );
                loadData();
                clearFields(kodeBarangField, namaBarangField, jumlahField, lokasiAwalField, lokasiTujuanField, keteranganField);
                jenisMutasiField.getSelectionModel().clearSelection();
            } catch (Exception ex) {
                showAlert("Error", "Gagal menyimpan data: " + ex.getMessage());
            }
        });

        // GridPane for Form
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Kode Barang:"), 0, 0);
        form.add(kodeBarangField, 1, 0);
        form.add(new Label("Nama Barang:"), 0, 1);
        form.add(namaBarangField, 1, 1);
        form.add(new Label("Jumlah:"), 0, 2);
        form.add(jumlahField, 1, 2);
        form.add(new Label("Jenis Mutasi:"), 0, 3);
        form.add(jenisMutasiField, 1, 3);
        form.add(new Label("Lokasi Awal:"), 0, 4);
        form.add(lokasiAwalField, 1, 4);
        form.add(new Label("Lokasi Tujuan:"), 0, 5);
        form.add(lokasiTujuanField, 1, 5);
        form.add(new Label("Keterangan:"), 0, 6);
        form.add(keteranganField, 1, 6);
        form.add(saveButton, 1, 7);

        // Tabel untuk menampilkan data
        table = new TableView<>();
        TableColumn<MutasiStok, String> kodeCol = new TableColumn<>("Kode Barang");
        kodeCol.setCellValueFactory(new PropertyValueFactory<>("kodeBarang"));
        TableColumn<MutasiStok, String> namaCol = new TableColumn<>("Nama Barang");
        namaCol.setCellValueFactory(new PropertyValueFactory<>("namaBarang"));
        TableColumn<MutasiStok, Integer> jumlahCol = new TableColumn<>("Jumlah");
        jumlahCol.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        TableColumn<MutasiStok, String> jenisCol = new TableColumn<>("Jenis Mutasi");
        jenisCol.setCellValueFactory(new PropertyValueFactory<>("jenisMutasi"));
        TableColumn<MutasiStok, String> lokasiAwalCol = new TableColumn<>("Lokasi Awal");
        lokasiAwalCol.setCellValueFactory(new PropertyValueFactory<>("lokasiAwal"));
        TableColumn<MutasiStok, String> lokasiTujuanCol = new TableColumn<>("Lokasi Tujuan");
        lokasiTujuanCol.setCellValueFactory(new PropertyValueFactory<>("lokasiTujuan"));
        TableColumn<MutasiStok, String> keteranganCol = new TableColumn<>("Keterangan");
        keteranganCol.setCellValueFactory(new PropertyValueFactory<>("keterangan"));

        table.getColumns().addAll(kodeCol, namaCol, jumlahCol, jenisCol, lokasiAwalCol, lokasiTujuanCol, keteranganCol);

        // Layout
        VBox layout = new VBox(10, form, table);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load data
        loadData();
    }

    private void saveMutasiStok(String kodeBarang, String namaBarang, int jumlah, String jenisMutasi, String lokasiAwal, String lokasiTujuan, String keterangan) throws SQLException {
        MutasiStokDAO dao = new MutasiStokDAO();
        MutasiStok mutasi = new MutasiStok();
        mutasi.setKodeBarang(kodeBarang);
        mutasi.setNamaBarang(namaBarang);
        mutasi.setJumlah(jumlah);
        mutasi.setJenisMutasi(jenisMutasi);
        mutasi.setLokasiAwal(lokasiAwal);
        mutasi.setLokasiTujuan(lokasiTujuan);
        mutasi.setKeterangan(keterangan);
        dao.insert(mutasi);
    }

    private void loadData() {
        try {
            MutasiStokDAO dao = new MutasiStokDAO();
            List<MutasiStok> mutasiList = dao.getAll();
            data = FXCollections.observableArrayList(mutasiList);
            table.setItems(data);
        } catch (SQLException e) {
            showAlert("Error", "Gagal memuat data: " + e.getMessage());
        }
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
