package edu.pradita.uas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.sql.*;

public class Penerimaan {

    @FXML
    private ComboBox<String> comboKodeSupplier;

    @FXML
    private TextField textNamaSupplier;

    @FXML
    private TextField textAlamatSupplier;

    @FXML
    private TextField textTeleponSupplier;

    @FXML
    private TextField textCariBarang;

    @FXML
    private TextField textKodeBarang;

    @FXML
    private TextField textNamaBarang;

    @FXML
    private TextField textJumlahBarang;

    @FXML
    private ComboBox<String> comboAdmin;

    @FXML
    private ListView<String> listItems;

    @FXML
    private TableView<Item> tableBarang;

    @FXML
    private TableColumn<Item, String> colKode;

    @FXML
    private TableColumn<Item, String> colNama;

    @FXML
    private TableColumn<Item, Integer> colJumlah;

    @FXML
    private Button btnSimpan;

    @FXML
    private Button btnBatal;

    @FXML
    private Button btnTutup;

    @FXML
    private TextField textTotalItems;

    @FXML
    private ComboBox<String> comboGudang;

    @FXML
    private Button btnTambah;

    @FXML
    private Button btnCari;

    @FXML
    private TextField textIdPenerimaan;

    @FXML
    private TextField textGetNamaGudang;

    @FXML
    private TextField textGetNamaAdmin;

    private ObservableList<Item> tableData;

    private Connection connection;

    @FXML
    public void initialize() {
        // Initialize database connection
        connectToDatabase();

        // Initialize TableView
        tableData = FXCollections.observableArrayList();
        colKode.setCellValueFactory(new PropertyValueFactory<>("kode"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        tableBarang.setItems(tableData);

        // Load data into components
        loadSuppliers();
        loadAdmins();
        loadGudangs();

        // Set text fields to non-editable
        textKodeBarang.setEditable(false);
        textNamaBarang.setEditable(false);

        listItems.setVisible(true);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://0.tcp.ap.ngrok.io:10725/pradita", "jaki", "jaki123Z!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchItems(String query) {
        listItems.getItems().clear();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT product_id, name FROM products WHERE name LIKE ?");
            statement.setString(1, "%" + query + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String item = resultSet.getInt("product_id") + " - " + resultSet.getString("name");
                listItems.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSuppliers() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id_supplier, name FROM suppliers");
            while (resultSet.next()) {
                comboKodeSupplier.getItems().add(resultSet.getString("id_supplier"));
            }

            comboKodeSupplier.setOnAction(e -> {
                String selectedSupplier = comboKodeSupplier.getValue();
                if (selectedSupplier != null) {
                    loadSupplierDetails(selectedSupplier);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSupplierDetails(String idSupplier) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM suppliers WHERE id_supplier = ?");
            statement.setString(1, idSupplier);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                textNamaSupplier.setText(resultSet.getString("name"));
                textAlamatSupplier.setText(resultSet.getString("alamat"));
                textTeleponSupplier.setText(resultSet.getString("contact_info"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAdmins() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id_admin, nama_admin FROM admin");
            while (resultSet.next()) {
                comboAdmin.getItems().add(resultSet.getString("id_admin"));
            }

            comboAdmin.setOnAction(e -> {
                String selectedAdmin = comboAdmin.getValue();
                if (selectedAdmin != null) {
                    loadAdminDetails(selectedAdmin);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAdminDetails(String idAdmin) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM admin WHERE id_admin = ?");
            statement.setString(1, idAdmin);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                textGetNamaAdmin.setText(resultSet.getString("nama_admin"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadGudangs() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id_gudang, nama_gudang FROM gudang");
            while (resultSet.next()) {
                comboGudang.getItems().add(resultSet.getString("id_gudang"));
            }

            comboGudang.setOnAction(e -> {
                String selectedGudang = comboGudang.getValue();
                if (selectedGudang != null) {
                    loadGudangDetails(selectedGudang);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadGudangDetails(String idGudang) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM gudang WHERE id_gudang = ?");
            statement.setString(1, idGudang);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                textGetNamaGudang.setText(resultSet.getString("nama_gudang"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleListClick(MouseEvent event) {
        String selectedItem = listItems.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String[] parts = selectedItem.split(" - ");
            textKodeBarang.setText(parts[0]);
            textNamaBarang.setText(parts[1]);
            listItems.setVisible(true);
        }
    }

    @FXML
    private void handleTambahClick(ActionEvent event) {
        String kode = textKodeBarang.getText();
        String nama = textNamaBarang.getText();
        int jumlah;

        try {
            jumlah = Integer.parseInt(textJumlahBarang.getText());
        } catch (NumberFormatException e) {
            showAlert("Jumlah harus berupa angka!");
            return;
        }

        if (kode.isEmpty() || nama.isEmpty() || jumlah <= 0) {
            showAlert("Semua field harus diisi dengan benar!");
            return;
        }

        // Add item to TableView
        tableData.add(new Item(kode, nama, jumlah));
        tableBarang.refresh();

        // Update total items
        updateTotalItems();

        // Clear input fields
        textKodeBarang.clear();
        textNamaBarang.clear();
        textJumlahBarang.clear();
    }

    @FXML
    private void handleSimpanClick(ActionEvent event) {
        try {
            connection.setAutoCommit(false);

            // Insert into penerimaan
            String insertPenerimaan = "INSERT INTO penerimaan (nomor_penerimaan, id_supplier, id_admin, id_gudang, total_items) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement penerimaanStatement = connection.prepareStatement(insertPenerimaan, Statement.RETURN_GENERATED_KEYS);

            penerimaanStatement.setString(1, textIdPenerimaan.getText());
            penerimaanStatement.setString(2, comboKodeSupplier.getValue());
            penerimaanStatement.setString(3, comboAdmin.getValue());
            penerimaanStatement.setString(4, comboGudang.getValue());
            penerimaanStatement.setInt(5, Integer.parseInt(textTotalItems.getText()));

            penerimaanStatement.executeUpdate();

            ResultSet generatedKeys = penerimaanStatement.getGeneratedKeys();
            int penerimaanId = 0;
            if (generatedKeys.next()) {
                penerimaanId = generatedKeys.getInt(1);
            }

            // Insert into detail_penerimaan
            String insertDetail = "INSERT INTO detail_penerimaan (id_penerimaan, product_id, jumlah) VALUES (?, ?, ?)";
            PreparedStatement detailStatement = connection.prepareStatement(insertDetail);

            for (Item item : tableData) {
                detailStatement.setInt(1, penerimaanId);
                detailStatement.setString(2, item.getKode());
                detailStatement.setInt(3, item.getJumlah());
                detailStatement.addBatch();
            }

            detailStatement.executeBatch();

            connection.commit();
            showAlert("Data berhasil disimpan!");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            showAlert("Terjadi kesalahan saat menyimpan data!");
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleBatalClick(ActionEvent event) {
        comboKodeSupplier.setValue(null);
        comboAdmin.setValue(null);
        comboGudang.setValue(null);
        textNamaSupplier.clear();
        textAlamatSupplier.clear();
        textTeleponSupplier.clear();
        textKodeBarang.clear();
        textNamaBarang.clear();
        textJumlahBarang.clear();
        textTotalItems.clear();
        textIdPenerimaan.clear();
        textGetNamaGudang.clear();
        textGetNamaAdmin.clear();
        tableData.clear();
        tableBarang.refresh();
    }

    @FXML
    private void handleTutupClick(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleCariClick(ActionEvent event) {
        String query = textCariBarang.getText();
        if (!query.isEmpty()) {
            listItems.setVisible(true);
            searchItems(query);
        } else {
            listItems.setVisible(true);
        }
    }

    private void updateTotalItems() {
        int totalItems = tableData.stream().mapToInt(Item::getJumlah).sum();
        textTotalItems.setText(String.valueOf(totalItems));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Item {
        private final String kode;
        private final String nama;
        private final int jumlah;

        public Item(String kode, String nama, int jumlah) {
            this.kode = kode;
            this.nama = nama;
            this.jumlah = jumlah;
        }

        public String getKode() {
            return kode;
        }

        public String getNama() {
            return nama;
        }

        public int getJumlah() {
            return jumlah;
        }
    }
}
