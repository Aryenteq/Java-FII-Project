package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LocationDAO {
    private Connection connection;

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

    public void deleteLocation(int id) throws SQLException {
        String query = "DELETE FROM Locations WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
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
}

