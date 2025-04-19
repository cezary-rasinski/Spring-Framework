package org.example.app;

import org.example.model.User;
import org.example.services.AuthService;
import org.example.services.RentalService;
import org.example.services.VehicleService;

import java.util.Scanner;

public class App {

    private final AuthService authService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final Scanner scanner = new Scanner(System.in);

    public App(AuthService authService, VehicleService vehicleService, RentalService rentalService) {
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void run() {
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
                    if (newUser != null) {
                        System.out.println("Successful registration");
                        userRun(newUser);
                    }
                    break;
                case "2":
                    System.out.println("Enter login");
                    String logLogin = scanner.nextLine();
                    System.out.println("Enter password");
                    String logPassword = scanner.nextLine();
                    user = authService.login(logLogin, logPassword);
                    if (user != null) {
                        userRun(user);
                    }
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Enter a valid option");
            }
        }
    }
    private void userRun(User user){
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

