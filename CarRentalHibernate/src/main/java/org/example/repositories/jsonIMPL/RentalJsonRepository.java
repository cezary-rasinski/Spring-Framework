package org.example.repositories.jsonIMPL;

import org.example.model.Rental;
import org.example.repositories.RentalRepository;
import org.example.utils.JsonFileStorage;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalJsonRepository implements RentalRepository {
    private final JsonFileStorage<Rental> storage;
    private final List<Rental> rentals;

    public RentalJsonRepository() {
        Type rentalListType = new TypeToken<List<Rental>>() {}.getType();
        this.storage = new JsonFileStorage<>("rentals.json", rentalListType);
        this.rentals = storage.load();
    }

    @Override
    public List<Rental> findAll() {
        return new ArrayList<>(rentals);
    }

    @Override
    public Optional<Rental> findById(String id) {
        return rentals.stream().filter(rental -> rental.getId().equals(id)).findFirst();
    }

    public Optional<Rental> findByUserId(String id) {
        return rentals.stream().filter(user -> user.getId().equals(id)).findFirst();
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
        rentals.removeIf(r -> r.getVehicle().getId().equals(id));
        storage.save(rentals);
    }
    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        return rentals.stream()
                .filter(r -> r.getVehicle() != null && r.getVehicle().getId().equals(vehicleId))
                .filter(r -> r.getReturnDate() == null)
                .findFirst();
    }
}
