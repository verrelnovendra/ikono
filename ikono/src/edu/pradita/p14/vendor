package uas.vendor.gui.p14;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import uas.vendor.p14.DatabaseVendorConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class VendorTableGUI extends JFrame {
		
	    private JTable table;
	    private DefaultTableModel tableModel;
	    private JTextField idField, nameField, phoneField, addressField, emailField, contactField;
	    private JButton addButton, deleteButton;

	    public VendorTableGUI() {
	        setTitle("Vendor Management");
	        setSize(900, 700);
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        setLayout(null);

	      
	        tableModel = new DefaultTableModel(new String[]{"Vendor ID", "Name", "Phone", "Address", "Email", "Contact Person"}, 0);
	        table = new JTable(tableModel);
	        JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.setBounds(20, 20, 850, 300);
	        add(scrollPane);

	        JLabel idLabel = new JLabel("Vendor ID:");
	        idLabel.setBounds(20, 340, 100, 25);
	        add(idLabel);

	        idField = new JTextField();
	        idField.setBounds(120, 340, 200, 25);
	        add(idField);

	        JLabel nameLabel = new JLabel("Name:");
	        nameLabel.setBounds(20, 380, 100, 25);
	        add(nameLabel);

	        nameField = new JTextField();
	        nameField.setBounds(120, 380, 200, 25);
	        add(nameField);

	        JLabel phoneLabel = new JLabel("Phone:");
	        phoneLabel.setBounds(20, 420, 100, 25);
	        add(phoneLabel);

	        phoneField = new JTextField();
	        phoneField.setBounds(120, 420, 200, 25);
	        add(phoneField);

	        JLabel addressLabel = new JLabel("Address:");
	        addressLabel.setBounds(20, 460, 100, 25);
	        add(addressLabel);

	        addressField = new JTextField();
	        addressField.setBounds(120, 460, 200, 25);
	        add(addressField);

	        JLabel emailLabel = new JLabel("Email:");
	        emailLabel.setBounds(350, 340, 100, 25);
	        add(emailLabel);

	        emailField = new JTextField();
	        emailField.setBounds(450, 340, 200, 25);
	        add(emailField);

	        JLabel contactLabel = new JLabel("Contact Person:");
	        contactLabel.setBounds(350, 380, 120, 25);
	        add(contactLabel);

	        contactField = new JTextField();
	        contactField.setBounds(450, 380, 200, 25);
	        add(contactField);

	        addButton = new JButton("Add Vendor");
	        addButton.setBounds(350, 420, 150, 25);
	        add(addButton);

	        deleteButton = new JButton("Delete Vendor");
	        deleteButton.setBounds(350, 460, 150, 25);
	        add(deleteButton);

	        
	        loadData();


	        addButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                addVendor();
	            }
	        });

	       
	        deleteButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                deleteVendor();
	            }
	        });
	    }

	    private void loadData() {
	        try (Connection connection = DatabaseVendorConnection.getConnection()) {
	            String query = "SELECT vendor_id, name, phone, address, email, contact_person FROM Vendor";
	            Statement stmt = connection.createStatement();
	            ResultSet rs = stmt.executeQuery(query);

	            
	            tableModel.setRowCount(0);

	          
	            while (rs.next()) {
	                tableModel.addRow(new Object[]{
	                        rs.getInt("vendor_id"),
	                        rs.getString("name"),
	                        rs.getString("phone"),
	                        rs.getString("address"),
	                        rs.getString("email"),
	                        rs.getString("contact_person")
	                });
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(this, "Failed to load data.");
	        }
	    }

	    private void addVendor() {
	        try (Connection connection = DatabaseVendorConnection.getConnection()) {
	            
	            String query = "INSERT INTO Vendor (name, phone, address, email, contact_person) VALUES (?, ?, ?, ?, ?)";
	            
	            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	                
	                pstmt.setString(1, nameField.getText());
	                pstmt.setString(2, phoneField.getText());
	                pstmt.setString(3, addressField.getText());
	                pstmt.setString(4, emailField.getText());
	                pstmt.setString(5, contactField.getText());

	              
	                int rowsAffected = pstmt.executeUpdate();

	                if (rowsAffected > 0) {
	                    
	                    tableModel.addRow(new Object[]{
	                            idField.getText(), 
	                            nameField.getText(),
	                            phoneField.getText(),
	                            addressField.getText(),
	                            emailField.getText(),
	                            contactField.getText()
	                    });

	                    JOptionPane.showMessageDialog(this, "Vendor added successfully.");

	                    
	                    idField.setText("");
	                    nameField.setText("");
	                    phoneField.setText("");
	                    addressField.setText("");
	                    emailField.setText("");
	                    contactField.setText("");
	                } else {
	                    JOptionPane.showMessageDialog(this, "No rows affected. Vendor not added.");
	                }
	            }
	        } catch (SQLException e) {
	           
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(this, "Failed to add vendor: " + e.getMessage());
	        }
	    }


	    private void deleteVendor() {
	        int selectedRow = table.getSelectedRow();
	        if (selectedRow >= 0) {
	            try (Connection connection = DatabaseVendorConnection.getConnection()) {
	                String vendorId = tableModel.getValueAt(selectedRow, 0).toString();
	                String query = "DELETE FROM Vendor WHERE vendor_id = ?";
	                PreparedStatement pstmt = connection.prepareStatement(query);
	                pstmt.setInt(1, Integer.parseInt(vendorId));
	                pstmt.executeUpdate();

	               
	                tableModel.removeRow(selectedRow);
	                JOptionPane.showMessageDialog(this, "Vendor deleted successfully.");
	            } catch (SQLException e) {
	                e.printStackTrace();
	                JOptionPane.showMessageDialog(this, "Failed to delete vendor.");
	            }
	        } else {
	            JOptionPane.showMessageDialog(this, "Please select a vendor to delete.");
	        }
	    }

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> new VendorTableGUI().setVisible(true));
	    }
	}
