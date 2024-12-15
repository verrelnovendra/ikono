package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chart of Accounts");

        // --- HEADER SECTION ---
        // Nama Perusahaan
        Text companyName = new Text("Pradita University");
        companyName.setFont(Font.font("Arial", 24));
        companyName.setStyle("-fx-font-weight: bold; -fx-fill: #2A4D69;");

        // HBox untuk Header
        HBox headerBox = new HBox(10, companyName);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(10));
        headerBox.setStyle("-fx-background-color: #E4F1FE;");

        // --- TABLE SECTION ---
        TableView<ChartOfAccount> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ChartOfAccount, Integer> accountNumberCol = new TableColumn<>("Account Number");
        accountNumberCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        TableColumn<ChartOfAccount, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<ChartOfAccount, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<ChartOfAccount, Double> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        table.getColumns().addAll(accountNumberCol, categoryCol, descriptionCol, balanceCol);

        // Add Dummy Data
        table.getItems().addAll(
                new ChartOfAccount(1000, "Assets", "Cash - Checking Account", 5000.0),
                new ChartOfAccount(1010, "Liabilities", "Accounts Payable", 3000.0),
                new ChartOfAccount(1020, "Revenue", "Sales Revenue", 15000.0),
                new ChartOfAccount(1100, "Expenses", "Rent Expense", 1200.0),
                new ChartOfAccount(1200, "Equity", "Owner's Equity", 10000.0)
        );

        // --- BUTTON SECTION ---
        Button printButton = new Button("Print Table");
        printButton.setStyle("-fx-background-color: #2E8B57; -fx-text-fill: white; -fx-font-weight: bold;");
        printButton.setOnAction(e -> printTable(table));

        // HBox untuk memposisikan tombol print ke kanan
        HBox buttonBox = new HBox(printButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10));

        // --- ROOT LAYOUT ---
        VBox root = new VBox(10, headerBox, table, buttonBox);
        root.setStyle("-fx-background-color: #F9F9F9;");
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- PRINT FUNCTION ---
    private void printTable(TableView<?> table) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            boolean success = job.printPage(table);
            if (success) {
                job.endJob();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // --- DATA MODEL CLASS ---
    public static class ChartOfAccount {
        private final int accountNumber;
        private final String category;
        private final String description;
        private final Double balance;

        public ChartOfAccount(int accountNumber, String category, String description, Double balance) {
            this.accountNumber = accountNumber;
            this.category = category;
            this.description = description;
            this.balance = balance;
        }

        public int getAccountNumber() {
            return accountNumber;
        }

        public String getCategory() {
            return category;
        }

        public String getDescription() {
            return description;
        }

        public Double getBalance() {
            return balance;
        }
    }
}
