package org.example.repositories.impl;

import org.example.model.Rental;
import org.example.repositories.IRentalRepository;
import org.example.utils.JsonFileStorage;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalRepository implements IRentalRepository {
    private final JsonFileStorage<Rental> storage;
    private final List<Rental> rentals;

    public RentalRepository() {
        Type rentalListType = new TypeToken<List<Rental>>() {}.getType();
        this.storage = new JsonFileStorage<>("rentals.json", rentalListType);
        this.rentals = storage.load();
    }

    @Override
    public List<Rental> getRentals() {
        return new ArrayList<>(rentals);
    }

    @Override
    public Optional<Rental> findByRentalId(String id) {
        return rentals.stream().filter(rental -> rental.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Rental> findByUserId(String id) {
        return rentals.stream().filter(user -> user.getUserId().equals(id)).findFirst();
    }

    @Override
    public Optional<Rental> findByVehicleId(String id) {
        return rentals.stream()
                .filter(r -> r.getVehicleId().equals(id))
                .reduce((first, second) -> second);
    }

    @Override
    public Rental save(Rental rental) {
        if (rental.getId() == null || rental.getId().isBlank()) {
            rental.setId(UUID.randomUUID().toString());
        } else {
            deleteById(rental.getId());
        }
        rentals.add(rental);
        storage.save(rentals);
        return rental;
    }
    @Override
    public void deleteById(String id) {
        rentals.removeIf(r -> r.getId().equals(id));
        storage.save(rentals);
    }
    @Override
    public void deleteByVehicleId(String id) {
        rentals.removeIf(r -> r.getVehicleId().equals(id));
        storage.save(rentals);
    }
}
