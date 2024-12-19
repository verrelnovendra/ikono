package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main extends Application {
    // Database credentials
    final String DB_URL = "jdbc:mysql://localhost:3306/productdb";
    final String USER = "root"; // Ganti dengan username MySQL Anda
    final String PASS = "";    // Ganti dengan password MySQL Anda

    @Override
    public void start(Stage primaryStage) {
        // Membuat Label judul
        Label titleLabel = new Label("Product Management System");

        // Membuat TextField untuk input data
        TextField brandNameField = new TextField();
        brandNameField.setPromptText("Enter Brand Name");

        TextField productNameField = new TextField();
        productNameField.setPromptText("Enter Product Name");

        TextField priceField = new TextField();
        priceField.setPromptText("Enter Price");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Enter Category");

        TextField featuresField = new TextField();
        featuresField.setPromptText("Enter Features Count");

        // Membuat Button untuk menambahkan produk
        Button addButton = new Button("Add Product");

        // Event handler untuk tombol
        addButton.setOnAction(event -> {
            try {
                String brandName = brandNameField.getText();
                String productName = productNameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String category = categoryField.getText();
                int features = Integer.parseInt(featuresField.getText());

                // Menambahkan produk ke database
                addProduct(brandName, productName, price, category, features);

                // Reset input field setelah berhasil
                brandNameField.clear();
                productNameField.clear();
                priceField.clear();
                categoryField.clear();
                featuresField.clear();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Membuat layout dengan VBox
        VBox layout = new VBox(10, titleLabel, brandNameField, productNameField, priceField, categoryField, featuresField, addButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Membuat Scene dan menampilkan stage
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("Product Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Metode untuk menambahkan produk ke database
    private void addProduct(String brandName, String productName, double price, String category, int features) {
        String insertSQL = "INSERT INTO products (brandname, productname, price, category, features) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, brandName);
            pstmt.setString(2, productName);
            pstmt.setDouble(3, price);
            pstmt.setString(4, category);
            pstmt.setInt(5, features);

            pstmt.executeUpdate();
            System.out.println("Product added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
