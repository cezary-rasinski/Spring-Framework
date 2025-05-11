package org.example.app;

import org.example.HibernateConfig;
import org.example.repositories.RentalRepository;
import org.example.repositories.UserRepository;
import org.example.repositories.VehicleRepository;
import org.example.repositories.dbIMPL.RentalJdbcRepository;
import org.example.repositories.dbIMPL.UserJdbcRepository;
import org.example.repositories.dbIMPL.VehicleJdbcRepository;
import org.example.repositories.hibernateIMPL.RentalHibernateRepository;
import org.example.repositories.hibernateIMPL.UserHibernateRepository;
import org.example.repositories.hibernateIMPL.VehicleHibernateRepository;
import org.example.repositories.jsonIMPL.RentalJsonRepository;
import org.example.repositories.jsonIMPL.UserJsonRepository;
import org.example.repositories.jsonIMPL.VehicleJsonRepository;
import org.example.services.hibernateIMPL.AuthHibernateService;
import org.example.services.hibernateIMPL.VehicleHibernateService;
import org.example.services.simpleIMPL.AuthService;
import org.example.services.hibernateIMPL.RentalHibernateService;
import org.example.services.simpleIMPL.RentalService;
import org.example.services.simpleIMPL.VehicleService;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {

        UserRepository userRepo;
        VehicleRepository vehicleRepo;
        RentalRepository rentalRepo;

        String storageType = "hibernate";
//        if (args.length > 0){
//            storageType = args[0];
//        } else {
//            storageType="json";
//        }

        switch (storageType) {
            case "jdbc" -> {
                userRepo = new UserJdbcRepository();
                vehicleRepo = new VehicleJdbcRepository();
                rentalRepo = new RentalJdbcRepository();

                VehicleService vehicleService = new VehicleService(vehicleRepo, rentalRepo);
                RentalService rentalService = new RentalService(vehicleRepo, rentalRepo);
                AuthService authService = new AuthService(userRepo);
                App app = new App(authService, vehicleService, rentalService);
                app.run();
            }
            case "json" -> {
                userRepo = new UserJsonRepository();
                vehicleRepo = new VehicleJsonRepository();
                rentalRepo = new RentalJsonRepository();

                VehicleService vehicleService = new VehicleService(vehicleRepo, rentalRepo);
                RentalService rentalService = new RentalService(vehicleRepo, rentalRepo);
                AuthService authService = new AuthService(userRepo);
                App app = new App(authService, vehicleService, rentalService);
                app.run();
            }
            case "hibernate" -> {
                Session session = HibernateConfig.getSessionFactory().openSession();

                VehicleHibernateRepository VehicleRepo = new VehicleHibernateRepository(session);
                RentalHibernateRepository RentalRepo = new RentalHibernateRepository(session);
                UserHibernateRepository UserRepo = new UserHibernateRepository(session);

                VehicleHibernateService vehicleHibernateService = new VehicleHibernateService(RentalRepo, VehicleRepo);
                RentalHibernateService rentalHibernateService = new RentalHibernateService(RentalRepo, VehicleRepo, UserRepo);
                AuthHibernateService authHibernateService = new AuthHibernateService(UserRepo);

                HibernateApp app = new HibernateApp(authHibernateService, vehicleHibernateService, rentalHibernateService);
                app.run();
            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }

    }
}

