package application;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class apaya extends Application {

    private TableView<Karyawan> tableView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Master Data Karyawan");

        // Table Columns
        TableColumn<Karyawan, Integer> colId = new TableColumn<>("ID Karyawan");
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colId.setMinWidth(100);

        TableColumn<Karyawan, String> colNama = new TableColumn<>("Nama");
        colNama.setCellValueFactory(data -> data.getValue().namaProperty());
        colNama.setMinWidth(150);

        TableColumn<Karyawan, String> colTanggalLahir = new TableColumn<>("Tanggal Lahir");
        colTanggalLahir.setCellValueFactory(data -> data.getValue().tanggalLahirProperty());
        colTanggalLahir.setMinWidth(120);

        TableColumn<Karyawan, String> colJenisKelamin = new TableColumn<>("Jenis Kelamin");
        colJenisKelamin.setCellValueFactory(data -> data.getValue().jenisKelaminProperty());
        colJenisKelamin.setMinWidth(100);

        TableColumn<Karyawan, String> colJabatan = new TableColumn<>("Jabatan");
        colJabatan.setCellValueFactory(data -> data.getValue().jabatanProperty());
        colJabatan.setMinWidth(150);

        TableColumn<Karyawan, String> colTanggalMasuk = new TableColumn<>("Tanggal Masuk");
        colTanggalMasuk.setCellValueFactory(data -> data.getValue().tanggalMasukProperty());
        colTanggalMasuk.setMinWidth(120);

        TableColumn<Karyawan, Double> colGaji = new TableColumn<>("Gaji");
        colGaji.setCellValueFactory(data -> data.getValue().gajiProperty().asObject());
        colGaji.setMinWidth(100);

        // TableView setup
        tableView = new TableView<>();
        tableView.getColumns().addAll(colId, colNama, colTanggalLahir, colJenisKelamin, colJabatan, colTanggalMasuk, colGaji);

        // Load data from hardcoded values
        ObservableList<Karyawan> data = loadData();
        tableView.setItems(data);

        // Layout
        VBox vbox = new VBox(10, tableView);
        vbox.setPadding(new Insets(10));

        // Scene setup
        Scene scene = new Scene(vbox, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ObservableList<Karyawan> loadData() {
        ObservableList<Karyawan> data = FXCollections.observableArrayList();
        data.add(new Karyawan(1, "Andi", "1990-01-15", "Laki-Laki", "Manager", "2015-03-20", 10000000));
        data.add(new Karyawan(2, "Siti", "1985-05-10", "Perempuan", "HR", "2016-07-25", 8000000));
        data.add(new Karyawan(3, "Budi", "1993-08-21", "Laki-Laki", "Developer", "2018-01-10", 7000000));
        data.add(new Karyawan(4, "Ayu", "1995-11-30", "Perempuan", "Designer", "2020-06-15", 6500000));
        return data;
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Kelas Model Karyawan
    public static class Karyawan {
        private final IntegerProperty id;
        private final StringProperty nama;
        private final StringProperty tanggalLahir;
        private final StringProperty jenisKelamin;
        private final StringProperty jabatan;
        private final StringProperty tanggalMasuk;
        private final DoubleProperty gaji;

        public Karyawan(int id, String nama, String tanggalLahir, String jenisKelamin, String jabatan, String tanggalMasuk, double gaji) {
            this.id = new SimpleIntegerProperty(id);
            this.nama = new SimpleStringProperty(nama);
            this.tanggalLahir = new SimpleStringProperty(tanggalLahir);
            this.jenisKelamin = new SimpleStringProperty(jenisKelamin);
            this.jabatan = new SimpleStringProperty(jabatan);
            this.tanggalMasuk = new SimpleStringProperty(tanggalMasuk);
            this.gaji = new SimpleDoubleProperty(gaji);
        }

        public IntegerProperty idProperty() {
            return id;
        }

        public StringProperty namaProperty() {
            return nama;
        }

        public StringProperty tanggalLahirProperty() {
            return tanggalLahir;
        }

        public StringProperty jenisKelaminProperty() {
            return jenisKelamin;
        }

        public StringProperty jabatanProperty() {
            return jabatan;
        }

        public StringProperty tanggalMasukProperty() {
            return tanggalMasuk;
        }

        public DoubleProperty gajiProperty() {
            return gaji;
        }
    }
}
