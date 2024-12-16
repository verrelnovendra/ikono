package edu.pradita.p14.asuransi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AsuransiController {

    @FXML
    private Button btnContinue;
    @FXML
    private Button btnBatal;
    @FXML
    private TableView<Asuransi> tabelAsuransi;
    @FXML
    private TableColumn<Asuransi, String> colKodeAsuransi;
    @FXML
    private TableColumn<Asuransi, String> colNamaAsuransi;
    @FXML
    private TableColumn<Asuransi, Double> colHarga;
    @FXML
    private TableColumn<Asuransi, String> colDeskripsi;
    @FXML
    private TextField textGetKodeAsuransi;
    @FXML
    private TextField textGetNamaAsuransi;
    @FXML
    private TextField textGetHargaAsuransi;
    @FXML
    private ComboBox<String> comboIdKaryawan;
    @FXML
    private TextField textGetNamaKaryawan;
    @FXML
    private TextField textGetJabatan;
    @FXML
    private ComboBox<String> comboSiklus;
    @FXML
    private Button btnKonfirm;

    private ObservableList<Asuransi> asuransiList = FXCollections.observableArrayList();
    private ObservableList<String> karyawanList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
    	
        colKodeAsuransi.setCellValueFactory(new PropertyValueFactory<>("kodeAsuransi"));
        colNamaAsuransi.setCellValueFactory(new PropertyValueFactory<>("namaAsuransi"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));

        loadAsuransiData();
        loadKaryawanData();

       
        comboSiklus.getItems().addAll("bulanan", "tahunan");

        
        tabelAsuransi.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                textGetKodeAsuransi.setText(newValue.getKodeAsuransi());
                textGetNamaAsuransi.setText(newValue.getNamaAsuransi());
                textGetHargaAsuransi.setText(String.valueOf(newValue.getHarga()));
            }
        });

        
        comboIdKaryawan.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadKaryawanDetails(newValue);
            }
        });

        btnBatal.setOnAction(event -> {
            closeWindow();
        });

        btnContinue.setOnAction(event -> {
            closeWindow();
        });

        btnKonfirm.setOnAction(event -> {
            konfirmasiAsuransi();
        });
    }

    private void loadAsuransiData() {
        try (Connection connection = getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM asuransi")) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Asuransi asuransi = new Asuransi(
                        resultSet.getString("id_asuransi"),
                        resultSet.getString("nama_asuransi"),
                        resultSet.getDouble("harga"),
                        resultSet.getString("deskripsi")
                );
                asuransiList.add(asuransi);
            }
            tabelAsuransi.setItems(asuransiList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadKaryawanData() {
        try (Connection connection = getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id_karyawan, nama FROM karyawan")) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                karyawanList.add(resultSet.getString("id_karyawan"));
            }
            comboIdKaryawan.setItems(karyawanList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadKaryawanDetails(String idKaryawan) {
        try (Connection connection = getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT nama, jabatan FROM karyawan WHERE id_karyawan = ?")) {

            statement.setString(1, idKaryawan);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                textGetNamaKaryawan.setText(resultSet.getString("nama"));
                textGetJabatan.setText(resultSet.getString("jabatan"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void konfirmasiAsuransi() {
        String idKaryawan = comboIdKaryawan.getValue();
        String idAsuransi = textGetKodeAsuransi.getText();
        String siklus = comboSiklus.getValue();

        if (idKaryawan != null && idAsuransi != null && siklus != null) {
            try (Connection connection = getDatabaseConnection();
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO asuransi_aktif (id_premi, id_asuransi, id_karyawan, siklus) VALUES (?, ?, ?, ?)")) {

                statement.setString(1, generateIdPremi());
                statement.setString(2, idAsuransi);
                statement.setString(3, idKaryawan);
                statement.setString(4, siklus);

                statement.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Konfirmasi Asuransi");
                alert.setHeaderText(null);
                alert.setContentText("Asuransi berhasil dikonfirmasi.");
                alert.showAndWait();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Konfirmasi Asuransi");
            alert.setHeaderText(null);
            alert.setContentText("Mohon lengkapi semua data sebelum mengkonfirmasi.");
            alert.showAndWait();
        }
    }

    private String generateIdPremi() {
        return "P" + System.currentTimeMillis();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnBatal.getScene().getWindow();
        stage.close();
    }

    private Connection getDatabaseConnection() throws SQLException {
        String url = "jdbc:mysql://0.tcp.ap.ngrok.io:10570/pradita ";
        String user = "jaki";
        String password = "jaki123Z!";
        return DriverManager.getConnection(url, user, password);
    }

    public static class Asuransi {
        private String kodeAsuransi;
        private String namaAsuransi;
        private Double harga;
        private String deskripsi;

        public Asuransi(String kodeAsuransi, String namaAsuransi, Double harga, String deskripsi) {
            this.kodeAsuransi = kodeAsuransi;
            this.namaAsuransi = namaAsuransi;
            this.harga = harga;
            this.deskripsi = deskripsi;
        }

        public String getKodeAsuransi() {
            return kodeAsuransi;
        }

        public void setKodeAsuransi(String kodeAsuransi) {
            this.kodeAsuransi = kodeAsuransi;
        }

        public String getNamaAsuransi() {
            return namaAsuransi;
        }

        public void setNamaAsuransi(String namaAsuransi) {
            this.namaAsuransi = namaAsuransi;
        }

        public Double getHarga() {
            return harga;
        }

        public void setHarga(Double harga) {
            this.harga = harga;
        }

        public String getDeskripsi() {
            return deskripsi;
        }

        public void setDeskripsi(String deskripsi) {
            this.deskripsi = deskripsi;
        }
    }
}
