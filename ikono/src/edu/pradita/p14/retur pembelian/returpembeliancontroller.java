package edu.pradita.p14.returpembelian;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class returpembeliancontroller {

    @FXML
    private ComboBox<String> comboIDPembelian;
    @FXML
    private ComboBox<String> comboProduk;
    @FXML
    private TextField textJumlah;
    @FXML
    private TextArea textAlasan;

    private ObservableList<String> pembelianList = FXCollections.observableArrayList();
    private ObservableList<String> produkList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Load data pembelian
        loadPembelianData();

        // Event handler for ComboBox selection
        comboIDPembelian.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadProdukData(newValue);
            }
        });
    }

    private void loadPembelianData() {
        try (Connection connection = getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id_pembelian FROM pembelian")) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                pembelianList.add(resultSet.getString("id_pembelian"));
            }
            comboIDPembelian.setItems(pembelianList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadProdukData(String idPembelian) {
        try (Connection connection = getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT p.name FROM detail_pembelian dp JOIN products p ON dp.product_id = p.product_id WHERE dp.id_pembelian = ?")) {

            statement.setString(1, idPembelian);
            ResultSet resultSet = statement.executeQuery();
            produkList.clear();
            while (resultSet.next()) {
                produkList.add(resultSet.getString("name"));
            }
            comboProduk.setItems(produkList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handlerSimpan() {
        String idPembelian = comboIDPembelian.getValue();
        String produk = comboProduk.getValue();
        String jumlah = textJumlah.getText();
        String alasanRetur = textAlasan.getText();

        if (idPembelian == null || produk == null || jumlah.isEmpty() || alasanRetur.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Mohon lengkapi semua data sebelum menyimpan.");
        } else {
            try (Connection connection = getDatabaseConnection();
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO retur_pembelian (id_pembelian, product_id, jumlah, alasan_retur) VALUES (?, ?, ?, ?)")) {

                statement.setString(1, idPembelian);
                statement.setInt(2, getProductId(produk));
                statement.setInt(3, Integer.parseInt(jumlah));
                statement.setString(4, alasanRetur);

                statement.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Retur pembelian berhasil disimpan.");

                // Reset form
                resetForm();

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan retur pembelian.");
            }
        }
    }

    @FXML
    public void handlerBatal() {
        resetForm();
    }

    @FXML
    public void handlerTutup() {
        Stage stage = (Stage) comboIDPembelian.getScene().getWindow();
        stage.close();
    }

    private void resetForm() {
        comboIDPembelian.getSelectionModel().clearSelection();
        comboProduk.getSelectionModel().clearSelection();
        textJumlah.clear();
        textAlasan.clear();
    }

    private int getProductId(String productName) {
        try (Connection connection = getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT product_id FROM products WHERE name = ?")) {

            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("product_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private Connection getDatabaseConnection() throws SQLException {
        String url = "jdbc:mysql://0.tcp.ap.ngrok.io:10725/pradita";
        String user = "jaki";
        String password = "jaki123Z!";
        return DriverManager.getConnection(url, user, password);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
