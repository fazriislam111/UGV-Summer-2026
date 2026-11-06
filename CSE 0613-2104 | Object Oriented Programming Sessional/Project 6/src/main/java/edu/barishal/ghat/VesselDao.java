package edu.barishal.ghat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VesselDao {
    private static final String INSERT_SQL = """
            INSERT INTO vessels
                (vessel_id, name, capacity, route, vessel_type, fuel_level, current_location)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_SQL = """
            SELECT vessel_id, name, capacity, route, vessel_type, fuel_level, current_location
            FROM vessels
            ORDER BY name, vessel_id
            """;

    private static final String UPDATE_OPERATIONAL_DATA_SQL = """
            UPDATE vessels
            SET fuel_level = ?, current_location = ?
            WHERE vessel_id = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE vessels
            SET vessel_id = ?, name = ?, capacity = ?, route = ?, vessel_type = ?,
                fuel_level = ?, current_location = ?
            WHERE vessel_id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM vessels
            WHERE vessel_id = ?
            """;

    private static final String EXISTS_SQL = """
            SELECT 1 FROM vessels WHERE vessel_id = ?
            """;

    public void insert(Vessel vessel) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setString(1, vessel.getVesselId());
            statement.setString(2, vessel.getName());
            statement.setInt(3, vessel.getCapacity());
            statement.setString(4, vessel.getRoute());
            statement.setString(5, vessel.getVesselType().name());
            statement.setDouble(6, vessel.getFuelLevel());
            statement.setString(7, vessel.getCurrentLocation());
            statement.executeUpdate();
        }
    }

    public List<Vessel> findAll() throws SQLException {
        List<Vessel> vessels = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                VesselType type = VesselType.fromDatabaseValue(
                        resultSet.getString("vessel_type"));
                vessels.add(VesselFactory.reconstruct(
                        type,
                        resultSet.getString("vessel_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("capacity"),
                        resultSet.getString("route"),
                        resultSet.getDouble("fuel_level"),
                        resultSet.getString("current_location")));
            }
        }
        return vessels;
    }

    public void updateOperationalData(Vessel vessel) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_OPERATIONAL_DATA_SQL)) {
            statement.setDouble(1, vessel.getFuelLevel());
            statement.setString(2, vessel.getCurrentLocation());
            statement.setString(3, vessel.getVesselId());
            if (statement.executeUpdate() != 1) {
                throw new SQLException(
                        "Could not update vessel " + vessel.getVesselId() + ".");
            }
        }
    }

    public void update(String originalVesselId, Vessel vessel) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, vessel.getVesselId());
            statement.setString(2, vessel.getName());
            statement.setInt(3, vessel.getCapacity());
            statement.setString(4, vessel.getRoute());
            statement.setString(5, vessel.getVesselType().name());
            statement.setDouble(6, vessel.getFuelLevel());
            statement.setString(7, vessel.getCurrentLocation());
            statement.setString(8, originalVesselId);
            if (statement.executeUpdate() == 0 && !exists(connection, originalVesselId)) {
                throw new SQLException(
                        "Could not find vessel " + originalVesselId + " to update.");
            }
        }
    }

    public void delete(String vesselId) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setString(1, vesselId);
            if (statement.executeUpdate() != 1) {
                throw new SQLException("Could not find vessel " + vesselId + " to delete.");
            }
        }
    }

    private boolean exists(Connection connection, String vesselId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(EXISTS_SQL)) {
            statement.setString(1, vesselId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
