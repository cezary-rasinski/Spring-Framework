package org.example.repositories;

import org.example.model.Rental;

import java.util.List;
import java.util.Optional;

public interface RentalRepository {
    List<Rental> findAll();
    Optional<Rental> findById(String id);
    Rental save(Rental rental);

    void deleteById(String id);
    void deleteByVehicleId(String id);

    Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId);
}
