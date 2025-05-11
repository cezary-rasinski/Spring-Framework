package org.example.services.hibernateIMPL;

import org.example.HibernateConfig;
import org.example.model.User;
import org.example.repositories.hibernateIMPL.UserHibernateRepository;
import org.example.services.IAuthService;
import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.UUID;

public class AuthHibernateService implements IAuthService {
    private final UserHibernateRepository userRepo;

    public AuthHibernateService(UserHibernateRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean register(String login, String rawPassword, String role) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            session.beginTransaction();
            userRepo.setSession(session);

            if (userRepo.findByLogin(login).isPresent()) {
                System.out.println("User already exists");
                return false;
            }

            String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
            if (role == null || role.isBlank()) role = "user";

            User user = User.builder()
                    .id(UUID.randomUUID().toString())
                    .login(login)
                    .password(hashed)
                    .role(role)
                    .build();

            userRepo.save(user);
            session.getTransaction().commit();
            System.out.println("User registered");
            return true;
        }
    }

    @Override
    public Optional<User> login(String login, String rawPassword) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            userRepo.setSession(session);

            Optional<User> found = userRepo.findByLogin(login);
            if (found.isPresent()) {
                User user = found.get();
                boolean passwordMatches = BCrypt.checkpw(rawPassword, user.getPassword());
                if (passwordMatches) {
                    System.out.println("Logged in " + user.getLogin());
                    return found;
                } else {
                    System.out.println("Wrong password");
                }
            } else {
                System.out.println("No matching username");
            }
            return Optional.empty();
        }
    }

}
