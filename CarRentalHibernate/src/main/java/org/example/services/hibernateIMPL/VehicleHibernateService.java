package org.example.services.hibernateIMPL;

import org.example.HibernateConfig;
import org.example.model.Rental;
import org.example.model.Vehicle;
import org.example.repositories.hibernateIMPL.RentalHibernateRepository;
import org.example.repositories.hibernateIMPL.VehicleHibernateRepository;
import org.example.services.IVehicleService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleHibernateService implements IVehicleService {
    private final RentalHibernateRepository rentalRepo;
    private final VehicleHibernateRepository vehicleRepo;

    public VehicleHibernateService(RentalHibernateRepository rentalRepo, VehicleHibernateRepository vehicleRepo) {
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
    }

    public List<Vehicle> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            return vehicleRepo.findAll();
        }
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            return vehicleRepo.findById(id);
        }
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            vehicleRepo.setSession(session);
            Vehicle savedVehicle = vehicleRepo.save(vehicle);

            tx.commit();
            return savedVehicle;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
    public void removeVehicle(String vehicleId) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            rentalRepo.setSession(session);
            vehicleRepo.setSession(session);

            Optional<Vehicle> vehicleOpt = vehicleRepo.findById(vehicleId);
            if (vehicleOpt.isEmpty()) {
                System.out.println("Vehicle not found.");
                return;
            }

            Vehicle vehicle = vehicleOpt.get();
            if (!isAvailable(vehicleId)) {
                System.out.println("Vehicle is currently rented and cannot be removed.");
                return;
            }

            vehicleRepo.deleteById(vehicleId);

            tx.commit();
            System.out.println("Vehicle removed successfully.");
            return;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            List<Vehicle> vehicleList = vehicleRepo.findAll();
            List<Vehicle> availableVehicles = new ArrayList<>();
            for (Vehicle v : vehicleList) {
                if (isAvailable(v.getId())) {
                    availableVehicles.add(v);
                }
            }
            return availableVehicles;
        }
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            Optional<Rental> rental = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId);
            return rental.isEmpty();
        }
    }
}
