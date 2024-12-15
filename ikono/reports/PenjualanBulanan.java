package edu.pradita.p14.user;

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
import java.time.YearMonth;

public class PenjualanBulanan extends Application {
    private DatePicker monthPicker;
    private TableView<SalesReport> reportTable;
    private ObservableList<SalesReport> salesReports;
    private Label totalSalesLabel;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox topLayout = new HBox(10);
        topLayout.setAlignment(Pos.CENTER);
        HBox.setHgrow(topLayout, Priority.ALWAYS);

        Label monthLabel = new Label("Select Month:");
        monthPicker = new DatePicker(LocalDate.now());
        monthPicker.setShowWeekNumbers(false);
        monthPicker.setPromptText("YYYY-MM");
        monthPicker.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? YearMonth.from(date).toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? YearMonth.parse(string).atDay(1) : null;
            }
        });

        Button loadButton = new Button("Load Report");
        loadButton.setOnAction(e -> loadMonthlyReport());

        topLayout.getChildren().addAll(monthLabel, monthPicker, loadButton);
        root.setTop(topLayout);

        reportTable = new TableView<>();
        salesReports = FXCollections.observableArrayList();
        reportTable.setItems(salesReports);

        TableColumn<SalesReport, String> orderNoCol = new TableColumn<>("Nomor Transaksi");
        orderNoCol.setCellValueFactory(data -> data.getValue().orderNoProperty());
        orderNoCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<SalesReport, String> customerCol = new TableColumn<>("Nama Pembeli");
        customerCol.setCellValueFactory(data -> data.getValue().customerProperty());
        customerCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<SalesReport, Integer> quantityCol = new TableColumn<>("Kuantitas");
        quantityCol.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        quantityCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<SalesReport, Double> totalPriceCol = new TableColumn<>("Total Harga");
        totalPriceCol.setCellValueFactory(data -> data.getValue().totalPriceProperty().asObject());
        totalPriceCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<SalesReport, String> paymentStatusCol = new TableColumn<>("Status Pembayaran");
        paymentStatusCol.setCellValueFactory(data -> data.getValue().paymentStatusProperty());
        paymentStatusCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<SalesReport, String> productDescCol = new TableColumn<>("Nama Produk");
        productDescCol.setCellValueFactory(data -> data.getValue().productDescriptionProperty());
        productDescCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<SalesReport, String> productUnitCol = new TableColumn<>("Unit");
        productUnitCol.setCellValueFactory(data -> data.getValue().productUnitProperty());
        productUnitCol.setStyle("-fx-alignment: CENTER;");

        reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        reportTable.getColumns().addAll(orderNoCol, customerCol, quantityCol, totalPriceCol, paymentStatusCol, productDescCol, productUnitCol);
        root.setCenter(reportTable);

        totalSalesLabel = new Label("Total Penjualan: Rp 0");
        totalSalesLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        HBox bottomLayout = new HBox(totalSalesLabel);
        bottomLayout.setAlignment(Pos.CENTER_RIGHT);
        bottomLayout.setPadding(new Insets(10));
        root.setBottom(bottomLayout);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Report Penjualan Bulanan");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadMonthlyReport() {
        salesReports.clear();
        totalSalesLabel.setText("Total Penjualan: Rp 0");

        LocalDate selectedDate = monthPicker.getValue();
        if (selectedDate == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Pilih Bulan Yang Valid.");
            return;
        }

        YearMonth selectedMonth = YearMonth.from(selectedDate);

        String query = "SELECT t.transaction_id AS nomor_transaksi, " +
                "u.username AS nama_pembeli, " +
                "t.quantity AS kuantitas, " +
                "t.total_price AS total_harga, " +
                "t.payment_status AS status_pembayaran, " +
                "p.product_name AS deskripsi_produk, " +
                "p.unit AS unit_produk " +
                "FROM transactions t " +
                "JOIN users u ON t.user_id = u.user_id " +
                "JOIN products p ON t.product_id = p.product_id " +
                "WHERE YEAR(t.transaction_date) = ? AND MONTH(t.transaction_date) = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, selectedMonth.getYear());
            stmt.setInt(2, selectedMonth.getMonthValue());
            ResultSet rs = stmt.executeQuery();

            double totalSales = 0;

            while (rs.next()) {
                double totalPrice = rs.getDouble("total_harga");
                totalSales += totalPrice;

                salesReports.add(new SalesReport(
                        rs.getString("nomor_transaksi"),
                        rs.getString("nama_pembeli"),
                        rs.getInt("kuantitas"),
                        totalPrice,
                        rs.getString("status_pembayaran"),
                        rs.getString("deskripsi_produk"),
                        rs.getString("unit_produk")
                ));
            }

            totalSalesLabel.setText(String.format("Total Penjualan: Rp %.2f", totalSales));

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal Memuat Laporan Penjualan.");
        }
    }

    private Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/transaction", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
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

class SalesReport {
    private final SimpleStringProperty orderNo;
    private final SimpleStringProperty customer;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty totalPrice;
    private final SimpleStringProperty paymentStatus;
    private final SimpleStringProperty productDescription;
    private final SimpleStringProperty productUnit;

    public SalesReport(String orderNo, String customer, int quantity, double totalPrice, String paymentStatus, String productDescription, String productUnit) {
        this.orderNo = new SimpleStringProperty(orderNo);
        this.customer = new SimpleStringProperty(customer);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
        this.paymentStatus = new SimpleStringProperty(paymentStatus);
        this.productDescription = new SimpleStringProperty(productDescription);
        this.productUnit = new SimpleStringProperty(productUnit);
    }

    public SimpleStringProperty orderNoProperty() {
        return orderNo;
    }

    public SimpleStringProperty customerProperty() {
        return customer;
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public SimpleDoubleProperty totalPriceProperty() {
        return totalPrice;
    }

    public SimpleStringProperty paymentStatusProperty() {
        return paymentStatus;
    }

    public SimpleStringProperty productDescriptionProperty() {
        return productDescription;
    }
    public SimpleStringProperty productUnitProperty() {
        return productUnit;
    }
}
