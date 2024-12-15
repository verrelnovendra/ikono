package edu.pradita.p14.piutang;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class PelunasanPiutangApp extends Application {

    private static final String DB_URL = "jdbc:mysql://0.tcp.ap.ngrok.io:10725/pradita";
    private static final String DB_USER = "jaki";
    private static final String DB_PASSWORD = "jaki123Z!";

    private Connection connectToDatabase() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error: ");
        	alert.setHeaderText("Koneksi SQL gagal.");
        	alert.setContentText("Tolong ubah DB_URL, DB_USER, dan DB_PASSWORD di class PelunasanPiutangApp dan pastikan bahwa koneksi tersebut aktif.");
        	alert.setHeight(200);;
        	e.printStackTrace();
        	alert.showAndWait();
            System.exit(0);
            return null;
        }
    }

    @Override
    public void start(Stage primaryStage) {
    	try(Connection connect = connectToDatabase()){
	    	primaryStage.setTitle("Aplikasi Pelunasan Piutang");
	
	        VBox root = new VBox(10);
	        root.setPadding(new Insets(15));
	
	        Label menuLabel = new Label("Menu:");
	        ListView<String> menuList = new ListView<>();
	        menuList.setItems(FXCollections.observableArrayList(
	            "Tambah Piutang",
	            "Tambah Pelunasan",
	            "Lihat Pelanggan",
	            "Lihat Piutang",
	            "Lihat Pelunasan",
	            "Hapus Piutang",
	            "Keluar"
	        ));
	
	        Button executeButton = new Button("Pilih");
	        executeButton.setOnAction(e -> {
				try {
					handleMenuSelection(menuList.getSelectionModel().getSelectedIndex() + 1);
				} catch (SQLException e1) {
		        	e1.printStackTrace();
				}
			});
	
	        root.getChildren().addAll(menuLabel, menuList, executeButton);
	
	        primaryStage.setScene(new Scene(root, 400, 265));
	        primaryStage.show();
    	} catch (SQLException e1) {
        	e1.printStackTrace();
		}
        
    }

    private void handleMenuSelection(int option) throws SQLException {
        switch (option) {
            case 1:
                showTambahPiutangGUI();
                break;
            case 2:
                showTambahPelunasanGUI();
                break;
            case 3:
                showLihatSemuaPelangganGUI();
                break;
            case 4:
                showLihatSemuaPiutangGUI();
                break;
            case 5:
                showLihatSemuaPelunasanGUI();
                break;
            case 6:
                showHapusDataGUI();
                break;
            case 7:
                System.exit(0);
                break;
            default:
                Alert alert = new Alert(Alert.AlertType.WARNING, "Pilihan tidak valid.");
                alert.show();
        }
    }

    private void showTambahPiutangGUI() {
        Stage stage = new Stage();
        stage.setTitle("Tambah Piutang");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField idPelanggan = new TextField();
        idPelanggan.setPromptText("ID Pelanggan");

        TextField jumlahField = new TextField();
        jumlahField.setPromptText("Jumlah Piutang");
        
        TextField dueField = new TextField();
        dueField.setPromptText("Due Date (YYY-MM-DD)");
        
        Button submitButton = new Button("Simpan");
        submitButton.setOnAction(e -> {
            try (Connection conn = connectToDatabase()) {
                String sql = "INSERT INTO piutang (id_pelanggan, jumlah, tanggal_pinjam, due_date) VALUES (?, ?, NOW(), ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(idPelanggan.getText()));
                pstmt.setDouble(2, Double.parseDouble(jumlahField.getText()));
                pstmt.setDate(3, Date.valueOf(dueField.getText()));
                pstmt.executeUpdate();
            } catch (SQLException ex) {
            	Alert alert = new Alert(AlertType.ERROR);
            	alert.setTitle("Error Dialog");
            	alert.setHeaderText("Ooops, sepertinya ada error!");
            	alert.setContentText("Pastikan ID pelanggan dimasukkan dengan benar.");
            	ex.printStackTrace();
            	alert.showAndWait();
            }
            stage.close();
        });

        root.getChildren().addAll(new Label("Tambah Piutang"), idPelanggan, jumlahField, dueField, submitButton);

        stage.setScene(new Scene(root, 300, 200));
        stage.show();
    }

    private void showTambahPelunasanGUI() {
        Stage stage = new Stage();
        stage.setTitle("Tambah Pelunasan");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField idPiutangField = new TextField();
        idPiutangField.setPromptText("ID Piutang");

        TextField jumlahBayarField = new TextField();
        jumlahBayarField.setPromptText("Jumlah Bayar");

        Button submitButton = new Button("Simpan");
        submitButton.setOnAction(e -> {
            try (Connection conn = connectToDatabase()) {
                String sql = "INSERT INTO pelunasan (id_piutang, jumlah_bayar, tanggal_bayar) VALUES (?, ?, NOW())";
                String updatePiutang = "UPDATE piutang SET status_lunas = TRUE WHERE id_piutang = ?";
                String updatePiutang2 = "UPDATE piutang SET jumlah = (jumlah - ? ) WHERE id_piutang = ?";
                String slect = "SELECT * FROM piutang WHERE id_piutang = ?";
                
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(idPiutangField.getText()));
                pstmt.setDouble(2, Double.parseDouble(jumlahBayarField.getText()));
                pstmt.executeUpdate();
                
                PreparedStatement pstmt2 = conn.prepareStatement(updatePiutang2);
                pstmt2.setInt(1, Integer.parseInt(jumlahBayarField.getText()));
                pstmt2.setInt(2, Integer.parseInt(idPiutangField.getText()));
                pstmt2.executeUpdate();
                
                PreparedStatement pstmt3 = conn.prepareStatement(slect);
                pstmt3.setInt(1, Integer.parseInt(idPiutangField.getText()));
                ResultSet resultS = pstmt3.executeQuery();
                while(resultS.next()) {
                	if(resultS.getDouble("jumlah") <= 0) {
                		PreparedStatement pstmt4  = conn.prepareStatement(updatePiutang);
                		pstmt4.setInt(1, Integer.parseInt(idPiutangField.getText()));
                		pstmt4.executeUpdate();
                	}
                }
                
            } catch (SQLException ex) {
            	Alert alert = new Alert(AlertType.ERROR);
            	alert.setTitle("Error Dialog");
            	alert.setHeaderText("Ooops, sepertinya ada error!");
            	alert.setContentText("Pastikan ID piutang dimasukkan dengan benar.");
            	ex.printStackTrace();
            	alert.showAndWait();
            }
            stage.close();
        });

        root.getChildren().addAll(new Label("Tambah Pelunasan"), idPiutangField, jumlahBayarField, submitButton);

        stage.setScene(new Scene(root, 300, 200));
        stage.show();
    }

    private void showLihatSemuaPelangganGUI() throws SQLException {
        Stage stage = new Stage();
        stage.setTitle("Lihat Semua Pelanggan");

        TableView<Pembeli> table = new TableView<>();
        TableColumn<Pembeli, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Pembeli, String> namaColumn = new TableColumn<>("Nama Lengkap");
        TableColumn<Pembeli, String> alamatColumn = new TableColumn<>("Alamat");
        TableColumn<Pembeli, String> kotaColumn = new TableColumn<>("Kota");
        TableColumn<Pembeli, String> kodePosColumn = new TableColumn<>("Kode Pos");
        TableColumn<Pembeli, String> noTelpColumn = new TableColumn<>("Nomor Telepon");
        TableColumn<Pembeli, String> emailColumn = new TableColumn<>("Email");
        TableColumn<Pembeli, String> jenisKelaminColumn = new TableColumn<>("Jenis Kelamin");
        TableColumn<Pembeli, String> tanggalDaftarColumn = new TableColumn<>("Tanggal Daftar");
        TableColumn<Pembeli, String> statusColumn = new TableColumn<>("Status");

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPembeli()).asObject());
        namaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamaLengkap()));
        alamatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlamat()));
        kotaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKota()));
        kodePosColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKodePos()));
        noTelpColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNoTelpon()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        jenisKelaminColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJenisKelamin()));
        tanggalDaftarColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalDaftar()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        table.getColumns().addAll(idColumn, namaColumn, alamatColumn, kotaColumn, kodePosColumn, noTelpColumn, emailColumn, jenisKelaminColumn, tanggalDaftarColumn, statusColumn);

        ObservableList<Pembeli> pembeliList = FXCollections.observableArrayList();
        String query = "SELECT * FROM pembeli";

        try (Connection conn = connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pembeli pembeli = new Pembeli(
                    rs.getInt("id_pembeli"),
                    rs.getString("nama_lengkap"),
                    rs.getString("alamat"),
                    rs.getString("kota"),
                    rs.getString("kode_pos"),
                    rs.getString("no_telepon"),
                    rs.getString("email"),
                    rs.getString("jenis_kelamin"),
                    rs.getTimestamp("tanggal_daftar").toString(),
                    rs.getString("status")
                );
                pembeliList.add(pembeli);
            }
        }

        table.setItems(pembeliList);

        VBox root = new VBox(10, table);
        root.setPadding(new Insets(10));
        
        TextField cariField = new TextField();
        cariField.setPromptText("Masukkan Nama Pelanggan");

        Button cariButton = new Button("Cari");
        cariButton.setOnAction(e -> {
            Stage resultStage = new Stage();
            resultStage.setTitle("Hasil Pencarian Pelanggan");

            TableView<Pembeli> resultTable = new TableView<>();
            TableColumn<Pembeli, Integer> resultIdColumn = new TableColumn<>("ID");
            TableColumn<Pembeli, String> resultNamaColumn = new TableColumn<>("Nama Lengkap");
            TableColumn<Pembeli, String> resultAlamatColumn = new TableColumn<>("Alamat");
            TableColumn<Pembeli, String> resultKotaColumn = new TableColumn<>("Kota");
            TableColumn<Pembeli, String> resultKodePosColumn = new TableColumn<>("Kode Pos");
            TableColumn<Pembeli, String> resultNoTelpColumn = new TableColumn<>("Nomor Telepon");
            TableColumn<Pembeli, String> resultEmailColumn = new TableColumn<>("Email");
            TableColumn<Pembeli, String> resultJenisKelaminColumn = new TableColumn<>("Jenis Kelamin");
            TableColumn<Pembeli, String> resultTanggalDaftarColumn = new TableColumn<>("Tanggal Daftar");
            TableColumn<Pembeli, String> resultStatusColumn = new TableColumn<>("Status");

            resultIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPembeli()).asObject());
            resultNamaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamaLengkap()));
            resultAlamatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlamat()));
            resultKotaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKota()));
            resultKodePosColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKodePos()));
            resultNoTelpColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNoTelpon()));
            resultEmailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
            resultJenisKelaminColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJenisKelamin()));
            resultTanggalDaftarColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalDaftar()));
            resultStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

            resultTable.getColumns().addAll(resultIdColumn,resultNamaColumn, resultAlamatColumn, resultKotaColumn, resultKodePosColumn, resultNoTelpColumn, resultEmailColumn, resultJenisKelaminColumn, resultTanggalDaftarColumn, resultStatusColumn);

            ObservableList<Pembeli> resultPembeliList = FXCollections.observableArrayList();
            String searchQuery = "SELECT * FROM pembeli WHERE nama_lengkap LIKE ?";

            try (Connection conn = connectToDatabase();
                 PreparedStatement stmt = conn.prepareStatement(searchQuery)) {

                stmt.setString(1, "%" + cariField.getText() + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Pembeli pembeli = new Pembeli(
                            rs.getInt("id_pembeli"),
                            rs.getString("nama_lengkap"),
                            rs.getString("alamat"),
                            rs.getString("kota"),
                            rs.getString("kode_pos"),
                            rs.getString("no_telepon"),
                            rs.getString("email"),
                            rs.getString("jenis_kelamin"),
                            rs.getTimestamp("tanggal_daftar").toString(),
                            rs.getString("status")
                        );
                        resultPembeliList.add(pembeli);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            resultTable.setItems(resultPembeliList);

            VBox resultRoot = new VBox(10, resultTable);
            resultRoot.setPadding(new Insets(10));

            resultStage.setScene(new Scene(resultRoot, 600, 100));
            resultStage.show();
        });
        
        root.getChildren().addAll(new Label("Cari Pelanggan"), cariField, cariButton);

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }


    private void showLihatSemuaPiutangGUI() {
        Stage stage = new Stage();
        stage.setTitle("Lihat Semua Piutang");

        TableView<Piutang> table = new TableView<>();
        TableColumn<Piutang, Integer> idPColumn = new TableColumn<>("ID");
        TableColumn<Piutang, String> namaColumn = new TableColumn<>("Nama Pelanggan");
        TableColumn<Piutang, Double> jumlahColumn = new TableColumn<>("Jumlah");
        TableColumn<Piutang, String> tanggalPinjamColumn = new TableColumn<>("Tanggal Pinjam");
        TableColumn<Piutang, String> dueDateColumn = new TableColumn<>("Tenggat Waktu");
        TableColumn<Piutang, String> lunasColumn = new TableColumn<>("Status Lunas");
        
        idPColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPiutang()).asObject());
        namaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamaPelanggan()));
        jumlahColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getJumlah()).asObject());
        tanggalPinjamColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalPinjam()));
        dueDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDueDate()));
        lunasColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusLunas()));

        table.getColumns().addAll(idPColumn, namaColumn, jumlahColumn, tanggalPinjamColumn, dueDateColumn, lunasColumn);

        ObservableList<Piutang> piutangList = FXCollections.observableArrayList();
        String query = "SELECT * FROM piutang INNER JOIN pembeli ON piutang.id_pelanggan = pembeli.id_pembeli";

        try (Connection conn = connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Piutang piutang = new Piutang(
                    rs.getInt("id_piutang"),
                    rs.getInt("id_pelanggan"),
                    rs.getString("nama_lengkap"),
                    rs.getDouble("jumlah"),
                    rs.getString("tanggal_pinjam"),
                    rs.getString("due_date"),
                    rs.getBoolean("status_lunas")
                );
                piutangList.add(piutang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setItems(piutangList);

        VBox root = new VBox(10, table);
        root.setPadding(new Insets(10));
        
        TextField cariField = new TextField();
        cariField.setPromptText("Masukkan Nama Pelanggan");
        
        Button cariButton = new Button("Cari");
        cariButton.setOnAction(e -> {
            Stage resultStage = new Stage();
            resultStage.setTitle("Hasil Pencarian Piutang");

            TableView<Piutang> resultTable = new TableView<>();
            TableColumn<Piutang, Integer> resultIdPColumn = new TableColumn<>("ID");
            TableColumn<Piutang, String> resultNamaColumn = new TableColumn<>("Nama Pelanggan");
            TableColumn<Piutang, Double> resultJumlahColumn = new TableColumn<>("Jumlah");
            TableColumn<Piutang, String> resultTanggalPinjamColumn = new TableColumn<>("Tanggal Pinjam");
            TableColumn<Piutang, String> resultDueDateColumn = new TableColumn<>("Tenggat Waktu");
            TableColumn<Piutang, String> resultLunasColumn = new TableColumn<>("Status Lunas");

            resultIdPColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPelaggan()).asObject());
            resultNamaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamaPelanggan()));
            resultJumlahColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getJumlah()).asObject());
            resultTanggalPinjamColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalPinjam()));
            resultDueDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDueDate()));
            resultLunasColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusLunas()));

            resultTable.getColumns().addAll(resultIdPColumn, resultNamaColumn, resultJumlahColumn, resultTanggalPinjamColumn, resultDueDateColumn, resultLunasColumn);

            ObservableList<Piutang> resultPiutangList = FXCollections.observableArrayList();
            String searchQuery = "SELECT id_piutang, id_pelanggan, nama_lengkap, jumlah, tanggal_pinjam, due_date, status_lunas FROM piutang INNER JOIN pembeli ON piutang.id_pelanggan = pembeli.id_pembeli WHERE pembeli.nama_lengkap LIKE ?";

            try (Connection conn = connectToDatabase();
                 PreparedStatement stmt = conn.prepareStatement(searchQuery)) {

                stmt.setString(1, "%" + cariField.getText() + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Piutang piutang = new Piutang(
                            rs.getInt("id_piutang"),
                            rs.getInt("id_pelanggan"),
                            rs.getString("nama_lengkap"),
                            rs.getDouble("jumlah"),
                            rs.getString("tanggal_pinjam"),
                            rs.getString("due_date"),
                            rs.getBoolean("status_lunas")
                        );
                        resultPiutangList.add(piutang);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            resultTable.setItems(resultPiutangList);

            VBox resultRoot = new VBox(10, resultTable);
            resultRoot.setPadding(new Insets(10));

            resultStage.setScene(new Scene(resultRoot, 600, 100));
            resultStage.show();
        });
        
        root.getChildren().addAll(new Label("Cari Piutang"), cariField, cariButton);

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    
    private void showLihatSemuaPelunasanGUI() {
        Stage stage = new Stage();
        stage.setTitle("Lihat Semua Pelunasan");

        TableView<Pelunasan> table = new TableView<>();
        TableColumn<Pelunasan, Integer> idPelunasanColumn = new TableColumn<>("ID");
        TableColumn<Pelunasan, String> namaLengkapColumn = new TableColumn<>("Nama Lengkap");
        TableColumn<Pelunasan, Double> jumlahBayarColumn = new TableColumn<>("Jumlah Bayar");
        TableColumn<Pelunasan, String> tanggalBayarColumn = new TableColumn<>("Tanggal Bayar");

        idPelunasanColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPelunasan()).asObject());
        namaLengkapColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdPiutang()));
        jumlahBayarColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getJumlahBayar()).asObject());
        tanggalBayarColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalBayar()));

        table.getColumns().addAll(idPelunasanColumn,namaLengkapColumn, jumlahBayarColumn, tanggalBayarColumn);

        ObservableList<Pelunasan> pelunasanList = FXCollections.observableArrayList();
        String query = "SELECT pelunasan.id_pelunasan, pelunasan.jumlah_bayar, pelunasan.tanggal_bayar, pembeli.nama_lengkap FROM pelunasan JOIN piutang ON pelunasan.id_piutang = piutang.id_piutang JOIN pembeli ON piutang.id_pelanggan = pembeli.id_pembeli";

        try (Connection conn = connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pelunasan pelunasan = new Pelunasan(
                    rs.getInt("id_pelunasan"),
                    rs.getString("nama_lengkap"),
                    rs.getDouble("jumlah_bayar"),
                    rs.getString("tanggal_bayar")
                );
                pelunasanList.add(pelunasan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setItems(pelunasanList);

        VBox root = new VBox(10, table);
        root.setPadding(new Insets(10));
        
        TextField cariField = new TextField();
        cariField.setPromptText("Masukkan Nama Pelanggan");

        Button cariButton = new Button("Cari");
        cariButton.setOnAction(e -> {
            Stage resultStage = new Stage();
            resultStage.setTitle("Hasil Pencarian Pelunasan");

            TableView<Pelunasan> resultTable = new TableView<>();
            TableColumn<Pelunasan, String> resultIdPiutangColumn = new TableColumn<>("ID Piutang");
            TableColumn<Pelunasan, String> resultNamaLengkapColumn = new TableColumn<>("Jumlah Bayar");
            TableColumn<Pelunasan, Double> resultJumlahBayarColumn = new TableColumn<>("Jumlah Bayar");
            TableColumn<Pelunasan, String> resultTanggalBayarColumn = new TableColumn<>("Tanggal Bayar");

            resultIdPiutangColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdPiutang()));
            resultNamaLengkapColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdPiutang()));
            resultJumlahBayarColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getJumlahBayar()).asObject());
            resultTanggalBayarColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalBayar()));

            resultTable.getColumns().addAll(resultIdPiutangColumn, resultNamaLengkapColumn, resultJumlahBayarColumn, resultTanggalBayarColumn);

            ObservableList<Pelunasan> searchPelunasanList = FXCollections.observableArrayList();
            String searchQuery = "SELECT pelunasan.id_pelunasan, pelunasan.jumlah_bayar, pelunasan.tanggal_bayar, pembeli.nama_lengkap FROM pelunasan JOIN piutang ON pelunasan.id_piutang = piutang.id_piutang JOIN pembeli ON piutang.id_pelanggan = pembeli.id_pembeli WHERE pembeli.nama_lengkap LIKE ?";

            try (Connection conn = connectToDatabase();
                 PreparedStatement stmt = conn.prepareStatement(searchQuery)) {

                stmt.setString(1, "%" + cariField.getText() + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Pelunasan pelunasan = new Pelunasan(
                            rs.getInt("id_pelunasan"),
                            rs.getString("nama_lengkap"),
                            rs.getDouble("jumlah_bayar"),
                            rs.getString("tanggal_bayar")
                        );
                        searchPelunasanList.add(pelunasan);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            resultTable.setItems(searchPelunasanList);

            VBox resultRoot = new VBox(10, resultTable);
            resultRoot.setPadding(new Insets(10));

            resultStage.setScene(new Scene(resultRoot, 600, 400));
            resultStage.show();
        });
        
        root.getChildren().addAll(new Label("Cari Pelunasan"), cariField, cariButton);

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
    
    private void showHapusDataGUI() {
        Stage stage = new Stage();
        stage.setTitle("Hapus Data");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField idField = new TextField();
        idField.setPromptText("Masukkan ID Data yang Akan Dihapus");

        Button hapusButton = new Button("Hapus");
        hapusButton.setOnAction(e -> {
            try (Connection conn = connectToDatabase()) {
            	String fk = "SET FOREIGN_KEY_CHECKS=0";
            	PreparedStatement fkey = conn.prepareStatement(fk);
            	fkey.executeUpdate();
            	
                String sql = "DELETE FROM piutang WHERE id_piutang = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                int id = Integer.parseInt(idField.getText());
                pstmt.setInt(1, id);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
                
                sql = "DELETE FROM pelunasan WHERE id_piutang = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                
                fk = "SET FOREIGN_KEY_CHECKS=1";
                fkey.executeUpdate();
            } catch (SQLException ex) {
            	Alert alert = new Alert(AlertType.ERROR);
            	alert.setTitle("Error Dialog");
            	alert.setHeaderText("Ooops, sepertinya ada error!");
            	alert.setContentText("Pastikan ID piutang dimasukkan dengan benar.");
            	ex.printStackTrace();
            	alert.showAndWait();
                
            }
            stage.close();
        });

        root.getChildren().addAll(new Label("Masukkan ID"), idField, hapusButton);

        stage.setScene(new Scene(root, 300, 150));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
