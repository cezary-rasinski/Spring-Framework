package org.example.services;

import org.example.model.Rental;
import org.example.model.User;
import org.example.model.Vehicle;
import org.example.repositories.impl.RentalRepository;
import org.example.repositories.impl.VehicleRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class RentalService {
    private final VehicleRepository vehicleRepo;
    private final RentalRepository rentalRepo;

    public RentalService (VehicleRepository vhRep, RentalRepository reRep){
        this.vehicleRepo = vhRep;
        this.rentalRepo = reRep;
    }
    public Rental rentVehicle(User user, String vehicleId){
        Optional<Vehicle> vehicle = vehicleRepo.findByVehicleId(vehicleId);
        if (vehicle.isEmpty()){
            System.out.println("No vehicle with such id.");
            return null;
        }

        Optional<Rental> existingRental = rentalRepo.findByVehicleId(vehicleId).filter(rent -> rent.getReturnTime() == null);
        if (existingRental.isPresent()){
            System.out.println("This vehicle is already rented.");
            return null;
        }

        Rental newRental = Rental.builder()
                .vehicleId(vehicleId)
                .userId(user.getId())
                .rentTime(LocalDateTime.now().toString())
                .returnTime(null)
                .build();

        rentalRepo.save(newRental);
        System.out.println("Successful rental");
        return newRental;
    }

    public void returnVehicle(User user, String vehicleId) {
        Optional<Rental> currentlyRented = rentalRepo
                .findByVehicleId(vehicleId)
                .filter(rent -> rent.getReturnTime() == null);
        if (currentlyRented.isEmpty()){
            System.out.println("Non-existing rental.");
            return;
        }
        Rental rental = currentlyRented.get();
        if (!rental.getUserId().equals(user.getId())) {
            System.out.println("You didn't rent this vehicle.");
            return;
        }
        rental.setReturnTime(LocalDateTime.now().toString());
        System.out.println("Successful return");
        rentalRepo.save(rental);
    }
}
