  package application;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;

import java.sql.*;

public class Buyer extends Application {

    @FXML private TextField nameField;
    @FXML private TextArea addressArea;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;
    @FXML private RadioButton activeRadio;
    @FXML private RadioButton inactiveRadio;
    @FXML private ListView<String> buyerListView;

    private ToggleGroup genderGroup;
    private ToggleGroup statusGroup;
    private static final String URL = "jdbc:mysql://localhost:3306/database";
    private static final String USER = "username";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        primaryStage.setTitle("Data Pembeli");

        Label label = new Label("Data Pembeli");
        label.setFont(new Font("FreeSans", 15));
        label.setLayoutX(256.0);
        label.setLayoutY(27.0);

        Label nameLabel = new Label("Nama Lengkap : ");
        nameLabel.setLayoutX(33.0);
        nameLabel.setLayoutY(70.0);

        nameField = new TextField();
        nameField.setLayoutX(136.0);
        nameField.setLayoutY(66.0);
        nameField.setPrefWidth(265.0);
        nameField.setPrefHeight(26.0);
        nameField.setPromptText("John Doe");

        Label addressLabel = new Label("Alamat : ");
        addressLabel.setLayoutX(33.0);
        addressLabel.setLayoutY(101.0);

        addressArea = new TextArea();
        addressArea.setLayoutX(136.0);
        addressArea.setLayoutY(101.0);
        addressArea.setPrefHeight(59.0);
        addressArea.setPrefWidth(265.0);
        addressArea.setPromptText("Jl. Raya No.1");

        Label cityLabel = new Label("Kota : ");
        cityLabel.setLayoutX(407.0);
        cityLabel.setLayoutY(102.0);

        cityField = new TextField();
        cityField.setLayoutX(450.0);
        cityField.setLayoutY(99.0);
        cityField.setPrefWidth(142.0);
        cityField.setPrefHeight(26.0);
        cityField.setPromptText("Tangerang");

        Label postalCodeLabel = new Label("Kode Pos : ");
        postalCodeLabel.setLayoutX(407.0);
        postalCodeLabel.setLayoutY(140.0);

        postalCodeField = new TextField();
        postalCodeField.setLayoutX(476.0);
        postalCodeField.setLayoutY(136.0);
        postalCodeField.setPrefWidth(115.0);
        postalCodeField.setPrefHeight(26.0);
        postalCodeField.setPromptText("12345");

        Label phoneLabel = new Label("No. Telpon/Hp : ");
        phoneLabel.setLayoutX(33.0);
        phoneLabel.setLayoutY(174.0);

        phoneField = new TextField();
        phoneField.setLayoutX(136.0);
        phoneField.setLayoutY(170.0);
        phoneField.setPrefWidth(124.0);
        phoneField.setPrefHeight(26.0);
        phoneField.setPromptText("08123456789");

        Label emailLabel = new Label("Email : ");
        emailLabel.setLayoutX(269.0);
        emailLabel.setLayoutY(174.0);

        emailField = new TextField();
        emailField.setLayoutX(321.0);
        emailField.setLayoutY(170.0);
        emailField.setPrefWidth(271.0);
        emailField.setPrefHeight(26.0);
        emailField.setPromptText("johndoe@example.com");

        Label genderLabel = new Label("Jenis Kelamin : ");
        genderLabel.setLayoutX(407.0);
        genderLabel.setLayoutY(69.0);

        ToggleGroup genderGroup = new ToggleGroup();
        maleRadio = new RadioButton("L");
        maleRadio.setLayoutX(501.0);
        maleRadio.setLayoutY(69.0);
        maleRadio.setToggleGroup(genderGroup);

        femaleRadio = new RadioButton("P");
        femaleRadio.setLayoutX(540.0);
        femaleRadio.setLayoutY(69.0);
        femaleRadio.setToggleGroup(genderGroup);

        Label statusLabel = new Label("Status : ");
        statusLabel.setLayoutX(33.0);
        statusLabel.setLayoutY(208.0);

        ToggleGroup statusGroup = new ToggleGroup();
        activeRadio = new RadioButton("Aktif");
        activeRadio.setLayoutX(136.0);
        activeRadio.setLayoutY(208.0);
        activeRadio.setToggleGroup(statusGroup);

        inactiveRadio = new RadioButton("Tidak Aktif");
        inactiveRadio.setLayoutX(200.0);
        inactiveRadio.setLayoutY(208.0);
        inactiveRadio.setToggleGroup(statusGroup);

        buyerListView = new ListView<>();
        buyerListView.setLayoutX(30.0);
        buyerListView.setLayoutY(246.0);
        buyerListView.setPrefHeight(220.0);
        buyerListView.setPrefWidth(565.0);

        Button saveButton = new Button("Tambah");
        saveButton.setLayoutX(320.0);
        saveButton.setLayoutY(208.0);
        saveButton.setPrefHeight(26.0);
        saveButton.setPrefWidth(88.0);
        saveButton.setOnAction(this::saveBuyer);

        Button updateButton = new Button("Update");
        updateButton.setLayoutX(412.0);
        updateButton.setLayoutY(208.0);
        updateButton.setPrefHeight(26.0);
        updateButton.setPrefWidth(88.0);
        updateButton.setOnAction(this::updateBuyer);

        Button deleteButton = new Button("Delete");
        deleteButton.setLayoutX(504.0);
        deleteButton.setLayoutY(208.0);
        deleteButton.setPrefHeight(26.0);
        deleteButton.setPrefWidth(88.0);
        deleteButton.setOnAction(this::deleteBuyer);

        root.getChildren().addAll(
                label, nameLabel, nameField, addressLabel, addressArea, cityLabel, cityField,
                postalCodeLabel, postalCodeField, phoneLabel, phoneField, emailLabel, emailField,
                genderLabel, maleRadio, femaleRadio, statusLabel, activeRadio, inactiveRadio,
                buyerListView, saveButton, updateButton, deleteButton
        );

        Scene scene = new Scene(root, 623, 478);
        primaryStage.setScene(scene);
        primaryStage.show();

        loadBuyers();
        buyerListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String[] data = newValue.split(" - ");
                String idPembeli = data[0].split(": ")[1];
                String namaLengkap = data[1].split(": ")[1];
                String kota = data[2].split(": ")[1];
                String status = data[3].split(": ")[1];

                // Ambil data lengkap dari database
                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    String sql = "SELECT * FROM pembeli WHERE id_pembeli = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, Integer.parseInt(idPembeli));
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        nameField.setText(rs.getString("nama_lengkap"));
                        addressArea.setText(rs.getString("alamat"));
                        cityField.setText(rs.getString("kota"));
                        postalCodeField.setText(rs.getString("kode_pos"));
                        phoneField.setText(rs.getString("no_telepon"));
                        emailField.setText(rs.getString("email"));
                        if (rs.getString("jenis_kelamin").equals("L")) {
                            maleRadio.setSelected(true);
                        } else {
                            femaleRadio.setSelected(true);
                        }
                        if (rs.getString("status").equals("Aktif")) {
                            activeRadio.setSelected(true);
                        } else {
                            inactiveRadio.setSelected(false);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void deleteBuyer(ActionEvent event) {
        String selectedItem = buyerListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String[] data = selectedItem.split(" - ");
            String idPembeli = data[0].split(": ")[1];

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "DELETE FROM pembeli WHERE id_pembeli = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(idPembeli));
                stmt.executeUpdate();

                loadBuyers();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateBuyer(ActionEvent event){
            String namaLengkap = nameField.getText();
            String alamat = addressArea.getText();
            String kota = cityField.getText();
            String kodePos = postalCodeField.getText();
            String noTelepon = phoneField.getText();
            String email = emailField.getText();
            String jenisKelamin = maleRadio.isSelected() ? "L" : "P";
            String status = activeRadio.isSelected() ? "Aktif" : "Non-Aktif";


            String selectedItem = buyerListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String[] data = selectedItem.split(" - ");
                String idPembeli = data[0].split(": ")[1];

                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    String sql = "UPDATE pembeli SET nama_lengkap = ?, alamat = ?, kota = ?, kode_pos = ?, no_telepon = ?, email = ?, jenis_kelamin = ?, status = ? WHERE id_pembeli = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, namaLengkap);
                    stmt.setString(2, alamat);
                    stmt.setString(3, kota);
                    stmt.setString(4, kodePos);
                    stmt.setString(5, noTelepon);
                    stmt.setString(6, email);
                    stmt.setString(7, jenisKelamin);
                    stmt.setString(8, status);
                    stmt.setInt(9, Integer.parseInt(idPembeli));
                    stmt.executeUpdate();

                    loadBuyers();
                    clearFields();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
    }
    private void saveBuyer(ActionEvent event) {
        String namaLengkap = nameField.getText();
        String alamat = addressArea.getText();
        String kota = cityField.getText();
        String kodePos = postalCodeField.getText();
        String noTelepon = phoneField.getText();
        String email = emailField.getText();
        String jenisKelamin = maleRadio.isSelected() ? "L" : "P";
        String status = activeRadio.isSelected() ? "Aktif" : "Non-Aktif";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO pembeli (nama_lengkap, alamat, kota, kode_pos, no_telepon, email, jenis_kelamin, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, namaLengkap);
            stmt.setString(2, alamat);
            stmt.setString(3, kota);
            stmt.setString(4, kodePos);
            stmt.setString(5, noTelepon);
            stmt.setString(6, email);
            stmt.setString(7, jenisKelamin);
            stmt.setString(8, status);
            stmt.executeUpdate();

            loadBuyers();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBuyers() {
        buyerListView.getItems().clear();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id_pembeli, nama_lengkap, kota, status FROM pembeli";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String displayData = String.format("ID: %d - Nama: %s - Kota: %s - Status: %s",
                        rs.getInt("id_pembeli"),
                        rs.getString("nama_lengkap"),
                        rs.getString("kota"),
                        rs.getString("status")
                );
                buyerListView.getItems().add(displayData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
        addressArea.clear();
        cityField.clear();
        postalCodeField.clear();
        phoneField.clear();
        emailField.clear();
        maleRadio.setSelected(false);
        femaleRadio.setSelected(false);
        activeRadio.setSelected(false);
        inactiveRadio.setSelected(false);
    }
}
