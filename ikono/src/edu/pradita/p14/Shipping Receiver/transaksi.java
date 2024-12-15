package uaspemdas;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class transaksi extends Application {
	
    private static final String DB_URL = "jdbc:mysql://0.tcp.ap.ngrok.io:10725/pradita";
    static final String DB_USER = "jaki";
    private static final String DB_PASSWORD = "jaki123Z!";

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Aplikasi Transaksi Pengiriman");

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        Label titleLabel = new Label("Pilih Menu");
        titleLabel.setStyle("-fx-font-size: 18px;");

        Button lihatPengirimButton = new Button("Lihat Semua Pengirim");
        Button tambahTransaksiButton = new Button("Tambah Transaksi Pengiriman Barang");
        Button lihatPengirimanMerekButton = new Button("Lihat Semua Pengiriman Merek");
        Button hapusDataButton = new Button("Hapus Data");
        Button keluarButton = new Button("Keluar");

        lihatPengirimButton.setOnAction(e -> lihatPengirim());
        tambahTransaksiButton.setOnAction(e -> tambahTransaksi());
        lihatPengirimanMerekButton.setOnAction(e -> lihatPengirimanMerek());
        hapusDataButton.setOnAction(e -> hapusData());
        keluarButton.setOnAction(e -> primaryStage.close());

        root.getChildren().addAll(titleLabel, lihatPengirimButton, tambahTransaksiButton,
                                  lihatPengirimanMerekButton, hapusDataButton, keluarButton);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void lihatPengirim() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM senders");
            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                sb.append("ID: ").append(resultSet.getInt("id"))
                  .append(", Nama: ").append(resultSet.getString("name"))
                  .append(", Alamat: ").append(resultSet.getString("address"))
                  .append("\n");
            }

            showAlert(Alert.AlertType.INFORMATION, "Data Pengirim", sb.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void tambahTransaksi() {
        Stage transaksiStage = new Stage();
        transaksiStage.setTitle("Tambah Transaksi Pengiriman");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));

        Label senderIdLabel = new Label("ID Pengirim:");
        TextField senderIdField = new TextField();
        Label senderNameLabel = new Label("Nama Pengirim:");
        TextField senderNameField = new TextField();
        Label senderAddressLabel = new Label("Alamat Pengirim:");
        TextField senderAddressField = new TextField();

        Label receiverIdLabel = new Label("ID Penerima:");
        TextField receiverIdField = new TextField();
        Label receiverNameLabel = new Label("Nama Penerima:");
        TextField receiverNameField = new TextField();
        Label receiverAddressLabel = new Label("Alamat Penerima:");
        TextField receiverAddressField = new TextField();

        Label shipmentCodeLabel = new Label("Kode Pengiriman:");
        TextField shipmentCodeField = new TextField();
        Label shippingDateLabel = new Label("Tanggal Pengiriman:");
        TextField shippingDateField = new TextField();
        Label shippingMethodLabel = new Label("Metode Pengiriman:");
        TextField shippingMethodField = new TextField();

        Button submitButton = new Button("Tambah Transaksi");
        submitButton.setOnAction(e -> {
            try {
                String senderId = senderIdField.getText().trim();
                String senderName = senderNameField.getText().trim();
                String senderAddress = senderAddressField.getText().trim();

                String receiverId = receiverIdField.getText().trim();
                String receiverName = receiverNameField.getText().trim();
                String receiverAddress = receiverAddressField.getText().trim();

                String shipmentCode = shipmentCodeField.getText().trim();
                String shippingDate = shippingDateField.getText().trim();
                String shippingMethod = shippingMethodField.getText().trim();

                // Validasi pengirim dan penerima, jika belum ada di database, maka tambah
                if (!isSenderExist(senderId)) {
                    addSender(senderId, senderName, senderAddress);
                }
                if (!isReceiverExist(receiverId)) {
                    addReceiver(receiverId, receiverName, receiverAddress);
                }

                // Lanjutkan menambahkan transaksi
                String sql = "INSERT INTO shipments (shipment_code, sender_id, receiver_id, shipping_date, status) VALUES (?, ?, ?, ?, 'pending')";
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                     PreparedStatement statement = connection.prepareStatement(sql)) {

                    statement.setString(1, shipmentCode);
                    statement.setInt(2, Integer.parseInt(senderId));
                    statement.setInt(3, Integer.parseInt(receiverId));
                    statement.setString(4, shippingDate);

                    int rows = statement.executeUpdate();
                    if (rows > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Transaksi berhasil ditambahkan.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambahkan transaksi.");
                }

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Pastikan ID Pengirim dan ID Penerima adalah angka.");
            }
        });

        // Menambahkan elemen-elemen input ke grid
        grid.add(senderIdLabel, 0, 0);
        grid.add(senderIdField, 1, 0);
        grid.add(senderNameLabel, 0, 1);
        grid.add(senderNameField, 1, 1);
        grid.add(senderAddressLabel, 0, 2);
        grid.add(senderAddressField, 1, 2);

        grid.add(receiverIdLabel, 0, 3);
        grid.add(receiverIdField, 1, 3);
        grid.add(receiverNameLabel, 0, 4);
        grid.add(receiverNameField, 1, 4);
        grid.add(receiverAddressLabel, 0, 5);
        grid.add(receiverAddressField, 1, 5);

        grid.add(shipmentCodeLabel, 0, 6);
        grid.add(shipmentCodeField, 1, 6);
        grid.add(shippingDateLabel, 0, 7);
        grid.add(shippingDateField, 1, 7);
        grid.add(shippingMethodLabel, 0, 8);
        grid.add(shippingMethodField, 1, 8);
        grid.add(submitButton, 1, 9);

        Scene scene = new Scene(grid, 400, 400);
        transaksiStage.setScene(scene);
        transaksiStage.show();
    }

    private boolean isSenderExist(String senderId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement("SELECT id FROM senders WHERE id = ?")) {
            stmt.setString(1, senderId);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next(); // Jika ada hasil, pengirim sudah ada
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addSender(String senderId, String senderName, String senderAddress) {
        String sql = "INSERT INTO senders (id, name, address) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(senderId));
            stmt.setString(2, senderName);
            stmt.setString(3, senderAddress);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambahkan pengirim.");
        }
    }

    private boolean isReceiverExist(String receiverId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement("SELECT id FROM receivers WHERE id = ?")) {
            stmt.setString(1, receiverId);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next(); // Jika ada hasil, penerima sudah ada
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addReceiver(String receiverId, String receiverName, String receiverAddress) {
        String sql = "INSERT INTO receivers (id, name, address) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(receiverId));
            stmt.setString(2, receiverName);
            stmt.setString(3, receiverAddress);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambahkan penerima.");
        }
    }


    private void lihatPengirimanMerek() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM shippingmerek");
            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                sb.append(resultSet.getInt("id"))
                  .append(" | ").append(resultSet.getString("name"))
                  .append(" | ").append(resultSet.getDouble("harga"))
                  .append(" | ").append(resultSet.getString("jenis"))
                  .append("\n");
            }

            showAlert(Alert.AlertType.INFORMATION, "Data Pengiriman Merek", sb.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void hapusData() {
        showAlert(Alert.AlertType.WARNING, "Hapus Data", "Fungsi hapus data belum diimplementasikan.");
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isValidCourier(String courierName) {
        Set<String> courierNames = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT name FROM shippingmerek");
            while (resultSet.next()) {
                // Ambil nama kurir dan bersihkan dari spasi yang tidak perlu
                String storedCourierName = resultSet.getString("name").trim().toLowerCase();
                courierNames.add(storedCourierName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data kurir.");
            return false;
        }

        // Bersihkan input dari spasi ekstra dan ubah ke huruf kecil
        String cleanedCourierName = courierName.trim().toLowerCase();
        System.out.println("Nama kurir yang dimasukkan: " + cleanedCourierName); // Debugging

        // Cek apakah nama kurir valid
        if (!courierNames.contains(cleanedCourierName)) {
            System.out.println("Nama kurir tidak ditemukan dalam database."); // Debugging
            return false;
        }

        return true;
    }

    private boolean isValidSenderReceiver(int senderId, int receiverId) {
        boolean isValid = false;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Cek apakah senderId valid
            String senderQuery = "SELECT COUNT(*) FROM senders WHERE id = ?";
            try (PreparedStatement senderStmt = connection.prepareStatement(senderQuery)) {
                senderStmt.setInt(1, senderId);
                ResultSet senderResult = senderStmt.executeQuery();
                if (senderResult.next() && senderResult.getInt(1) > 0) {
                    // Cek apakah receiverId valid
                    String receiverQuery = "SELECT COUNT(*) FROM receivers WHERE id = ?";
                    try (PreparedStatement receiverStmt = connection.prepareStatement(receiverQuery)) {
                        receiverStmt.setInt(1, receiverId);
                        ResultSet receiverResult = receiverStmt.executeQuery();
                        if (receiverResult.next() && receiverResult.getInt(1) > 0) {
                            isValid = true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }
}
