package edu.pradita.p14;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import java.sql.*;

public class FormTransaksiController {

    @FXML
    private TextField textAutoGenerateIdTransaksi, textInputIDBuyer, textNamaBuyer, textTeleponBuyer;

    @FXML
    private TextField textKodeBarang, textNamaBarang, textGetHarga, textJumlahBarang1, textTotalHarga, textBayar, textKembalian;

    @FXML
    private TableView<ObservableList<String>> tableBarang;

    @FXML
    private TableColumn<ObservableList<String>, String> colKode, colNama, colJumlah, colSubtotal;

    @FXML
    private ListView<String> listItems;

    private Connection connection;
    private ObservableList<String> productList = FXCollections.observableArrayList();
    private ObservableList<ObservableList<String>> tableData = FXCollections.observableArrayList();

    public void initialize() {
        connectDatabase();
        loadProducts();
        configureTable();
    }

    private void connectDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/pradita";
            String username = "root"; // Sesuaikan dengan konfigurasi Anda
            String password = "";    // Sesuaikan dengan konfigurasi Anda
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            showError("Database Connection Error", e.getMessage());
        }
    }

    private void loadProducts() {
        String query = "SELECT name FROM products";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                productList.add(rs.getString("name"));
            }
            listItems.setItems(productList);
        } catch (SQLException e) {
            showError("SQL Error", e.getMessage());
        }
    }

    private void configureTable() {
        colKode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        colNama.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        colJumlah.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        colSubtotal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
        tableBarang.setItems(tableData);
    }

    @FXML
    void handleCariClick(ActionEvent event) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, Integer.parseInt(textInputIDBuyer.getText()));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                textNamaBuyer.setText(rs.getString("username"));
                textTeleponBuyer.setText(rs.getString("email"));
            } else {
                showError("Data Not Found", "User ID not found in the database.");
            }
        } catch (SQLException e) {
            showError("SQL Error", e.getMessage());
        }
    }

    @FXML
    void handleListClick(MouseEvent event) {
        String selectedProduct = listItems.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            String query = "SELECT * FROM products WHERE name = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, selectedProduct);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    textKodeBarang.setText(String.valueOf(rs.getInt("product_id")));
                    textNamaBarang.setText(rs.getString("name"));
                    textGetHarga.setText(String.valueOf(rs.getBigDecimal("price")));
                }
            } catch (SQLException e) {
                showError("SQL Error", e.getMessage());
            }
        }
    }

    @FXML
    void handleTambahClick(ActionEvent event) {
        if (!textKodeBarang.getText().isEmpty() && !textJumlahBarang1.getText().isEmpty()) {
            String kode = textKodeBarang.getText();
            String nama = textNamaBarang.getText();
            String jumlah = textJumlahBarang1.getText();
            String harga = textGetHarga.getText();
            
            try {
                int qty = Integer.parseInt(jumlah);
                double price = Double.parseDouble(harga);
                double subtotal = qty * price;

                ObservableList<String> row = FXCollections.observableArrayList(kode, nama, jumlah, String.valueOf(subtotal));
                tableData.add(row);

                updateTotalHarga();
                clearProductFields();
            } catch (NumberFormatException e) {
                showError("Input Error", "Jumlah dan harga harus berupa angka yang valid.");
            }
        } else {
            showError("Input Error", "Pastikan semua field produk terisi sebelum menambahkan.");
        }
    }

    private void updateTotalHarga() {
        double total = 0;
        for (ObservableList<String> row : tableData) {
            total += Double.parseDouble(row.get(3));
        }
        textTotalHarga.setText(String.valueOf(total));
    }

    private void clearProductFields() {
        textKodeBarang.clear();
        textNamaBarang.clear();
        textGetHarga.clear();
        textJumlahBarang1.clear();
    }

    @FXML
    void handleSimpanClick(ActionEvent event) {
        String insertQuery = "INSERT INTO transactions (transaction_id, user_id, product_id, quantity, total_price, payment_status) " +
                             "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            for (ObservableList<String> row : tableData) {
                ps.setInt(1, Integer.parseInt(textAutoGenerateIdTransaksi.getText()));
                ps.setInt(2, Integer.parseInt(textInputIDBuyer.getText()));
                ps.setInt(3, Integer.parseInt(row.get(0)));
                ps.setInt(4, Integer.parseInt(row.get(2)));
                ps.setBigDecimal(5, new java.math.BigDecimal(row.get(3)));
                ps.setString(6, "pending");
                ps.addBatch();
            }

            ps.executeBatch();
            showInfo("Success", "Transaction saved successfully.");
            tableData.clear();
            updateTotalHarga();
        } catch (SQLException e) {
            showError("SQL Error", e.getMessage());
        }
    }

    @FXML
    void handleBatalClick(ActionEvent event) {
        clearFields();
    }

    @FXML
    void handleTutupClick(ActionEvent event) {
        System.exit(0);
    }

    private void clearFields() {
        textAutoGenerateIdTransaksi.clear();
        textInputIDBuyer.clear();
        textNamaBuyer.clear();
        textTeleponBuyer.clear();
        textKodeBarang.clear();
        textNamaBarang.clear();
        textGetHarga.clear();
        textJumlahBarang1.clear();
        textTotalHarga.clear();
        textBayar.clear();
        textKembalian.clear();
        tableData.clear();
        updateTotalHarga();
    }

    @FXML
    void handleBayarKeyReleased() {
        try {
            double totalHarga = Double.parseDouble(textTotalHarga.getText());
            double bayar = Double.parseDouble(textBayar.getText());
            double kembalian = bayar - totalHarga;

            if (kembalian >= 0) {
                textKembalian.setText(String.format("%.2f", kembalian));
            } else {
                textKembalian.setText("0.00");
            }
        } catch (NumberFormatException e) {
            textKembalian.setText("0.00");
        }
    }
    

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
