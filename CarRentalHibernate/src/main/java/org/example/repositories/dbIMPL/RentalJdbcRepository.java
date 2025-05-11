package org.example.repositories.dbIMPL;

import org.example.db.JdbcConnectionManager;
import org.example.model.Rental;
import org.example.model.User;
import org.example.model.Vehicle;
import org.example.repositories.RentalRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RentalJdbcRepository implements RentalRepository {
    @Override
    public List<Rental> findAll() {
        List<Rental> list = new ArrayList<>();
        String sql = "SELECT * from rental";
        try(Connection connection = JdbcConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String vehicleId = rs.getString("vehicle_id");
                String userId = rs.getString("user_id");

                Vehicle vehicle = getVehicleById(vehicleId);
                User user = getUserById(userId);

                Rental rental = Rental.builder()
                        .id(rs.getString("id"))
                        .vehicle(vehicle)
                        .user(user)
                        .rentDate(rs.getString("rent_date"))
                        .returnDate(rs.getString("return_date"))
                        .build();

                list.add(rental);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading rentals", e);
        }
        return list;
    }
    private Vehicle getVehicleById(String vehicleId) {
        String sql = "SELECT * from vehicle WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, vehicleId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Vehicle.builder()
                            .id(rs.getString("id"))
                            .category(rs.getString("category"))
                            .brand(rs.getString("brand"))
                            .model(rs.getString("model"))
                            .year(rs.getInt("year"))
                            .plate(rs.getString("plate"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while fetching vehicle", e);
        }
        return null;
    }
    private User getUserById(String userId) {
        String sql = "SELECT * from users WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return User.builder()
                            .id(rs.getString("id"))
                            .login(rs.getString("login"))
                            .password(rs.getString("password"))
                            .role(rs.getString("role"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while fetching user", e);
        }
        return null;
    }

    @Override
    public Optional<Rental> findById(String id) {
        String sql = "SELECT * from rental where id =?";
        try(Connection connection = JdbcConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String vehicleId = rs.getString("vehicle_id");
                    String userId = rs.getString("user_id");

                    Vehicle vehicle = getVehicleById(vehicleId);
                    User user = getUserById(userId);

                    Rental rental = Rental.builder()
                            .id(rs.getString("id"))
                            .vehicle(vehicle)
                            .user(user)
                            .rentDate(rs.getString("rent_date"))
                            .returnDate(rs.getString("return_date"))
                            .build();

                    return Optional.of(rental);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading rental", e);
        }
        return Optional.empty();
    }
    public Optional<Rental> findByUserId(String id) {
        String sql = "SELECT * from rental where user_id =?";
        try(Connection connection = JdbcConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    String vehicleId = rs.getString("vehicle_id");
                    String userId = rs.getString("user_id");

                    Vehicle vehicle = getVehicleById(vehicleId);
                    User user = getUserById(userId);

                    Rental rental = Rental.builder()
                            .id(rs.getString("id"))
                            .vehicle(vehicle)
                            .user(user)
                            .rentDate(rs.getString("rent_date"))
                            .returnDate(rs.getString("return_date"))
                            .build();

                    return Optional.of(rental);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading rental", e);
        }
        return Optional.empty();
    }

    @Override
    public Rental save(Rental rental) {
        if (rental.getId() == null || rental.getId().isBlank()) {
            rental.setId(UUID.randomUUID().toString());
        } else {
            deleteById(rental.getId());
        }
            String sql = "INSERT INTO rental (id, vehicle_id, user_id, rent_date, return_date) VALUES (?, ?, ?, ?, ?)";
            try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, rental.getId());
                stmt.setString(2, rental.getVehicle().getId());
                stmt.setString(3, rental.getUser().getId());
                stmt.setString(4, rental.getRentDate());
                stmt.setString(5, rental.getReturnDate());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error occurred while saving rental", e);
            }
        return rental;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM rental WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting rental", e);
        }
    }

    @Override
    public void deleteByVehicleId(String id) {
        String sql = "DELETE FROM rental WHERE vehicle_id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting rental", e);
        }

    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        String sql = "SELECT * FROM rental WHERE vehicle_id = ? AND return_date IS NULL";
        try(Connection connection = JdbcConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, vehicleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String userId = rs.getString("user_id");

                    Vehicle vehicle = getVehicleById(vehicleId);
                    User user = getUserById(userId);

                    Rental rental = Rental.builder()
                            .id(rs.getString("id"))
                            .vehicle(vehicle)
                            .user(user)
                            .rentDate(rs.getString("rent_date"))
                            .returnDate(rs.getString("return_date"))
                            .build();

                    return Optional.of(rental);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred when searching for rental", e);
        }
        return Optional.empty();
    }
}

