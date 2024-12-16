package application;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.print.*;
import java.time.LocalDate;
import java.sql.*;

public class LaporanItemPalingPopuler extends Application {
    private TableView<ItemReport> reportTable;
    private ObservableList<ItemReport> itemReports;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox topLayout = new VBox(10);
        topLayout.setAlignment(Pos.CENTER);
        topLayout.setPadding(new Insets(10));

        HBox filterLayout = new HBox(10);
        filterLayout.setAlignment(Pos.CENTER);

        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");
        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");

        Button loadButton = new Button("Load Report");
        loadButton.setOnAction(e -> loadPopularItems());

        Button printButton = new Button("Print Report");
        printButton.setOnAction(e -> printReport());

        filterLayout.getChildren().addAll(new Label("Start Date:"), startDatePicker, new Label("End Date:"), endDatePicker, loadButton, printButton);
        topLayout.getChildren().add(filterLayout);
        root.setTop(topLayout);

        reportTable = new TableView<>();
        itemReports = FXCollections.observableArrayList();
        reportTable.setItems(itemReports);

        TableColumn<ItemReport, String> productNameCol = new TableColumn<>("Nama Produk");
        productNameCol.setCellValueFactory(data -> data.getValue().productNameProperty());
        productNameCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<ItemReport, Integer> quantityCol = new TableColumn<>("Jumlah Terjual");
        quantityCol.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        quantityCol.setStyle("-fx-alignment: CENTER;");

        reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        reportTable.getColumns().addAll(productNameCol, quantityCol);
        root.setCenter(reportTable);

        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle("Laporan Item Paling Populer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadPopularItems() {
        itemReports.clear();

        String query = "SELECT p.product_name AS nama_produk, " +
                "SUM(t.quantity) AS total_terjual " +
                "FROM transactions t " +
                "JOIN products p ON t.product_id = p.product_id " +
                "WHERE t.transaction_date BETWEEN ? AND ? " +
                "GROUP BY p.product_id " +
                "ORDER BY total_terjual DESC";

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please select both start and end dates.");
            return;
        }

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                itemReports.add(new ItemReport(
                        rs.getString("nama_produk"),
                        rs.getInt("total_terjual")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal Memuat Laporan Item Paling Populer.");
        }
    }

    private void printReport() {
        PrinterJob printerJob = PrinterJob.createPrinterJob();

        if (printerJob != null && printerJob.showPrintDialog(reportTable.getScene().getWindow())) {
            boolean success = printerJob.printPage(reportTable);
            if (success) {
                printerJob.endJob();
                showAlert(Alert.AlertType.INFORMATION, "Print Success", "Laporan berhasil dicetak.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Print Failed", "Gagal mencetak laporan.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Print Cancelled", "Pencetakan dibatalkan.");
        }
    }

    private Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/transaction", "root", "karv2020");
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

class ItemReport {
    private final SimpleStringProperty productName;
    private final SimpleIntegerProperty quantity;

    public ItemReport(String productName, int quantity) {
        this.productName = new SimpleStringProperty(productName);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }
}
