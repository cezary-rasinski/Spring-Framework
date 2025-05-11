package org.example.services.hibernateIMPL;

import org.example.HibernateConfig;
import org.example.model.Rental;
import org.example.model.User;
import org.example.model.Vehicle;
import org.example.repositories.hibernateIMPL.RentalHibernateRepository;
import org.example.repositories.hibernateIMPL.UserHibernateRepository;
import org.example.repositories.hibernateIMPL.VehicleHibernateRepository;
import org.example.services.IRentalService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalHibernateService implements IRentalService {
    private final RentalHibernateRepository rentalRepo;
    private final VehicleHibernateRepository vehicleRepo;
    private final UserHibernateRepository userRepo;

    public RentalHibernateService(RentalHibernateRepository rentalRepo, VehicleHibernateRepository vehicleRepo,
                                  UserHibernateRepository userRepo) {
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
        }
    }

    @Override
    public Rental rent(String vehicleId, String userId) {
        Transaction tx = null;
        Session session = null;
        try {
            // Open a new session
            session = HibernateConfig.getSessionFactory().openSession();
            tx = session.beginTransaction();

            vehicleRepo.setSession(session);
            userRepo.setSession(session);
            rentalRepo.setSession(session);

            if (this.isVehicleRented(vehicleId)) {
                throw new IllegalStateException("Vehicle is rented");
            }
            rentalRepo.setSession(session);

            Vehicle vehicle = vehicleRepo.findById(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Rental rental = Rental.builder()
                    .id(UUID.randomUUID().toString())
                    .vehicle(vehicle)
                    .user(user)
                    .rentDate(LocalDateTime.now().toString())
                    .returnDate(null)
                    .build();

            rentalRepo.save(rental);

            tx.commit();

            return rental;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error during rental process: " + e.getMessage());
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    @Override
    public boolean returnRental(String vehicleId, String userId) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            rentalRepo.setSession(session);
            vehicleRepo.setSession(session);
            userRepo.setSession(session);

            Optional<Rental> ongoingRentalOpt = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId);
            if (ongoingRentalOpt.isEmpty()) {
                throw new RuntimeException("No active rental found for this vehicle.");
            }
            Rental ongoingRental = ongoingRentalOpt.get();

            if (!ongoingRental.getUser().getId().equals(userId)) {
                throw new RuntimeException("This rental is not associated with the provided user.");
            }

            ongoingRental.setReturnDate(LocalDateTime.now().toString());

            rentalRepo.save(ongoingRental);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Rental> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            return rentalRepo.findAll();
        }
    }
}