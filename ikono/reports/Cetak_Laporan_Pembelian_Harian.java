package report;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class Cetak_Laporan_Pembelian_Harian extends Application {
    private DatePicker datePicker;
    private TableView<PurchaseReport> reportTable;
    private ObservableList<PurchaseReport> purchaseReports;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Root container
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top layout (DatePicker and Button)
        HBox topLayout = new HBox(10);
        topLayout.setAlignment(Pos.CENTER);
        HBox.setHgrow(topLayout, Priority.ALWAYS);

        Label dateLabel = new Label("Pilih Tanggal:");
        datePicker = new DatePicker(LocalDate.now());
        Button loadButton = new Button("Load Laporan");
        loadButton.setOnAction(e -> loadDailyPurchaseReport());

        topLayout.getChildren().addAll(dateLabel, datePicker, loadButton);
        root.setTop(topLayout);

        // Table for report
        reportTable = new TableView<>();
        purchaseReports = FXCollections.observableArrayList();
        reportTable.setItems(purchaseReports);

        // Columns for report
        addColumnToTable("Tanggal", "date", 100);
        addColumnToTable("Nomor Pembelian", "orderNo", 150);
        addColumnToTable("Nama Supplier", "supplier", 150);
        addColumnToTable("Deskripsi Produk", "productDescription", 200);
        addColumnToTable("Kuantitas", "quantity", 100);
        addColumnToTable("Harga per Unit", "unitPrice", 120);
        addColumnToTable("Total Harga", "totalPrice", 120);
        addColumnToTable("Status Pembayaran", "paymentStatus", 150);

        // Set the report table to the center of root layout
        root.setCenter(reportTable);

        // Scene and stage setup
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Laporan Pembelian Harian");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addColumnToTable(String columnName, String propertyName, double width) {
        TableColumn<PurchaseReport, String> column = new TableColumn<>(columnName);
        column.setCellValueFactory(data -> data.getValue().getProperty(propertyName));
        column.setStyle("-fx-alignment: CENTER;");
        column.setPrefWidth(width);
        reportTable.getColumns().add(column);
    }

    private void loadDailyPurchaseReport() {
        purchaseReports.clear();

        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Pilih Tanggal Yang Valid.");
            return;
        }

        String query = "SELECT pt.PurchaseDate AS tanggal, " +
                "pt.PurchaseID AS nomor_pembelian, " +
                "s.SupplierName AS nama_supplier, " +
                "pd.Quantity AS kuantitas, " +
                "pd.UnitPrice AS harga_per_unit, " +
                "pd.Subtotal AS total_harga, " +
                "pt.PaymentStatus AS status_pembayaran, " +
                "p.ProductName AS deskripsi_produk " +
                "FROM PurchaseTransactions pt " +
                "JOIN Suppliers s ON pt.SupplierID = s.SupplierID " +
                "JOIN PurchaseDetails pd ON pt.PurchaseID = pd.PurchaseID " +
                "JOIN Products p ON pd.ProductID = p.ProductID " +
                "WHERE DATE(pt.PurchaseDate) = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(selectedDate));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                purchaseReports.add(new PurchaseReport(
                        rs.getString("tanggal"),
                        rs.getString("nomor_pembelian"),
                        rs.getString("nama_supplier"),
                        rs.getString("deskripsi_produk"),
                        rs.getInt("kuantitas"),
                        rs.getDouble("harga_per_unit"),
                        rs.getDouble("total_harga"),
                        rs.getString("status_pembayaran")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal Memuat Laporan Pembelian.");
        }
    }

    private Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/transaksi", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal terhubung ke database.");
            return null;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

class PurchaseReport {
    private final SimpleStringProperty date;
    private final SimpleStringProperty orderNo;
    private final SimpleStringProperty supplier;
    private final SimpleStringProperty productDescription;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty unitPrice;
    private final SimpleDoubleProperty totalPrice;
    private final SimpleStringProperty paymentStatus;

    public PurchaseReport(String date, String orderNo, String supplier, String productDescription,
                          int quantity, double unitPrice, double totalPrice, String paymentStatus) {
        this.date = new SimpleStringProperty(date);
        this.orderNo = new SimpleStringProperty(orderNo);
        this.supplier = new SimpleStringProperty(supplier);
        this.productDescription = new SimpleStringProperty(productDescription);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
        this.paymentStatus = new SimpleStringProperty(paymentStatus);
    }

    public SimpleStringProperty getProperty(String property) {
        switch (property) {
            case "date": return date;
            case "orderNo": return orderNo;
            case "supplier": return supplier;
            case "productDescription": return productDescription;
            case "paymentStatus": return paymentStatus;
            default: return null;
        }
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public SimpleDoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    public SimpleDoubleProperty totalPriceProperty() {
        return totalPrice;
    }
}
