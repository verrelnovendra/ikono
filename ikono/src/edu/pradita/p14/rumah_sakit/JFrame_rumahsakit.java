package rumah_sakit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class JFrame_rumahsakit extends JFrame {
    private JTable tablePoli;
    private JTable tableDokter;
    private JTable tableSuster;
    private DefaultTableModel modelPoli;
    private DefaultTableModel modelDokter;
    private DefaultTableModel modelSuster;

    public JFrame_rumahsakit() {
        setTitle("Sistem Informasi Rumah Sakit");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel utama
        JPanel mainPanel = new JPanel(new GridLayout(3, 1));
        
        // Tabel Poli
        modelPoli = new DefaultTableModel(new String[]{"ID", "Nama Poli", "Deskripsi"}, 0);
        tablePoli = new JTable(modelPoli);
        JScrollPane scrollPoli = new JScrollPane(tablePoli);
        JPanel panelPoli = new JPanel(new BorderLayout());
        panelPoli.add(new JLabel("Daftar Poli"), BorderLayout.NORTH);
        panelPoli.add(scrollPoli, BorderLayout.CENTER);
        mainPanel.add(panelPoli);

        // Tabel Dokter
        modelDokter = new DefaultTableModel(new String[]{"ID", "Nama Dokter", "Spesialisasi", "Poli ID"}, 0);
        tableDokter = new JTable(modelDokter);
        JScrollPane scrollDokter = new JScrollPane(tableDokter);
        JPanel panelDokter = new JPanel(new BorderLayout());
        panelDokter.add(new JLabel("Daftar Dokter"), BorderLayout.NORTH);
        panelDokter.add(scrollDokter, BorderLayout.CENTER);
        mainPanel.add(panelDokter);

        // Tabel Suster
        modelSuster = new DefaultTableModel(new String[]{"ID", "Nama Suster", "Bagian", "Poli ID"}, 0);
        tableSuster = new JTable(modelSuster);
        JScrollPane scrollSuster = new JScrollPane(tableSuster);
        JPanel panelSuster = new JPanel(new BorderLayout());
        panelSuster.add(new JLabel("Daftar Suster"), BorderLayout.NORTH);
        panelSuster.add(scrollSuster, BorderLayout.CENTER);
        mainPanel.add(panelSuster);

        add(mainPanel, BorderLayout.CENTER);

        // Load data dari database
        loadData();
    }

    private void loadData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                // Load data Poli
                String queryPoli = "SELECT * FROM poli";
                try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(queryPoli)) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nama = rs.getString("nama");
                        String deskripsi = rs.getString("deskripsi");
                        modelPoli.addRow(new Object[]{id, nama, deskripsi});
                    }
                }

                // Load data Dokter
                String queryDokter = "SELECT * FROM dokter";
                try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(queryDokter)) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nama = rs.getString("nama");
                        String spesialisasi = rs.getString("spesialisasi");
                        int poliId = rs.getInt("poli_id");
                        modelDokter.addRow(new Object[]{id, nama, spesialisasi, poliId});
                    }
                }

                // Load data Suster
                String querySuster = "SELECT * FROM suster";
                try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(querySuster)) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nama = rs.getString("nama");
                        String bagian = rs.getString("bagian");
                        int poliId = rs.getInt("poli_id");
                        modelSuster.addRow(new Object[]{id, nama, bagian, poliId});
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame_rumahsakit frame = new JFrame_rumahsakit();
            frame.setVisible(true);
        });
    }
}
