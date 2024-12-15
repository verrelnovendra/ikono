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

public class Cetak_Laporan_Penjualan_Harian extends Application {
    private DatePicker datePicker;
    private TableView<SalesReport> reportTable;
    private ObservableList<SalesReport> salesReports;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox topLayout = new HBox(10);
        topLayout.setAlignment(Pos.CENTER);
        HBox.setHgrow(topLayout, Priority.ALWAYS);

        Label dateLabel = new Label("Select Date:");
        datePicker = new DatePicker(LocalDate.now());
        Button loadButton = new Button("Load Report");
        loadButton.setOnAction(e -> loadDailyReport());

        topLayout.getChildren().addAll(dateLabel, datePicker, loadButton);
        root.setTop(topLayout);

        reportTable = new TableView<>();
        salesReports = FXCollections.observableArrayList();
        reportTable.setItems(salesReports);

        TableColumn<SalesReport, String> dateCol = new TableColumn<>("Tanggal");
        dateCol.setCellValueFactory(data -> data.getValue().dateProperty());
        dateCol.setStyle("-fx-alignment: CENTER;");

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

        TableColumn<SalesReport, String> productDescCol = new TableColumn<>("Deskripsi Produk");
        productDescCol.setCellValueFactory(data -> data.getValue().productDescriptionProperty());
        productDescCol.setStyle("-fx-alignment: CENTER;");

        reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        reportTable.getColumns().addAll(dateCol, orderNoCol, customerCol, quantityCol, totalPriceCol, paymentStatusCol, productDescCol);
        root.setCenter(reportTable);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Report Penjualan Harian");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadDailyReport() {
        salesReports.clear();

        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Pilih Tanggal Yang Valid.");
            return;
        }

        String query = "SELECT t.transaction_date AS tanggal, " +
                "t.transaction_id AS nomor_transaksi, " +
                "u.username AS nama_pembeli, " +
                "t.quantity AS kuantitas, " +
                "t.total_price AS total_harga, " +
                "t.payment_status AS status_pembayaran, " +
                "p.product_name AS deskripsi_produk " +
                "FROM transactions t " +
                "JOIN users u ON t.user_id = u.user_id " +
                "JOIN products p ON t.product_id = p.product_id " +
                "WHERE DATE(t.transaction_date) = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(selectedDate));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                salesReports.add(new SalesReport(
                        rs.getString("tanggal"),
                        rs.getString("nomor_transaksi"),
                        rs.getString("nama_pembeli"),
                        rs.getInt("kuantitas"),
                        rs.getDouble("total_harga"),
                        rs.getString("status_pembayaran"),
                        rs.getString("deskripsi_produk")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal Memuat Laporan Penjualan.");
        }
    }

    private Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/transaction", "root", "");
        } catch (SQLException e) {
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
    private final SimpleStringProperty date;
    private final SimpleStringProperty orderNo;
    private final SimpleStringProperty customer;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty totalPrice;
    private final SimpleStringProperty paymentStatus;
    private final SimpleStringProperty productDescription;

    public SalesReport(String date, String orderNo, String customer, int quantity, double totalPrice, String paymentStatus, String productDescription) {
        this.date = new SimpleStringProperty(date);
        this.orderNo = new SimpleStringProperty(orderNo);
        this.customer = new SimpleStringProperty(customer);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
        this.paymentStatus = new SimpleStringProperty(paymentStatus);
        this.productDescription = new SimpleStringProperty(productDescription);
    }

    public SimpleStringProperty dateProperty() {
        return date;
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
}
