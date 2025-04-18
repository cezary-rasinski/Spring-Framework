package org.example.repositories;

import org.example.model.User;
import org.example.model.Vehicle;

import java.util.List;

public interface IVehicleRepository {
    void rentVehicle(String id);
    void returnVehicle(String id);

    //void getVehicle(String id);
    void removeVehicle(String id, User user);
    void addVehicle(Vehicle v, User user);
    List<Vehicle> getVehicles();
    void save();
}