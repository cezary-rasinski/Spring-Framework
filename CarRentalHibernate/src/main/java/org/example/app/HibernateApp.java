package org.example.app;

import org.example.model.User;
import org.example.model.Vehicle;
import org.example.services.hibernateIMPL.AuthHibernateService;
import org.example.services.hibernateIMPL.RentalHibernateService;
import org.example.services.hibernateIMPL.VehicleHibernateService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class HibernateApp {
    private final AuthHibernateService authService;
    private final VehicleHibernateService vehicleService;
    private final RentalHibernateService rentalService;
    private final Scanner scanner = new Scanner(System.in);
    public HibernateApp(AuthHibernateService authService, VehicleHibernateService vehicleService, RentalHibernateService rentalService) {
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
                    boolean registered = authService.register(regLogin, regPassword, role);
                    if (registered) {
                        System.out.println("Successful registration");
                        user = authService.login(regLogin, regPassword).orElse(null);
                        if (user != null) {
                            userRun(user);
                        }
                    } else {
                        System.out.println("User already exists");
                    }
                    break;
                case "2":
                    System.out.println("Enter login");
                    String logLogin = scanner.nextLine();
                    System.out.println("Enter password");
                    String logPassword = scanner.nextLine();
                    user = authService.login(logLogin, logPassword).orElse(null);
                    if (user != null) {
                        userRun(user);
                    } else {
                        System.out.println("Invalid login or password");
                    }
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Enter a valid option");
            }
        }
    }

    private void userRun(User user) {
        while (true) {
            System.out.println("1. Rent a vehicle\n" + "2. Return a vehicle\n" + "3. See available vehicles\n"
                    + "4. Add a vehicle\n" + "5. Remove a vehicle\n" + "6. See all vehicles\n" + "7. Log out\n");

            String choice2 = scanner.nextLine();

            switch (choice2) {
                case "1":
                    System.out.println("Enter ID of vehicle you want to rent");
                    String rentVehicleID = scanner.nextLine();
                    rentalService.rent(rentVehicleID, user.getId());
                    break;
                case "2":
                    System.out.println("Enter ID of vehicle you want to return");
                    String returnVehicleID = scanner.nextLine();
                    rentalService.returnRental(returnVehicleID, user.getId());
                    break;
                case "3":
                    vehicleService.findAvailableVehicles().forEach(System.out::println);
                    break;
                case "4":
                    Vehicle vehicle = buildVehicle();
                    vehicleService.save(vehicle);
                    break;
                case "5":
                    System.out.println("Enter ID of vehicle you want to delete");
                    String deleteVehicleID = scanner.nextLine();
                    vehicleService.removeVehicle(deleteVehicleID);
                    break;
                case "6":
                    vehicleService.findAll().forEach(System.out::println);
                    break;
                case "7":
                    System.out.println("Logged out user: " + user.getLogin());
                    return;
                default:
                    System.out.println("Enter a valid option");
            }
        }
    }
    private Vehicle buildVehicle(){
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
                .id(UUID.randomUUID().toString())
                .category(category)
                .brand(brand)
                .model(model)
                .year(year)
                .plate(plate)
                .build();
        return vehicle;
    }
}
