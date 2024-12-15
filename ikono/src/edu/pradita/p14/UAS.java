package application;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UAS extends Application {

    private ObservableList<Pasien> data = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Master Data Pasien");

        // Form Input
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        // Fields
        TextField idField = new TextField();
        TextField namaField = new TextField();
        DatePicker tanggalLahirField = new DatePicker();
        ComboBox<String> jenisKelaminField = new ComboBox<>();
        jenisKelaminField.getItems().addAll("Laki-laki", "Perempuan");
        TextField alamatField = new TextField();
        TextField teleponField = new TextField();
        TextField emailField = new TextField();
        ComboBox<String> golonganDarahField = new ComboBox<>();
        golonganDarahField.getItems().addAll("A", "B", "AB", "O");
        ComboBox<String> statusPernikahanField = new ComboBox<>();
        statusPernikahanField.getItems().addAll("Lajang", "Menikah", "Duda", "Janda");
        TextField riwayatPenyakitField = new TextField();
        TextField alergiField = new TextField();
        DatePicker tanggalRegistrasiField = new DatePicker();

        // Adding fields to form
        form.add(new Label("ID Pasien:"), 0, 0);
        form.add(idField, 1, 0);
        form.add(new Label("Nama Lengkap:"), 0, 1);
        form.add(namaField, 1, 1);
        form.add(new Label("Tanggal Lahir:"), 0, 2);
        form.add(tanggalLahirField, 1, 2);
        form.add(new Label("Jenis Kelamin:"), 0, 3);
        form.add(jenisKelaminField, 1, 3);
        form.add(new Label("Alamat:"), 0, 4);
        form.add(alamatField, 1, 4);
        form.add(new Label("Nomor Telepon:"), 0, 5);
        form.add(teleponField, 1, 5);
        form.add(new Label("Email:"), 0, 6);
        form.add(emailField, 1, 6);
        form.add(new Label("Golongan Darah:"), 0, 7);
        form.add(golonganDarahField, 1, 7);
        form.add(new Label("Status Pernikahan:"), 0, 8);
        form.add(statusPernikahanField, 1, 8);
        form.add(new Label("Riwayat Penyakit:"), 0, 9);
        form.add(riwayatPenyakitField, 1, 9);
        form.add(new Label("Alergi:"), 0, 10);
        form.add(alergiField, 1, 10);
        form.add(new Label("Tanggal Registrasi:"), 0, 11);
        form.add(tanggalRegistrasiField, 1, 11);

        // Table
        TableView<Pasien> table = new TableView<>();
        table.setItems(data);
        table.getColumns().addAll(
            createColumn("ID", "id"),
            createColumn("Nama", "nama"),
            createColumn("Tanggal Lahir", "tanggalLahir"),
            createColumn("Jenis Kelamin", "jenisKelamin"),
            createColumn("Alamat", "alamat"),
            createColumn("Telepon", "telepon"),
            createColumn("Email", "email"),
            createColumn("Golongan Darah", "golonganDarah"),
            createColumn("Status Pernikahan", "statusPernikahan"),
            createColumn("Riwayat Penyakit", "riwayatPenyakit"),
            createColumn("Alergi", "alergi"),
            createColumn("Tanggal Registrasi", "tanggalRegistrasi")
        );

        // Add Button
        Button addButton = new Button("Tambah");
        addButton.setOnAction(e -> {
            data.add(new Pasien(
                idField.getText(),
                namaField.getText(),
                tanggalLahirField.getValue() != null ? tanggalLahirField.getValue().toString() : "",
                jenisKelaminField.getValue(),
                alamatField.getText(),
                teleponField.getText(),
                emailField.getText(),
                golonganDarahField.getValue(),
                statusPernikahanField.getValue(),
                riwayatPenyakitField.getText(),
                alergiField.getText(),
                tanggalRegistrasiField.getValue() != null ? tanggalRegistrasiField.getValue().toString() : ""
            ));
            idField.clear();
            namaField.clear();
            tanggalLahirField.setValue(null);
            jenisKelaminField.setValue(null);
            alamatField.clear();
            teleponField.clear();
            emailField.clear();
            golonganDarahField.setValue(null);
            statusPernikahanField.setValue(null);
            riwayatPenyakitField.clear();
            alergiField.clear();
            tanggalRegistrasiField.setValue(null);
        });

        VBox layout = new VBox(10, form, addButton, table);
        layout.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(layout, 800, 600));
        primaryStage.show();
    }

    private TableColumn<Pasien, String> createColumn(String title, String property) {
        TableColumn<Pasien, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getProperty(property)
        ));
        return column;
    }

    public static class Pasien {
        private final SimpleStringProperty id, nama, tanggalLahir, jenisKelamin, alamat, telepon, email,
                golonganDarah, statusPernikahan, riwayatPenyakit, alergi, tanggalRegistrasi;

        public Pasien(String id, String nama, String tanggalLahir, String jenisKelamin, String alamat, String telepon, 
                      String email, String golonganDarah, String statusPernikahan, String riwayatPenyakit, 
                      String alergi, String tanggalRegistrasi) {
            this.id = new SimpleStringProperty(id);
            this.nama = new SimpleStringProperty(nama);
            this.tanggalLahir = new SimpleStringProperty(tanggalLahir);
            this.jenisKelamin = new SimpleStringProperty(jenisKelamin);
            this.alamat = new SimpleStringProperty(alamat);
            this.telepon = new SimpleStringProperty(telepon);
            this.email = new SimpleStringProperty(email);
            this.golonganDarah = new SimpleStringProperty(golonganDarah);
            this.statusPernikahan = new SimpleStringProperty(statusPernikahan);
            this.riwayatPenyakit = new SimpleStringProperty(riwayatPenyakit);
            this.alergi = new SimpleStringProperty(alergi);
            this.tanggalRegistrasi = new SimpleStringProperty(tanggalRegistrasi);
        }

        public String getProperty(String propertyName) {
            switch (propertyName) {
                case "id": return id.get();
                case "nama": return nama.get();
                case "tanggalLahir": return tanggalLahir.get();
                case "jenisKelamin": return jenisKelamin.get();
                case "alamat": return alamat.get();
                case "telepon": return telepon.get();
                case "email": return email.get();
                case "golonganDarah": return golonganDarah.get();
                case "statusPernikahan": return statusPernikahan.get();
                case "riwayatPenyakit": return riwayatPenyakit.get();
                case "alergi": return alergi.get();
            }
			return propertyName;
        }
    }
}
