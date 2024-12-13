package application;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class CabangModule extends Application {
    private TableView<Cabang> cabangTable; // tabel show data cabang
    private ObservableList<Cabang> cabangData; // nyimpan data cabang
    private Connection connection; // connect mySQL

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Modul Cabang - Point of Sales");

        connectToDatabase();

        // Header label
        Label headerLabel = new Label("List Cabang");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Kolom tabel pos cabang
        cabangTable = new TableView<>();
        TableColumn<Cabang, String> kodePosColumn = new TableColumn<>("Kode Pos");
        kodePosColumn.setCellValueFactory(data -> data.getValue().kodePosProperty());
        kodePosColumn.setMinWidth(100);

        // Kolom tabel nama cabang
        TableColumn<Cabang, String> namaCabangColumn = new TableColumn<>("Nama Cabang");
        namaCabangColumn.setCellValueFactory(data -> data.getValue().namaCabangProperty());
        namaCabangColumn.setMinWidth(200);

        // Kolom tabel nomor telepon
        TableColumn<Cabang, String> nomorTeleponColumn = new TableColumn<>("Nomor Telepon");
        nomorTeleponColumn.setCellValueFactory(data -> data.getValue().nomorTeleponProperty());
        nomorTeleponColumn.setMinWidth(150);

        // Kolom tabel alamat
        TableColumn<Cabang, String> alamatColumn = new TableColumn<>("Alamat");
        alamatColumn.setCellValueFactory(data -> data.getValue().alamatProperty());
        alamatColumn.setMinWidth(300);

        cabangTable.getColumns().addAll(kodePosColumn, namaCabangColumn, nomorTeleponColumn, alamatColumn); // tambah kolom tabel

        
        cabangData = FXCollections.observableArrayList(new Cabang("10110", "Cabang Jakarta", "0856-9254-8025", 
        		"Jl. Sukasari No.1"), new Cabang("20220", "Cabang Bandung", "0821-9024-7254", "Jl. Asia Afrika No.2"));
        cabangTable.setItems(cabangData);
        loadCabangFromDatabase(); // ngeload data dari mysql ke tableview

        // Form input tambah cabang baru
        TextField kodePosInput = new TextField();
        kodePosInput.setPromptText("Kode Pos");

        TextField namaCabangInput = new TextField();
        namaCabangInput.setPromptText("Nama Cabang");

        TextField nomorTeleponInput = new TextField();
        nomorTeleponInput.setPromptText("Nomor Telepon");

        TextField alamatInput = new TextField();
        alamatInput.setPromptText("Alamat");

        Button addButton = new Button("Tambah Cabang");
        addButton.setOnAction(e -> {
            String kodePos = kodePosInput.getText();
            String namaCabang = namaCabangInput.getText();
            String nomorTelepon = nomorTeleponInput.getText();
            String alamat = alamatInput.getText();

            if (!kodePos.isEmpty() && !namaCabang.isEmpty() && !nomorTelepon.isEmpty() && !alamat.isEmpty()) {
                addCabangToDatabase(kodePos, namaCabang, nomorTelepon, alamat); 
                cabangData.add(new Cabang(kodePos, namaCabang, nomorTelepon, alamat)); // tambah ke tableview
                kodePosInput.clear();
                namaCabangInput.clear();
                nomorTeleponInput.clear();
                alamatInput.clear();
            } else {
                showAlert("Error", "Semua kolom harus diisi!");
            }
        });

        // Layout form input
        HBox inputLayout = new HBox(10);
        inputLayout.getChildren().addAll(kodePosInput, namaCabangInput, nomorTeleponInput, alamatInput, addButton);

        // Layout utama
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().addAll(headerLabel, cabangTable, inputLayout);

        // scene
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("URL, USER, PASSWORD"); // saat connect di input url, root user, password 
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to connect to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadCabangFromDatabase() {
        try {
            String query = "SELECT * FROM Cabang";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            // Eksekusi query utk ambil data cabang

            while (resultSet.next()) {
                String kodePos = resultSet.getString("kodePos");
                String namaCabang = resultSet.getString("namaCabang");
                String nomorTelepon = resultSet.getString("nomorTelepon");
                String alamat = resultSet.getString("alamat");
                cabangData.add(new Cabang(kodePos, namaCabang, nomorTelepon, alamat));  // + data hasil query ke ObservableList
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addCabangToDatabase(String kodePos, String namaCabang, String nomorTelepon, String alamat) {
        try {
            String query = "INSERT INTO Cabang (kodePos, namaCabang, nomorTelepon, alamat) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, kodePos);
            preparedStatement.setString(2, namaCabang);
            preparedStatement.setString(3, nomorTelepon);
            preparedStatement.setString(4, alamat);
            preparedStatement.executeUpdate(); // // Masukkan data baru ke database
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to add data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
        // Fungsi untuk tampilkan alert kalo user belum nambah details utk cabang
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Cabang {
    private final SimpleStringProperty kodePos;
    private final SimpleStringProperty namaCabang;
    private final SimpleStringProperty nomorTelepon;
    private final SimpleStringProperty alamat;
    // buat nyimpan data cabang

    public Cabang(String kodePos, String namaCabang, String nomorTelepon, String alamat) {
        this.kodePos = new SimpleStringProperty(kodePos);
        this.namaCabang = new SimpleStringProperty(namaCabang);
        this.nomorTelepon = new SimpleStringProperty(nomorTelepon);
        this.alamat = new SimpleStringProperty(alamat);
    }

    // akses data
    public String getKodePos() {
        return kodePos.get();
    }

    public SimpleStringProperty kodePosProperty() {
        return kodePos;
    }

    public String getNamaCabang() {
        return namaCabang.get();
    }

    public SimpleStringProperty namaCabangProperty() {
        return namaCabang;
    }

    public String getNomorTelepon() {
        return nomorTelepon.get();
    }

    public SimpleStringProperty nomorTeleponProperty() {
        return nomorTelepon;
    }

    public String getAlamat() {
        return alamat.get();
    }

    public SimpleStringProperty alamatProperty() {
        return alamat;
    }
}
