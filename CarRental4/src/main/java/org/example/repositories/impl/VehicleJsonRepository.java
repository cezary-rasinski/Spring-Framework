package org.example.repositories.impl;

import com.google.gson.reflect.TypeToken;
import org.example.model.Vehicle;
import org.example.repositories.VehicleRepository;
import org.example.utils.JsonFileStorage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VehicleJsonRepository implements VehicleRepository {
    private final JsonFileStorage<Vehicle> storage;
    private final List<Vehicle> vehicles;

    public VehicleJsonRepository() {
        Type userListType = new TypeToken<List<Vehicle>>() {}.getType();
        this.storage = new JsonFileStorage<>("vehicles.json", userListType);
        this.vehicles = storage.load();
    }

    @Override
    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles);
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicles.stream().filter(v -> v.getId().equals(id)).findFirst();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == null || vehicle.getId().isBlank()) {
            vehicle.setId(UUID.randomUUID().toString());
        } else {
            deleteById(vehicle.getId());
        }
        vehicles.add(vehicle);
        storage.save(vehicles);
        return vehicle;
    }

    @Override
    public void deleteById(String id) {
        vehicles.removeIf(r -> r.getId().equals(id));
        storage.save(vehicles);
    }
}
