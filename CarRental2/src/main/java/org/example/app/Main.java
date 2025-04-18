package org.example.app;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.model.Authentication;
import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.User;
import org.example.repositories.UserRepository;
import org.example.repositories.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final VehicleRepository repository = new VehicleRepository();

    private static final UserRepository userRepo = new UserRepository();


    public static void main(String[] args) {
          List<User> users = userRepo.getUsers();
          User user2 = users.get(3);
          User user = users.get(0);
          userRepo.getAllUsersInfo(user2);
          repository.removeVehicle("5",user2);
          repository.removeVehicle("6",user);
          String userInfo = userRepo.getUserDetails(user);
          System.out.println(userInfo);
          Authentication auth = new Authentication();
          user.authenticate(auth, "133");
          repository.rentVehicleToUser("1", user2);


        while (true) {
            System.out.println("1. Show available vehicles\n2. Rent vehicle\n3. Return vehicle\n4. Exit");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    repository.getVehicles().forEach(System.out::println);
                    break;
                case "2":
                    System.out.print("Enter ID of vehicle you want to rent ");
                    repository.rentVehicle(scanner.nextLine());
                    break;
                case "3":
                    System.out.print("Enter ID of vehicle you want to return ");
                    repository.returnVehicle(scanner.nextLine());
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Enter a valid case");
            }
        }
    }
}

