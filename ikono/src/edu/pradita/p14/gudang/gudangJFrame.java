package gudang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class gudangJFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    gudangJFrame frame = new gudangJFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public gudangJFrame() {
        setTitle("Gudang Data");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1107, 550);

        // Set background image
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Ensure path to image is correct and update if necessary
                ImageIcon backgroundImage = new ImageIcon("C:\\Users\\zikry\\Downloads\\op.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Create table with column names
        model = new DefaultTableModel();
        model.addColumn("ID Gudang");
        model.addColumn("Nama Gudang");
        model.addColumn("Lokasi");
        model.addColumn("Kota");
        model.addColumn("Kapasitas");
        model.addColumn("Jumlah Barang");
        model.addColumn("No Telepon");
        model.addColumn("Email");
        model.addColumn("Status");

        // Customize the table
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12)); // Set font for the table
        table.setRowHeight(30); // Increase row height for better readability
        table.setBackground(new Color(255, 255, 255, 200)); // Set table background color to white with transparency
        table.setGridColor(new Color(0, 0, 0)); // Set grid color to black
        table.setSelectionBackground(new Color(100, 149, 237)); // Set selected row background color

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false); // Make viewport transparent

        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Fetch data from database
        fetchDataFromDatabase();
    }

    private void fetchDataFromDatabase() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                String query = "SELECT * FROM gudang";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Clear any previous data
                model.setRowCount(0);

                while (resultSet.next()) {
                    // Extract data from ResultSet
                    Object[] row = {
                        resultSet.getInt("id_gudang"),
                        resultSet.getString("nama_gudang"),
                        resultSet.getString("lokasi"),
                        resultSet.getString("kota"),
                        resultSet.getInt("kapasitas"),
                        resultSet.getInt("jumlah_barang"),
                        resultSet.getString("no_telepon"),
                        resultSet.getString("email"),
                        resultSet.getString("status")
                    };
                    model.addRow(row);
                }

                resultSet.close();
                statement.close();
            } else {
                JOptionPane.showMessageDialog(this, "Connection to database failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
