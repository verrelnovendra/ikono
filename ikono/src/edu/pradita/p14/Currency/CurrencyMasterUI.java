package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;

public class CurrencyMasterUI extends Application {

    @FXML private TextField curIDField;
    @FXML private TextField curNameField;
    @FXML private TextField curSymbolField;
    @FXML private TextField curCountryField;
    @FXML private TextField curExcRateField;
    @FXML private TextField decimalPlacesField;
    @FXML private RadioButton activeRadio;
    @FXML private RadioButton inactiveRadio;
    @FXML private ListView<String> currencyListView;

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        primaryStage.setTitle("Master Data Mata Uang");

        Label label = new Label("Master Data Mata Uang");
        label.setFont(new Font("Arial", 20));
        label.setLayoutX(200);
        label.setLayoutY(10);

        Label curIDLabel = new Label("Currency ID:");
        curIDLabel.setLayoutX(20);
        curIDLabel.setLayoutY(50);
        curIDField = new TextField();
        curIDField.setLayoutX(130);
        curIDField.setLayoutY(50);

        Label curNameLabel = new Label("Currency Name:");
        curNameLabel.setLayoutX(20);
        curNameLabel.setLayoutY(90);
        curNameField = new TextField();
        curNameField.setLayoutX(130);
        curNameField.setLayoutY(90);

        Label curSymbolLabel = new Label("Currency Symbol:");
        curSymbolLabel.setLayoutX(20);
        curSymbolLabel.setLayoutY(130);
        curSymbolField = new TextField();
        curSymbolField.setLayoutX(130);
        curSymbolField.setLayoutY(130);

        Label curCountryLabel = new Label("Currency Country:");
        curCountryLabel.setLayoutX(20);
        curCountryLabel.setLayoutY(170);
        curCountryField = new TextField();
        curCountryField.setLayoutX(130);
        curCountryField.setLayoutY(170);

        Label curExcRateLabel = new Label("Exchange Rate:");
        curExcRateLabel.setLayoutX(20);
        curExcRateLabel.setLayoutY(210);
        curExcRateField = new TextField();
        curExcRateField.setLayoutX(130);
        curExcRateField.setLayoutY(210);

        Label decimalPlacesLabel = new Label("Decimal Places:");
        decimalPlacesLabel.setLayoutX(20);
        decimalPlacesLabel.setLayoutY(250);
        decimalPlacesField = new TextField();
        decimalPlacesField.setLayoutX(130);
        decimalPlacesField.setLayoutY(250);

        Label statusLabel = new Label("Status:");
        statusLabel.setLayoutX(20);
        statusLabel.setLayoutY(290);
        activeRadio = new RadioButton("Active");
        activeRadio.setLayoutX(130);
        activeRadio.setLayoutY(290);
        inactiveRadio = new RadioButton("Inactive");
        inactiveRadio.setLayoutX(200);
        inactiveRadio.setLayoutY(290);
        ToggleGroup statusGroup = new ToggleGroup();
        activeRadio.setToggleGroup(statusGroup);
        inactiveRadio.setToggleGroup(statusGroup);

        currencyListView = new ListView<>();
        currencyListView.setLayoutX(20);
        currencyListView.setLayoutY(330);
        currencyListView.setPrefSize(500, 200);

        Button addButton = new Button("Add");
        addButton.setLayoutX(130);
        addButton.setLayoutY(550);
        addButton.setOnAction(this::addCurrency);

        Button deleteButton = new Button("Delete");
        deleteButton.setLayoutX(200);
        deleteButton.setLayoutY(550);
        deleteButton.setOnAction(this::deleteCurrency);

        Button updateButton = new Button("Update");
        updateButton.setLayoutX(280);
        updateButton.setLayoutY(550);
        updateButton.setOnAction(this::updateCurrency);

        root.getChildren().addAll(label, curIDLabel, curIDField, curNameLabel, curNameField,
                                  curSymbolLabel, curSymbolField, curCountryLabel, curCountryField,
                                  curExcRateLabel, curExcRateField, decimalPlacesLabel, decimalPlacesField,
                                  statusLabel, activeRadio, inactiveRadio, currencyListView,
                                  addButton, deleteButton, updateButton);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        loadCurrencies();
    }

    private void loadCurrencies() {
        currencyListView.getItems().clear();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM currencies";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String data = String.format("ID: %s, Name: %s, Country: %s, Status: %s",
                        rs.getString("CurID"),
                        rs.getString("CurName"),
                        rs.getString("CurCountry"),
                        rs.getString("CurStatus"));
                currencyListView.getItems().add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCurrency(ActionEvent event) {
        String curID = curIDField.getText();
        String curName = curNameField.getText();
        String curSymbol = curSymbolField.getText();
        String curCountry = curCountryField.getText();
        String curExcRate = curExcRateField.getText();
        String decimalPlaces = decimalPlacesField.getText();
        String status = activeRadio.isSelected() ? "active" : "inactive";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO currencies (CurID, CurName, CurSymbol, CurCountry, CurExcRate, CurStatus, decimal_places) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, curID);
            stmt.setString(2, curName);
            stmt.setString(3, curSymbol);
            stmt.setString(4, curCountry);
            stmt.setBigDecimal(5, new java.math.BigDecimal(curExcRate));
            stmt.setString(6, status);
            stmt.setInt(7, Integer.parseInt(decimalPlaces));
            stmt.executeUpdate();
            loadCurrencies();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteCurrency(ActionEvent event) {
        String selectedItem = currencyListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String curID = selectedItem.split(",")[0].split(":")[1].trim();
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "DELETE FROM currencies WHERE CurID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, curID);
                stmt.executeUpdate();
                loadCurrencies();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateCurrency(ActionEvent event) {
    }
}
