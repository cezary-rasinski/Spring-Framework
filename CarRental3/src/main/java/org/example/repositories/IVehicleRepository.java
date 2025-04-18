package org.example.repositories;

import org.example.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface IVehicleRepository {
    List<Vehicle> getVehicles();
    Optional<Vehicle> findByVehicleId(String id);
    Vehicle save(Vehicle vehicle);
    void deleteById(String id);
}