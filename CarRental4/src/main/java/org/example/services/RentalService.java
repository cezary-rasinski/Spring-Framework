package org.example.services;

import org.example.model.Rental;
import org.example.model.User;
import org.example.model.Vehicle;
import org.example.repositories.RentalRepository;
import org.example.repositories.VehicleRepository;
import org.example.repositories.impl.RentalJsonRepository;
import org.example.repositories.impl.VehicleJsonRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class RentalService {
    private final VehicleRepository vehicleRepo;
    private final RentalRepository rentalRepo;

    public RentalService (VehicleRepository vhRep, RentalRepository reRep){
        this.vehicleRepo = vhRep;
        this.rentalRepo = reRep;
    }
    public Rental rentVehicle(User user, String vehicleId){
        Optional<Rental> existingRental = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId);
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
        Optional<Rental> currentlyRented = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId);
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
