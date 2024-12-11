package edu.pradita.p14;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField databaseField;

    @FXML
    private Label infoLabel;

    @FXML
    private Button connectButton;

    @FXML
    private void connectToDatabase(MouseEvent event) {
        String host = hostField.getText();
        String port = portField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String database = databaseField.getText();

        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            Connection connection = DriverManager.getConnection(url, username, password);
            infoLabel.setText("Koneksi berhasil!");
            infoLabel.setStyle("-fx-text-fill: green;");
            infoLabel.setVisible(true);
            connection.close();
        } catch (SQLException e) {
            infoLabel.setText("Koneksi gagal: " + e.getMessage());
            infoLabel.setStyle("-fx-text-fill: red;");
            infoLabel.setVisible(true);
        }
    }
}
