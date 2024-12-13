package edu.pradita.p14.Tax;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class taxList {
    private static final String url = "jdbc:mysql://localhost:3306/tax"
    private static final String user = "root";
    private static final String password = "";

    private List<Tax> taxes;

    public taxList() {
        taxes = new Arraylist<>();
        loadTaxes();
    }

    public void loadTaxes() {
        try (Connection conn = DriverManager.getConnection(url, user,password)) {
            String sql = "SELECT * FROM tax";
            PrepareStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int taxID = rs.getInt("taxID");
                String taxName = rs.getString("taxName");
                Double taxRate = rs.getDouble("taxRate");
                taxes.add(new Tax(taxID, taxName, taxRate));
            }
        }
    } catch (SQLexeption e) {
        e.printStackTrace();
    }
}
public List<Tax> getTaxes() {
    return taxes;
}
