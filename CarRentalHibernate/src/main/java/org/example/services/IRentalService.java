package org.example.services;

import org.example.model.Rental;

import java.util.List;

public interface IRentalService {
    boolean isVehicleRented(String vehicleId);
    Rental rent(String vehicleId, String userId);
    boolean returnRental(String vehicleId, String userId);
    List<Rental> findAll();
}
