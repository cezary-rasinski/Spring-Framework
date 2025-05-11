package org.example.services;

import org.example.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface IVehicleService {
    List<Vehicle> findAll();
    Optional<Vehicle> findById(String id);
    Vehicle save(Vehicle vehicle);
    List<Vehicle> findAvailableVehicles();
    boolean isAvailable(String vehicleId);
}
