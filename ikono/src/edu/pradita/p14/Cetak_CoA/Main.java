package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    // Class model untuk data Chart of Accounts
    public static class Account {
        private final int accountNumber;
        private final String category;
        private final String description;
        private final double balance;

        public Account(int accountNumber, String category, String description, double balance) {
            this.accountNumber = accountNumber;
            this.category = category;
            this.description = description;
            this.balance = balance;
        }

        public int getAccountNumber() { return accountNumber; }
        public String getCategory() { return category; }
        public String getDescription() { return description; }
        public double getBalance() { return balance; }
    }

    @Override
    public void start(Stage primaryStage) {
        // Membuat TableView
        TableView<Account> tableView = new TableView<>();

        // Kolom untuk Account Number
        TableColumn<Account, Integer> colAccountNumber = new TableColumn<>("Account #");
        colAccountNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        // Kolom untuk Category
        TableColumn<Account, String> colCategory = new TableColumn<>("Category");
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Kolom untuk Description
        TableColumn<Account, String> colDescription = new TableColumn<>("Description");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Kolom untuk Balance
        TableColumn<Account, Double> colBalance = new TableColumn<>("Balance");
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));

        // Menambahkan kolom ke TableView
        tableView.getColumns().addAll(colAccountNumber, colCategory, colDescription, colBalance);

        // Data dummy untuk tabel
        ObservableList<Account> data = FXCollections.observableArrayList(
            new Account(1000, "Assets", "Cash - Checking Account", 5000),
            new Account(1010, "Liabilities", "Accounts Payable", 3000),
            new Account(1020, "Revenue", "Sales Revenue", 15000),
            new Account(1100, "Expenses", "Rent Expense", 1200),
            new Account(1200, "Equity", "Owner's Equity", 10000)
        );
        tableView.setItems(data);

        // Tombol Print
        Button printButton = new Button("Print Table");
        printButton.setOnAction(e -> printTable(tableView));

        // Layout utama
        VBox vbox = new VBox(10, tableView, printButton);
        BorderPane root = new BorderPane(vbox);

        // Scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Chart of Accounts");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Fungsi untuk mencetak tabel
    private void printTable(TableView<?> tableView) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            boolean proceed = printerJob.showPrintDialog(tableView.getScene().getWindow());
            if (proceed) {
                boolean success = printerJob.printPage(tableView);
                if (success) {
                    printerJob.endJob();
                }
            }
        } else {
            System.out.println("Printer job creation failed!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
