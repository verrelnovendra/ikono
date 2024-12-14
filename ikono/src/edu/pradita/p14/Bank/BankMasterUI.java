package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class BankMasterUI extends Application {
    private final Bank bankModule = new Bank();
    private TableView<BankAccount> tableView;

    public void resetFormValues(TextField accountNumber, TextField bankName, TextField description, TextField balance) {
        accountNumber.setText("");
        bankName.setText("");
        description.setText("");
        balance.setText("");
    }

    private void addBankAccount(TextField accountNumberField, TextField bankNameField, TextField descriptionField, TextField balanceField) {
        String accountNumber = accountNumberField.getText();
        String bankName = bankNameField.getText();
        String description = descriptionField.getText();

        try {
            double initialBalance = Double.parseDouble(balanceField.getText());

            boolean success = bankModule.createBankAccount(accountNumber, bankName, description, initialBalance);
            if (success) {
                showDialog(Alert.AlertType.INFORMATION, "Success", "Successfully created bank account.");
                tableView.getItems().add(new BankAccount(accountNumber, bankName, description, initialBalance));
                resetFormValues(accountNumberField, bankNameField, descriptionField, balanceField);
            } else {
                showDialog(Alert.AlertType.ERROR, "Error", "Failed to create bank account.");
            }
        } catch (NumberFormatException e) {
            showDialog(Alert.AlertType.ERROR, "Invalid Input", "Invalid balance. Please input a valid number.");
        }

    }

    private void showDialog(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private GridPane createForm() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label accountNumberLabel = new Label("Account Number");
        TextField accountNumberField = new TextField();
        accountNumberField.setPromptText("Account Number");

        Label bankNameLabel = new Label("Bank Name");
        TextField bankNameField = new TextField();
        bankNameField.setPromptText("Bank Name");

        Label descriptionLabel = new Label("Description");
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        Label balanceLabel = new Label("Balance");
        TextField balanceField = new TextField();
        balanceField.setPromptText("Balance");

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> addBankAccount(accountNumberField, bankNameField, descriptionField, balanceField));

        gridPane.add(accountNumberLabel, 0, 0);
        gridPane.add(accountNumberField, 1, 0);

        gridPane.add(bankNameLabel, 0, 1);
        gridPane.add(bankNameField, 1, 1);

        gridPane.add(descriptionLabel, 0, 2);
        gridPane.add(descriptionField, 1, 2);

        gridPane.add(balanceLabel, 0, 3);
        gridPane.add(balanceField, 1, 3);

        gridPane.add(addButton, 0, 4);
        return gridPane;
    }

    private TableView<BankAccount> createTableView(){
        TableView<BankAccount> table = new TableView<>();

        TableColumn<BankAccount, String> accountNumberColumn = new TableColumn<>("Account Number");
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        TableColumn<BankAccount, String> bankNameColumn = new TableColumn<>("Bank Name");
        bankNameColumn.setCellValueFactory(new PropertyValueFactory<>("bankName"));

        TableColumn<BankAccount, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<BankAccount, String> balanceColumn = new TableColumn<>("Balance");
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        table.getColumns().addAll(accountNumberColumn, bankNameColumn, descriptionColumn, balanceColumn);
        return table;
    }

    private void populateTable() {
        List<BankAccount> bankAccounts = bankModule.getBankAccounts();
        tableView.getItems().addAll(bankAccounts);
    }

    private void deleteSelectedBankAccount() {
        BankAccount selectedBankAccount = tableView.getSelectionModel().getSelectedItem();
        if (selectedBankAccount != null) {
            boolean success = bankModule.deleteBankAccount(selectedBankAccount.getAccountNumber());
            if (success) {
                tableView.getItems().remove(selectedBankAccount);
                showDialog(Alert.AlertType.INFORMATION, "Success", "Successfully deleted bank account.");
            } else {
                showDialog(Alert.AlertType.ERROR, "Failure", "Failed to delete bank account.");
            }
        } else {
            showDialog(Alert.AlertType.WARNING, "No Selection", "Please select a bank account to delete.");
        }
    }

    private void updateSelectedAccount() {
        BankAccount selectedBankAccount = tableView.getSelectionModel().getSelectedItem();
        if (selectedBankAccount != null) {
            Stage updateStage = new Stage();

            TextField accountNumberField = new TextField(selectedBankAccount.getAccountNumber());
            TextField bankNameField = new TextField(selectedBankAccount.getBankName());
            TextField descriptionField = new TextField(selectedBankAccount.getDescription());
            TextField balanceField = new TextField(String.valueOf(selectedBankAccount.getBalance()));

            Button updateButton = new Button("Confirm Update");
            updateButton.setOnAction(event -> {
                try {
                    double updatededBalance = Double.parseDouble(balanceField.getText());

                    boolean success = bankModule.updateBankAccount(selectedBankAccount.getAccountNumber(),
                            new BankAccount(accountNumberField.getText(), bankNameField.getText(), balanceField.getText(), updatededBalance)
                    );

                    if (success) {
                        selectedBankAccount.setAccountNumber(accountNumberField.getText());
                        selectedBankAccount.setBankName(bankNameField.getText());
                        selectedBankAccount.setDescription(descriptionField.getText());
                        selectedBankAccount.setBalance(Double.parseDouble(balanceField.getText()));
                        tableView.refresh();
                        showDialog(Alert.AlertType.INFORMATION, "Success", "Successfully updated bank account.");
                        updateStage.close();
                    } else {
                        showDialog(Alert.AlertType.ERROR, "Failure", "Failed to update bank account.");
                        updateStage.close();
                    }
                } catch (NumberFormatException e) {
                    showDialog(Alert.AlertType.ERROR, "Invalid Input", "Invalid balance. Please input a valid number.");
                }
            });

            VBox updatedLayout = new VBox(10, accountNumberField, bankNameField, descriptionField, balanceField, updateButton);
            updatedLayout.setPadding(new Insets(10));

            Scene updateScene = new Scene(updatedLayout);
            updateStage.setScene(updateScene);
            updateStage.show();
        } else {
            showDialog(Alert.AlertType.WARNING, "No Selection", "Please select a bank account to update.");

        }
    }

    private HBox createButtons() {
        Button deleteButton = new Button("Delete Selected Account");
        deleteButton.setOnAction(event -> deleteSelectedBankAccount());

        Button updateButton = new Button("Update Selected Account");
        updateButton.setOnAction(event -> updateSelectedAccount());

        return new HBox(10, deleteButton, updateButton);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Bank Demo");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        GridPane gridPane = createForm();

        tableView = createTableView();
        populateTable();

        HBox buttonBox = createButtons();

        root.getChildren().addAll(gridPane, tableView, buttonBox);

        // Set the scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
