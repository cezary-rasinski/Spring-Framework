package org.example.repositories;

import org.example.model.User;
import org.example.model.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
        private List<User> users = new ArrayList<>();
        private static final String FILE_NAME = "password.csv";

        public UserRepository() {
            load();
        }

        private void load() {
            try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(";");
                    User u = new User(data[0], data[1], data[2]);
                    users.add(u);
                }
            } catch (IOException e) {
                System.out.println("Error");
            }
        }

        public User getUser(String login) {
            return users.stream().filter(u -> u.getLogin().equals(login)).findFirst().orElse(null);
        }

        public List<User> getUsers() { return users; }

        public void save(User user) { users.add(user); }

//        public void addVehicle(User user, Vehicle vehicle) {
//            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
//                vehicleRepository.addVehicle(vehicle);
//                System.out.println("Vehicle added: " + vehicle);
//            }
//        }
//
//        public void removeVehicle(User user, String vehicleId) {
//            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
//                vehicleRepository.removeVehicle(vehicleId);
//                System.out.println("Vehicle removed: " + vehicleId);
//            }
//        }
        public void getAllUsersInfo(User admin) {
            if ("ADMIN".equalsIgnoreCase(admin.getRole())) {
                for (User u: users){
                    System.out.println(u.getLogin() + ' ' + u.getRole() + ' ' + u.getUserVehicle());
                }
            } else {
                System.out.println("Lack of access to all user data");
            }
        }
        public String getUserDetails(User user) {
            StringBuilder details = new StringBuilder();
            details.append("Login: ").append(user.getLogin()).append("\n");
            details.append("Role: ").append(user.getRole()).append("\n");

            if (user.getUserVehicle() != null) {
                details.append("Rented Vehicle: ").append(user.getUserVehicle().getId()).append("\n");
            } else {
                details.append("No vehicle rented.\n");
            }
            return details.toString();
        }

    public void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            users.forEach(u -> pw.println(u.toCSV()));
        } catch (IOException e) {
            System.out.println("Error in writing to file");
        }
    }
}
