package org.example.app;

import org.example.repositories.RentalRepository;
import org.example.repositories.UserRepository;
import org.example.repositories.VehicleRepository;
import org.example.repositories.dbIMPL.RentalJdbcRepository;
import org.example.repositories.dbIMPL.UserJdbcRepository;
import org.example.repositories.dbIMPL.VehicleJdbcRepository;
import org.example.repositories.impl.RentalJsonRepository;
import org.example.repositories.impl.UserJsonRepository;
import org.example.repositories.impl.VehicleJsonRepository;
import org.example.services.AuthService;
import org.example.services.RentalService;
import org.example.services.VehicleService;

public class Main {
    public static void main(String[] args) {

        UserRepository userRepo;
        VehicleRepository vehicleRepo;
        RentalRepository rentalRepo;

        String storageType = "json";
        if (args.length > 0){
            storageType = args[0];
        } else {
            storageType="json";
        }

        switch (storageType) {
            case "jdbc" -> {
                userRepo = new UserJdbcRepository();
                vehicleRepo = new VehicleJdbcRepository();
                rentalRepo = new RentalJdbcRepository();

            }
            case "json" -> {
                userRepo = new UserJsonRepository();
                vehicleRepo = new VehicleJsonRepository();
                rentalRepo = new RentalJsonRepository();

            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
        VehicleService vehicleService = new VehicleService(vehicleRepo, rentalRepo);
        RentalService rentalService = new RentalService(vehicleRepo, rentalRepo);


        AuthService authService = new AuthService(userRepo);

        App app = new App(authService, vehicleService, rentalService);


        app.run();
    }
}

