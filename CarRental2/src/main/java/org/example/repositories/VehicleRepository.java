package org.example.repositories;

import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.User;
import org.example.model.Vehicle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository implements IVehicleRepository {
    private List<Vehicle> vehicles = new ArrayList<>();
    private static final String FILE_NAME = "vehicles.csv";

    public VehicleRepository() {
        load();
    }

    private void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                Vehicle v = data.length == 7 ? new Motorcycle(data[0], data[1], data[2], Integer.parseInt(data[3]), Double.parseDouble(data[4]), data[6]) : new Car(data[0], data[1], data[2], Integer.parseInt(data[3]), Double.parseDouble(data[4]));
                if (Boolean.parseBoolean(data[5])) v.rent();
                vehicles.add(v);
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }
    public void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            vehicles.forEach(v -> pw.println(v.toCSV()));
        } catch (IOException e) {
            System.out.println("Error in writing to file");
        }
    }
    public void returnVehicle(String id) {
        vehicles.stream()
                .filter(v -> v.getId().equals(id) && v.isRented())
                .findFirst()
                .ifPresent(v -> {
                    v.returnVehicle();
                    save();
                });
    }

    public void rentVehicle(String id) {
        vehicles.stream()
                .filter(v -> v.getId().equals(id) && !v.isRented())
                .findFirst()
                .ifPresent(v -> {
                    v.rent();
                    save();
                });
    }
    public void rentVehicleToUser(String vehicleId, User user) {
        Vehicle vehicle = vehicles.stream()
                .filter(v -> v.getId().equals(vehicleId) && !v.isRented())
                .findFirst()
                .orElse(null);

        if (vehicle != null) {
            vehicle.rent();
            user.setUserVehicle(vehicle);
            save();
            System.out.println("Vehicle " + vehicleId + " rented to user " + user.getLogin());
        } else {
            System.out.println("Vehicle not available or already rented.");
        }
    }

    public void addVehicle(Vehicle v, User user){
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            if (v instanceof Car) {
                vehicles.add(new Car((Car)v));
            } else if (v instanceof Motorcycle){
                vehicles.add(new Motorcycle((Motorcycle)v));
            }
            save();
        }
    }

    public void removeVehicle(String id, User user){
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            vehicles.removeIf(v -> v.getId().equals(id));
        } else {
            System.out.println("Lack of access!");
        }

    }

    public List<Vehicle> getVehicles() {
        List<Vehicle> copy = new ArrayList<>();

        for (Vehicle v: vehicles){
            if (v instanceof Car){
                copy.add(new Car((Car)v));
            } else if (v instanceof Motorcycle){
                copy.add(new Motorcycle((Motorcycle)v));
            }
        }
        return copy;
    }
}

