package application;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class PelunasanHutangApp extends Application {

    private final ObservableList<Hutang> hutangList = FXCollections.observableArrayList();

    private Connection connectToDatabase() {
        try {
            System.out.println("Mencoba menghubungkan ke database...");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Hutang", "root", "12345");
            System.out.println("Koneksi berhasil!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Koneksi gagal!");
            showAlert(Alert.AlertType.ERROR, "Koneksi Gagal", null);
            e.printStackTrace();
            return null;
        }
    }

    private void loadHutangData() {
        hutangList.clear();
        try (Connection conn = connectToDatabase();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM hutang")) {

            while (rs.next()) {
                hutangList.add(new Hutang(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("jenis_kelamin"),
                        rs.getBigDecimal("jumlah").toString(),
                        rs.getString("status"),
                        rs.getDate("tanggal").toLocalDate().toString()
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addHutang(int id, String nama, String jenisKelamin, BigDecimal jumlah, LocalDate tanggal) {
        try (Connection conn = connectToDatabase();
             PreparedStatement checkStmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM hutang WHERE id = ?");
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO hutang (id, nama, jenis_kelamin, jumlah, status, tanggal) VALUES (?, ?, ?, ?, 'Belum Lunas', ?)")
        ) {
            // Periksa apakah ID sudah ada
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "ID sudah digunakan, gunakan ID lain!");
                return;
            }

            // Tambahkan data baru
            pstmt.setInt(1, id);
            pstmt.setString(2, nama);
            pstmt.setString(3, jenisKelamin);
            pstmt.setBigDecimal(4, jumlah);
            pstmt.setDate(5, Date.valueOf(tanggal));
            pstmt.executeUpdate();
            loadHutangData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteHutang(int id) {
        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(
                     "DELETE FROM hutang WHERE id = ?")) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            loadHutangData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void markAsPaid(int id) {
        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE hutang SET status = 'Lunas' WHERE id = ?")) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            loadHutangData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        TableView<Hutang> tableView = new TableView<>();
        
        TableColumn<Hutang, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));


        TableColumn<Hutang, String> namaCol = new TableColumn<>("Nama");
        namaCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNama()));

        TableColumn<Hutang, String> genderCol = new TableColumn<>("Jenis Kelamin");
        genderCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJenisKelamin()));

        TableColumn<Hutang, String> jumlahCol = new TableColumn<>("Jumlah");
        jumlahCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJumlah()));

        TableColumn<Hutang, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        TableColumn<Hutang, String> tanggalCol = new TableColumn<>("Tanggal");
        tanggalCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTanggal()));

        TableColumn<Hutang, Void> actionCol = new TableColumn<>("Aksi");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button lunasButton = new Button("Tandai Lunas");

            {
                lunasButton.setOnAction(event -> {
                    Hutang hutang = getTableView().getItems().get(getIndex());
                    markAsPaid(hutang.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || "Lunas".equals(getTableView().getItems().get(getIndex()).getStatus())) {
                    setGraphic(null);
                } else {
                    setGraphic(lunasButton);
                }
            }
        });

        tableView.getColumns().addAll(idCol, namaCol, genderCol, jumlahCol, statusCol, tanggalCol, actionCol);
        tableView.setItems(hutangList);

        loadHutangData();

        // Form Input untuk Menambah Hutang
        TextField idField = new TextField();
        idField.setPromptText("ID");

        TextField namaField = new TextField();
        namaField.setPromptText("Nama");

        ComboBox<String> genderField = new ComboBox<>();
        genderField.setItems(FXCollections.observableArrayList("Pria", "Wanita"));
        genderField.setPromptText("Jenis Kelamin");

        TextField jumlahField = new TextField();
        jumlahField.setPromptText("Jumlah");

        DatePicker tanggalField = new DatePicker();
        tanggalField.setPromptText("Tanggal");

        Button addButton = new Button("Tambah");
        addButton.setOnAction(event -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String nama = namaField.getText();
                String jenisKelamin = genderField.getValue();
                BigDecimal jumlah = new BigDecimal(jumlahField.getText());
                LocalDate tanggal = tanggalField.getValue();

                if (nama.isEmpty() || jenisKelamin == null || jumlah.compareTo(BigDecimal.ZERO) <= 0 || tanggal == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Data tidak valid!");
                    return;
                }

                addHutang(id, nama, jenisKelamin, jumlah, tanggal);
                idField.clear();
                namaField.clear();
                genderField.setValue(null);
                jumlahField.clear();
                tanggalField.setValue(null);

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "ID dan jumlah harus berupa angka!");
            }
        });

        HBox inputForm = new HBox(10, idField, namaField, genderField, jumlahField, tanggalField, addButton);
        inputForm.setPadding(new Insets(10));

        // Tombol untuk Menghapus Hutang
        Button deleteButton = new Button("Hapus");
        deleteButton.setOnAction(event -> {
            Hutang selectedHutang = tableView.getSelectionModel().getSelectedItem();
            if (selectedHutang != null) {
                deleteHutang(selectedHutang.getId());
            } else {
                showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih hutang yang akan dihapus!");
            }
        });


        VBox root = new VBox(10, tableView, inputForm, deleteButton);
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.setTitle("Daftar Pelunasan Hutang");
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
