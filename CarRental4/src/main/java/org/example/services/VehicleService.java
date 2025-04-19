package org.example.services;

import org.example.model.Rental;
import org.example.model.User;
import org.example.model.Vehicle;
import org.example.repositories.RentalRepository;
import org.example.repositories.VehicleRepository;
import org.example.repositories.impl.RentalJsonRepository;
import org.example.repositories.impl.VehicleJsonRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class VehicleService {
    private final VehicleRepository vehicleRepo;
    private final RentalRepository rentalRepo;
    Scanner scanner = new Scanner(System.in);

    public VehicleService(VehicleRepository vehicleRepo, RentalRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }


    public void addVehicle(User user) {
        if (user.getRole().equalsIgnoreCase("USER")){
            System.out.println("You don't have access to this feature");
            return;
        }
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter model: ");
        String model = scanner.nextLine();
        System.out.print("Enter year: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Plate: ");
        String plate = scanner.nextLine();

        Vehicle vehicle = Vehicle.builder()
                .category(category)
                .brand(brand)
                .model(model)
                .year(year)
                .plate(plate)
                .build();

        Optional<Vehicle> existingVehicle = vehicleRepo.findById(vehicle.getId());
        if (existingVehicle.isPresent()){
            System.out.println("This vehicle is already in the list.");
            return;
        }
        vehicleRepo.save(vehicle);
        System.out.println("Successfully added a new vehicle");

    }
    public void removeVehicle(User user){
        if (user.getRole().equalsIgnoreCase("USER")){
            System.out.println("You don't have access to this feature");
            return;
        }
        System.out.println("Enter ID of vehicle you want to remove");
        String vehicleID = scanner.nextLine();
        Optional<Rental> ongoingRental = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleID);
        if (ongoingRental.isEmpty()){
            rentalRepo.deleteByVehicleId(vehicleID);
            vehicleRepo.deleteById(vehicleID);
            System.out.println("Successfully removed a vehicle");
        } else {
            System.out.println("Vehicle doesn't exist or is currently being rented");
        }
    }
    public void seeAllVehicles(User user){
        if (user.getRole().equalsIgnoreCase("USER")){
            System.out.println("You don't have access to this feature");
            return;
        }
        List<Vehicle> vehicleList = vehicleRepo.findAll();
        vehicleList.forEach(System.out::println);
    }
    public void seeNonRented(){
        List<Vehicle> vehicleList = vehicleRepo.findAll();
        for (Vehicle v :vehicleList){
            Optional<Rental> rent = rentalRepo.findByVehicleIdAndReturnDateIsNull(v.getId());
            if (rent.isEmpty()) {
                System.out.println(v.toString());
            }
        }
    }
}
