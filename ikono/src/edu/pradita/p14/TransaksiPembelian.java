import java.awt.Button;
import java.awt.Label;
import java.awt.TextField;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableColumn;

public class transaksiPembelian extends Application {

    // TableView untuk menampilkan data
    private TableView<PurchaseTransactionWithDetails> table;
    private ObservableList<PurchaseTransactionWithDetails> data;

    // Input Fields
    private TextField purchaseDateField, totalAmountField, paymentStatusField, paymentMethodField, vendorIDField;
    private TextField productIDField, quantityField, unitPriceField, subtotalField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Inisialisasi TableView
        table = new TableView<>();
        data = FXCollections.observableArrayList();
        table.setItems(data);

        // Kolom untuk PurchaseTransaction
        TableColumn<PurchaseTransactionWithDetails, Integer> purchaseIDColumn = new TableColumn<>("Purchase ID");
        purchaseIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPurchaseID()).asObject());

        TableColumn<PurchaseTransactionWithDetails, String> purchaseDateColumn = new TableColumn<>("Purchase Date");
        purchaseDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurchaseDate()));

        TableColumn<PurchaseTransactionWithDetails, Double> totalAmountColumn = new TableColumn<>("Total Amount");
        totalAmountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalAmount()).asObject());

        TableColumn<PurchaseTransactionWithDetails, String> paymentStatusColumn = new TableColumn<>("Payment Status");
        paymentStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentStatus()));

        TableColumn<PurchaseTransactionWithDetails, String> paymentMethodColumn = new TableColumn<>("Payment Method");
        paymentMethodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentMethod()));

        // Kolom untuk PurchaseDetail
        TableColumn<PurchaseTransactionWithDetails, Integer> vendorIDColumn = new TableColumn<>("Vendor ID");
        vendorIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getVendorID()).asObject());

        TableColumn<PurchaseTransactionWithDetails, Integer> productIDColumn = new TableColumn<>("Product ID");
        productIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProductID()).asObject());

        TableColumn<PurchaseTransactionWithDetails, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        TableColumn<PurchaseTransactionWithDetails, Double> unitPriceColumn = new TableColumn<>("Unit Price");
        unitPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getUnitPrice()).asObject());

        TableColumn<PurchaseTransactionWithDetails, Double> subtotalColumn = new TableColumn<>("Subtotal");
        subtotalColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSubtotal()).asObject());

        // Menambahkan kolom ke TableView
        table.getColumns().addAll(purchaseIDColumn, purchaseDateColumn, totalAmountColumn, paymentStatusColumn, 
                                  paymentMethodColumn, vendorIDColumn, productIDColumn, quantityColumn, 
                                  unitPriceColumn, subtotalColumn);

        // Input Fields untuk PurchaseTransaction dan PurchaseDetail
        purchaseDateField = new TextField();
        totalAmountField = new TextField();
        paymentStatusField = new TextField();
        paymentMethodField = new TextField();
        vendorIDField = new TextField();

        productIDField = new TextField();
        quantityField = new TextField();
        unitPriceField = new TextField();
        subtotalField = new TextField();

        // Tombol untuk menambah data
        Button addButton = new Button("Add Purchase Transaction");
        addButton.setOnAction(e -> addTransactionWithDetails());

        // Layout untuk tampilan GUI
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(table, 
                                  new Label("Purchase Date:"), purchaseDateField, 
                                  new Label("Total Amount:"), totalAmountField,
                                  new Label("Payment Status:"), paymentStatusField, 
                                  new Label("Payment Method:"), paymentMethodField,
                                  new Label("Vendor ID:"), vendorIDField,
                                  new Label("Product ID:"), productIDField, 
                                  new Label("Quantity:"), quantityField,
                                  new Label("Unit Price:"), unitPriceField,
                                  new Label("Subtotal:"), subtotalField,
                                  addButton);

        // Scene dan Stage
        Scene scene = new Scene(vbox, 800, 600);
        stage.setTitle("Purchase Transactions with Details");
        stage.setScene(scene);
        stage.show();

        // Load data dari database ke TableView
        loadData();
    }

    // Mengambil data dari database dan menambahkannya ke TableView
    private void loadData() {
        String sql = "SELECT pt.PurchaseID, pt.PurchaseDate, pt.TotalAmount, pt.PaymentStatus, pt.PaymentMethod, pt.vendor_id, " +
                     "pd.PurchaseDetailID, pd.ProductID, pd.Quantity, pd.UnitPrice, pd.Subtotal " +
                     "FROM PurchaseTransactions pt " +
                     "JOIN PurchaseDetails pd ON pt.PurchaseID = pd.PurchaseID";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int purchaseID = rs.getInt("PurchaseID");
                String purchaseDate = rs.getString("PurchaseDate");
                double totalAmount = rs.getDouble("TotalAmount");
                String paymentStatus = rs.getString("PaymentStatus");
                String paymentMethod = rs.getString("PaymentMethod");
                int vendorID = rs.getInt("vendor_id");

                int purchaseDetailID = rs.getInt("PurchaseDetailID");
                int productID = rs.getInt("ProductID");
                int quantity = rs.getInt("Quantity");
                double unitPrice = rs.getDouble("UnitPrice");
                double subtotal = rs.getDouble("Subtotal");

                // Menambahkan data ke ObservableList
                data.add(new PurchaseTransactionWithDetails(purchaseID, purchaseDate, totalAmount, paymentStatus,
                        paymentMethod, vendorID, purchaseDetailID, productID, quantity, unitPrice, subtotal));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Menambahkan data transaksi pembelian dan detail ke database
    private void addTransactionWithDetails() {
        String purchaseDate = purchaseDateField.getText();
        double totalAmount = Double.parseDouble(totalAmountField.getText());
        String paymentStatus = paymentStatusField.getText();
        String paymentMethod = paymentMethodField.getText();
        int vendorID = Integer.parseInt(vendorIDField.getText());

        int productID = Integer.parseInt(productIDField.getText());
        int quantity = Integer.parseInt(quantityField.getText());
        double unitPrice = Double.parseDouble(unitPriceField.getText());
        double subtotal = Double.parseDouble(subtotalField.getText());

        // Insert PurchaseTransaction
        String insertPurchaseTransaction = "INSERT INTO PurchaseTransactions (PurchaseDate, TotalAmount, PaymentStatus, PaymentMethod, vendor_id) " +
                                           "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(insertPurchaseTransaction, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, purchaseDate);
            stmt.setDouble(2, totalAmount);
            stmt.setString(3, paymentStatus);
            stmt.setString(4, paymentMethod);
            stmt.setInt(5, vendorID);
            stmt.executeUpdate();

            // Get the generated PurchaseID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int purchaseID = rs.getInt(1);

                // Insert PurchaseDetails
                String insertPurchaseDetails = "INSERT INTO PurchaseDetails (PurchaseID, ProductID, Quantity, UnitPrice, Subtotal) " +
                                               "VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmtDetail = conn.prepareStatement(insertPurchaseDetails)) {
                    stmtDetail.setInt(1, purchaseID);
                    stmtDetail.setInt(2, productID);
                    stmtDetail.setInt(3, quantity);
                    stmtDetail.setDouble(4, unitPrice);
                    stmtDetail.setDouble(5, subtotal);
                    stmtDetail.executeUpdate();

                    // Reload data
                    data.clear();
                    loadData();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    
