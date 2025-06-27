package com.example.car_rent.service.impl;

import com.example.car_rent.model.Vehicle;
import com.example.car_rent.repository.RentalRepository;
import com.example.car_rent.repository.VehicleRepository;
import com.example.car_rent.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository,
                              RentalRepository rentalRepository) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAllActive() {
        return vehicleRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vehicle> findById(String id) {
        return vehicleRepository.findById(id);
    }

    @Override
    @Transactional
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == null || vehicle.getId().isBlank()) {
            vehicle.setId(UUID.randomUUID().toString());
            vehicle.setActive(true);
        }
        return vehicleRepository.save(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAvailableVehicles() {
        Set<String> rentedIDs = rentalRepository.findRentedVehicleIds();
        if (rentedIDs.isEmpty()) {
            return vehicleRepository.findByIsActiveTrue();
        }
        return vehicleRepository.findByIsActiveTrueAndIdNotIn(rentedIDs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findRentedVehicles() {
        Set<String> rentedIDs = rentalRepository.findRentedVehicleIds();
        if (rentedIDs.isEmpty()) {
            return List.of();
        }
        return vehicleRepository.findAllById(rentedIDs);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAvailable(String vehicleId) {
        return vehicleRepository.findByIdAndIsActiveTrue(vehicleId).isPresent() && !rentalRepository.existsByVehicleIdAndReturnDateIsNull(vehicleId);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        vehicleRepository.findById(id).ifPresent(v -> {
            v.setActive(false);
            vehicleRepository.save(v);
        });
    }
}
