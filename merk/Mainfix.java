
	
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
    final String DB_URL = "jdbc:mysql://localhost:3306/productdb";
    final String USER = "root";
    final String PASS = ""; 

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("Product Management System");

        TextField merekField = new TextField();
        merekField.setPromptText("Enter Brand Name");

        TextField kodeNameField = new TextField();
        kodeNameField.setPromptText("Enter Product Code");

        TextField deskripsiField = new TextField();
        deskripsiField.setPromptText("Enter Description");

        Button addButton = new Button("Add Product");

        addButton.setOnAction(event -> {
            try {
                String merek = merekField.getText();
                String kode = kodeNameField.getText();
                String deskripsi = deskripsiField.getText();

                addProduct(merek, kode, deskripsi);

                merekField.clear();
                kodeNameField.clear();
                deskripsiField.clear();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10, titleLabel, merekField, kodeNameField, deskripsiField, addButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("Product Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addProduct(String brandName, String code, String description) {
        String insertSQL = "INSERT INTO products (brandname, code, description) VALUES (?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, brandName);
            pstmt.setString(2, code);
            pstmt.setString(3, description);

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
