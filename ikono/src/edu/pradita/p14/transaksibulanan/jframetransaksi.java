package transaksibulanan;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Vector;

public class jframetransaksi extends JFrame {

    private JTable table;
    private JScrollPane scrollPane;
    private JLabel totalLabel;

    public jframetransaksi() {
        // Set up JFrame
        setTitle("Laporan Pembelian Bulanan");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout
        setLayout(new BorderLayout());

        // Initialize components
        totalLabel = new JLabel("Total Keseluruhan: Rp. 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(totalLabel, BorderLayout.SOUTH);

        // Create table and scroll pane
        table = new JTable();
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Fetch and display data
        loadData();
    }

    private void loadData() {
        try {
            Connection connection = databaseconnection.getConnection();
            if (connection != null) {
                // Query to fetch data from database
                String query = "SELECT tanggal, nama_barang, jumlah, harga FROM pembelian";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Prepare table columns
                Vector<String> columnNames = new Vector<>();
                columnNames.add("Tanggal");
                columnNames.add("Nama Barang");
                columnNames.add("Jumlah");
                columnNames.add("Harga");
                columnNames.add("Total");

                // Prepare data rows
                Vector<Vector<Object>> data = new Vector<>();
                double totalKeseluruhan = 0.0;
                DecimalFormat df = new DecimalFormat("#,###.00");

                while (resultSet.next()) {
                    String tanggal = resultSet.getString("tanggal");
                    String namaBarang = resultSet.getString("nama_barang");
                    int jumlah = resultSet.getInt("jumlah");
                    double harga = resultSet.getDouble("harga");
                    double totalHarga = jumlah * harga;

                    Vector<Object> row = new Vector<>();
                    row.add(tanggal);
                    row.add(namaBarang);
                    row.add(jumlah);
                    row.add(df.format(harga));
                    row.add(df.format(totalHarga));

                    data.add(row);

                    totalKeseluruhan += totalHarga;
                }

                // Set the table model
                table.setModel(new CustomTableModel(data, columnNames));

                // Update total label
                totalLabel.setText("Total Keseluruhan: Rp. " + df.format(totalKeseluruhan));

                // Close resources
                resultSet.close();
                statement.close();
                connection.close();
            } else {
                JOptionPane.showMessageDialog(this, "Koneksi ke database gagal!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan dalam mengambil data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom Table Model to handle the formatting
    public static class CustomTableModel extends javax.swing.table.AbstractTableModel {
        private final Vector<Vector<Object>> data;
        private final Vector<String> columnNames;

        public CustomTableModel(Vector<Vector<Object>> data, Vector<String> columnNames) {
            this.data = data;
            this.columnNames = columnNames;
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.size();
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data.get(row).get(col);
        }

        @Override
        public String getColumnName(int col) {
            return columnNames.get(col);
        }

        @Override
        public Class<?> getColumnClass(int col) {
            return getValueAt(0, col).getClass();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
           jframetransaksi frame = new jframetransaksi();
            frame.setVisible(true);
        });
    }
}