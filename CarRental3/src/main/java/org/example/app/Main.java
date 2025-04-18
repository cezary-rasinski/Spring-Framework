package org.example.app;

import org.example.model.User;
import org.example.model.Vehicle;
import org.example.repositories.impl.RentalRepository;
import org.example.repositories.impl.UserRepository;
import org.example.repositories.impl.VehicleRepository;
import org.example.services.AuthenticationService;
import org.example.services.RentalService;
import org.example.services.VehicleService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        UserRepository userRepo = new UserRepository();
        VehicleRepository vehicleRepo = new VehicleRepository();
        RentalRepository rentalRepo = new RentalRepository();

        AuthenticationService authService = new AuthenticationService(userRepo);
        VehicleService vehicleService = new VehicleService(vehicleRepo, rentalRepo);
        RentalService rentalService = new RentalService(vehicleRepo, rentalRepo);
        Scanner scanner = new Scanner(System.in);

        User user = null;

        while (true) {
            System.out.println("1. Register\n" +
                    "2. Login\n" +
                    "3. Exit\n");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Enter login");
                    String regLogin = scanner.nextLine();
                    System.out.println("Enter password");
                    String regPassword = scanner.nextLine();
                    System.out.println("Enter role");
                    String role = scanner.nextLine();
                    User newUser = authService.register(regLogin, regPassword, role);
                    if (newUser != null){
                        System.out.println("Successful registration");
                    }
                    break;
                case "2":
                    System.out.println("Enter login");
                    String logLogin = scanner.nextLine();
                    System.out.println("Enter password");
                    String logPassword = scanner.nextLine();
                    user = authService.login(logLogin, logPassword);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Enter a valid option");
            }
            if (user != null){
                while (true){
                    System.out.println("1. Rent a vehicle\n" + "2. Return a vehicle\n" + "3. See available vehicles\n"
                    + "4. Add a vehicle\n" + "5. Remove a vehicle\n" + "6. See all vehicles\n" + "7. Log out\n");

                    String choice2 = scanner.nextLine();

                    switch (choice2) {
                        case "1":
                            System.out.println("Enter ID of vehicle u want to rent");
                            String rentVehicleID = scanner.nextLine();
                            rentalService.rentVehicle(user, rentVehicleID);
                            break;
                        case "2":
                            System.out.println("Enter ID of vehicle u want to return");
                            String returnVehicleID = scanner.nextLine();
                            rentalService.returnVehicle(user, returnVehicleID);
                            break;
                        case "3":
                            vehicleService.seeNonRented();
                            break;
                        case "4":
                            vehicleService.addVehicle(user);
                            break;
                        case "5":

                            vehicleService.removeVehicle(user);
                            break;
                        case "6":
                            vehicleService.seeAllVehicles(user);
                            break;
                        case "7":
                            System.out.println("Logged out user: " + user.getLogin());
                            return;
                        default:
                            System.out.println("Enter a valid option");
                    }
                }
            }
        }
    }
}

