package UAS;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UASpemdas extends Application {
    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(UASpemdas.class.getName());

    private TextField txtProductId;
    private TextField txtProductName;
    private TextField txtCurrentStock;
    private TextField txtNewStock;
    private TextArea txtReason;
    private TableView<StockCorrectionRecord> tableStockHistory;

    public static class StockCorrectionRecord {
        private int correctionId;
        private int productId;
        private double oldStock;
        private double newStock;
        private String reason;
        private String correctedBy;
        private Timestamp correctionDate;

        public StockCorrectionRecord(int correctionId, int productId, double oldStock,
                                     double newStock, String reason,
                                     String correctedBy, Timestamp correctionDate) {
            this.correctionId = correctionId;
            this.productId = productId;
            this.oldStock = oldStock;
            this.newStock = newStock;
            this.reason = reason;
            this.correctedBy = correctedBy;
            this.correctionDate = correctionDate;
        }

        public int getCorrectionId() { return correctionId; }
        public int getProductId() { return productId; }
        public double getOldStock() { return oldStock; }
        public double getNewStock() { return newStock; }
        public String getReason() { return reason; }
        public String getCorrectedBy() { return correctedBy; }
        public Timestamp getCorrectionDate() { return correctionDate; }
    }

    private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/pos_database";
            String username = "root";
            String password = "Root202122!@#";
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan Driver", "MySQL JDBC Driver tidak ditemukan");
            LOGGER.log(Level.SEVERE, "Driver database tidak ditemukan", e);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan Koneksi", "Tidak dapat terhubung ke database");
            LOGGER.log(Level.SEVERE, "Koneksi database gagal", e);
        }
    }

    private boolean findProductDetails(int productId) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT product_name, current_stock FROM Products WHERE product_id = ?"
            );
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                txtProductName.setText(rs.getString("product_name"));
                txtCurrentStock.setText(String.valueOf(rs.getDouble("current_stock")));
                return true;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan", "Tidak dapat mengambil detail produk");
            LOGGER.log(Level.SEVERE, "Gagal mengambil detail produk", e);
        }
        return false;
    }

    private boolean correctStock(int productId, double newStock, String reason) {
        if (reason.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Alasan koreksi tidak boleh kosong");
            return false;
        }
        try {
            CallableStatement stmt = connection.prepareCall("{call CorrectionStock(?, ?, ?, ?)}");
            stmt.setInt(1, productId);
            stmt.setDouble(2, newStock);
            stmt.setString(3, reason);
            stmt.setString(4, "Admin");
            stmt.execute();
            loadStockCorrectionHistory(productId);
            return true;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan", "Gagal melakukan koreksi stok");
            LOGGER.log(Level.SEVERE, "Gagal melakukan koreksi stok", e);
            return false;
        }
    }

    private void loadStockCorrectionHistory(int productId) {
        tableStockHistory.getItems().clear();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM Stock_Corrections " +
                "WHERE product_id = ? " +
                "ORDER BY correction_date DESC " +
                "LIMIT 10"
            );
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                StockCorrectionRecord record = new StockCorrectionRecord(
                    rs.getInt("correction_id"),
                    rs.getInt("product_id"),
                    rs.getDouble("old_stock"),
                    rs.getDouble("new_stock"),
                    rs.getString("correction_reason"),
                    rs.getString("corrected_by"),
                    rs.getTimestamp("correction_date")
                );
                tableStockHistory.getItems().add(record);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan", "Tidak dapat memuat riwayat koreksi");
            LOGGER.log(Level.SEVERE, "Gagal memuat riwayat koreksi", e);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void start(Stage primaryStage) {
        connectDatabase();

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));

        GridPane inputGrid = new GridPane();
        inputGrid.setVgap(10);
        inputGrid.setHgap(10);

        Label lblProductId = new Label("ID Produk:");
        txtProductId = new TextField();
        inputGrid.add(lblProductId, 0, 0);
        inputGrid.add(txtProductId, 1, 0);

        Label lblProductName = new Label("Nama Produk:");
        txtProductName = new TextField();
        txtProductName.setEditable(false);
        inputGrid.add(lblProductName, 0, 1);
        inputGrid.add(txtProductName, 1, 1);

        Label lblCurrentStock = new Label("Stok Saat Ini:");
        txtCurrentStock = new TextField();
        txtCurrentStock.setEditable(false);
        inputGrid.add(lblCurrentStock, 0, 2);
        inputGrid.add(txtCurrentStock, 1, 2);

        Label lblNewStock = new Label("Stok Baru:");
        txtNewStock = new TextField();
        inputGrid.add(lblNewStock, 0, 3);
        inputGrid.add(txtNewStock, 1, 3);

        Label lblReason = new Label("Alasan Koreksi:");
        txtReason = new TextArea();
        txtReason.setPrefRowCount(3);
        inputGrid.add(lblReason, 0, 4);
        inputGrid.add(txtReason, 1, 4);

        HBox buttonBox = new HBox(10);
        Button btnSearch = new Button("Cari Produk");
        btnSearch.setOnAction(e -> {
            try {
                int productId = Integer.parseInt(txtProductId.getText());
                findProductDetails(productId);
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Kesalahan", "Masukkan ID Produk yang valid");
            }
        });

        Button btnCorrect = new Button("Koreksi Stok");
        btnCorrect.setOnAction(e -> {
            try {
                int productId = Integer.parseInt(txtProductId.getText());
                double newStock = Double.parseDouble(txtNewStock.getText());
                String reason = txtReason.getText();

                if (correctStock(productId, newStock, reason)) {
                    showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Koreksi stok berhasil dilakukan!");
                    txtCurrentStock.setText(String.valueOf(newStock));
                    txtNewStock.clear();
                    txtReason.clear();
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Kesalahan", "Masukkan data dengan benar");
            }
        });

        buttonBox.getChildren().addAll(btnSearch, btnCorrect);
        inputGrid.add(buttonBox, 1, 5);

        Label lblHistory = new Label("Riwayat Koreksi Stok:");
        tableStockHistory = new TableView<>();

        TableColumn<StockCorrectionRecord, Integer> colCorrectionId =
            new TableColumn<>("ID Koreksi");
        colCorrectionId.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCorrectionId()).asObject());

        TableColumn<StockCorrectionRecord, Double> colOldStock =
            new TableColumn<>("Stok Lama");
        colOldStock.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getOldStock()).asObject());

        TableColumn<StockCorrectionRecord, Double> colNewStock =
            new TableColumn<>("Stok Baru");
        colNewStock.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getNewStock()).asObject());

        TableColumn<StockCorrectionRecord, String> colReason =
            new TableColumn<>("Alasan");
        colReason.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getReason()));

        TableColumn<StockCorrectionRecord, Timestamp> colDate =
            new TableColumn<>("Tanggal");
        colDate.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCorrectionDate()));

        tableStockHistory.getColumns().addAll(
            colCorrectionId, colOldStock, colNewStock, colReason, colDate
        );

        mainLayout.getChildren().addAll(
            inputGrid,
            lblHistory,
            tableStockHistory
        );

        Scene scene = new Scene(mainLayout, 600, 700);
        primaryStage.setTitle("Koreksi Stok Produk");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal menutup koneksi", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
