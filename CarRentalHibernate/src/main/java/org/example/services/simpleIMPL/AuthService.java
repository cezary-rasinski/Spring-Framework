package org.example.services.simpleIMPL;


import org.example.model.User;
import org.example.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;

public class AuthService {
    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User register(String login, String password, String role){
        Optional<User> existingUser = userRepo.findByLogin(login);
        if (existingUser.isPresent()){
            System.out.println("You have already registered.");
            return null;
        }
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = User.builder()
                .login(login)
                .password(hashed)
                .role(role)
                .build();
        userRepo.save(newUser);
        return newUser;
    }
    public User login(String login, String password) {
        Optional<User> existingUser = userRepo.findByLogin(login);
        if (existingUser.isEmpty()){
            System.out.println("This user doesn't exist.");
            return null;
        }

        if (BCrypt.checkpw(password, existingUser.get().getPassword())) {
            System.out.println("Successful log in.");
            return existingUser.get();
        } else {
            System.out.println("Incorrect password.");
            return null;
        }

    }
}
