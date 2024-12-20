package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Rack;

public class RackDAO {
    public List<Rack> getAllRacks() {
        List<Rack> racks = new ArrayList<>();
        String query = "SELECT * FROM racks";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Rack rack = new Rack(
                    resultSet.getInt("rack_id"),
                    resultSet.getString("rack_name"),
                    resultSet.getString("location")
                );
                racks.add(rack);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return racks;
    }

    public boolean addRack(Rack rack) {
        String query = "INSERT INTO racks (rack_name, location) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, rack.getRackName());
            preparedStatement.setString(2, rack.getLocation());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Additional methods: updateRack, deleteRack
}
