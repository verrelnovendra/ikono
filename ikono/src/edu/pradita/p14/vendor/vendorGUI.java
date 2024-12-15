package vendor.gui.p14;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vendor.connection.p14.VendorConnection;

import java.sql.*;

public class VendorGUI extends Application {

    private TableView<Vendor> table;
    private ObservableList<Vendor> vendorList;
    private TextField nameField, phoneField, addressField, emailField, contactField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Vendor Management");

        table = new TableView<>();
        vendorList = FXCollections.observableArrayList();

        TableColumn<Vendor, String> idColumn = new TableColumn<>("Vendor ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Vendor, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Vendor, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Vendor, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Vendor, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Vendor, String> contactColumn = new TableColumn<>("Contact Person");
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));

        table.getColumns().addAll(idColumn, nameColumn, phoneColumn, addressColumn, emailColumn, contactColumn);
        table.setItems(vendorList);

        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        nameField = new TextField();
        phoneField = new TextField();
        addressField = new TextField();
        emailField = new TextField();
        contactField = new TextField();

        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Phone:"), 0, 1);
        form.add(phoneField, 1, 1);
        form.add(new Label("Address:"), 0, 2);
        form.add(addressField, 1, 2);
        form.add(new Label("Email:"), 2, 0);
        form.add(emailField, 3, 0);
        form.add(new Label("Contact Person:"), 2, 1);
        form.add(contactField, 3, 1);

        Button addButton = new Button("Add Vendor");
        Button deleteButton = new Button("Delete Vendor");
        form.add(addButton, 2, 3);
        form.add(deleteButton, 3, 3);

        VBox layout = new VBox(10, table, form);
        layout.setPadding(new Insets(10));

        loadData();

        addButton.setOnAction(e -> addVendor());
        deleteButton.setOnAction(e -> deleteVendor());

        Scene scene = new Scene(layout, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadData() {
        try (Connection connection = VendorConnection.getConnection()) {
            String query = "SELECT vendor_id, name, phone, address, email, contact_person FROM Vendor";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            vendorList.clear();

            while (rs.next()) {
                vendorList.add(new Vendor(
                        rs.getString("vendor_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("contact_person")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load data.");
        }
    }

    private void addVendor() {
        try (Connection connection = VendorConnection.getConnection()) {
            String query = "INSERT INTO Vendor (name, phone, address, email, contact_person) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, phoneField.getText());
            pstmt.setString(3, addressField.getText());
            pstmt.setString(4, emailField.getText());
            pstmt.setString(5, contactField.getText());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                loadData();
                showAlert(Alert.AlertType.INFORMATION, "Vendor added successfully.");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to add vendor.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to add vendor: " + e.getMessage());
        }
    }

    private void deleteVendor() {
        Vendor selectedVendor = table.getSelectionModel().getSelectedItem();
        if (selectedVendor != null) {
            try (Connection connection = VendorConnection.getConnection()) {
                String query = "DELETE FROM Vendor WHERE vendor_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, selectedVendor.getId());
                pstmt.executeUpdate();

                loadData();
                showAlert(Alert.AlertType.INFORMATION, "Vendor deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Failed to delete vendor.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Please select a vendor to delete.");
        }
    }

    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        addressField.clear();
        emailField.clear();
        contactField.clear();
    }
    

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class Vendor {
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty phone;
        private final SimpleStringProperty address;
        private final SimpleStringProperty email;
        private final SimpleStringProperty contactPerson;

        public Vendor(String id, String name, String phone, String address, String email, String contactPerson) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.phone = new SimpleStringProperty(phone);
            this.address = new SimpleStringProperty(address);
            this.email = new SimpleStringProperty(email);
            this.contactPerson = new SimpleStringProperty(contactPerson);
        }

        public String getId() { return id.get(); }
        public String getName() { return name.get(); }
        public String getPhone() { return phone.get(); }
        public String getAddress() { return address.get(); }
        public String getEmail() { return email.get(); }
        public String getContactPerson() { return contactPerson.get(); }
    }    
}
