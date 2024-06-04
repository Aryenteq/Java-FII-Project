package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {
    private final Connection connection;

    public LocationDAO() throws SQLException {
        this.connection = MySQLConnector.getInstance().getConnection();
    }

    public void addLocation(String name, double latitude, double longitude, boolean solved) throws SQLException {
        String query = "INSERT INTO Locations (name, latitude, longitude, solved) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, latitude);
            preparedStatement.setDouble(3, longitude);
            preparedStatement.setBoolean(4, solved);
            preparedStatement.executeUpdate();
        }
    }

    public void updateSolvedStatus(int id, boolean solved) throws SQLException {
        String query = "UPDATE Locations SET solved = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBoolean(1, solved);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteLocation(int id) throws SQLException {
        String query = "DELETE FROM Locations WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteAll() throws SQLException {
        String query = "DELETE FROM Locations";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }

    public ResultSet getUnsolvedLocations() throws SQLException {
        String query = "SELECT * FROM Locations WHERE solved = 0";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    public List<LocationStructure> getUnsolvedLocationsList() throws SQLException {
        ResultSet resultSet = getUnsolvedLocations();
        List<LocationStructure> locations = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            double lat = resultSet.getDouble("latitude");
            double lon = resultSet.getDouble("longitude");
            boolean solved = resultSet.getBoolean("solved");

            LocationStructure locationStructure = new LocationStructure(id, name, lat, lon, solved);
            locations.add(locationStructure);
        }
        return locations;
    }

    public void markAsSolved(int id) throws SQLException {
        String query = "UPDATE Locations SET solved = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}