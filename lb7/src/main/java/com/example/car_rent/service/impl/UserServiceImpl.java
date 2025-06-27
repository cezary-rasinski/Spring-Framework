package com.example.car_rent.service.impl;

import com.example.car_rent.dto.UserRequest;
import com.example.car_rent.model.Role;
import com.example.car_rent.model.User;
import com.example.car_rent.repository.RoleRepository;
import com.example.car_rent.repository.UserRepository;
import com.example.car_rent.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void register(UserRequest req) {
        if (userRepository.findByLogin(req.getLogin()).isPresent()) {
            throw new IllegalArgumentException("User already registered!");
        }
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() ->
                        new IllegalStateException("There is no role... ROLE_USER"));

        User u = User.builder()
                .id(UUID.randomUUID().toString())
                .login(req.getLogin())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles(Set.of(userRole))
                .isActive(true)
                .build();

        userRepository.save(u);
    }
    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public void deleteUser(String login) {
        userRepository.findByLogin(login).ifPresent(u -> {
                u.setActive(false);
                userRepository.save(u);
        });
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> giveRole(String login, String roleName) {
        return userRepository.findByLogin(login)
                .flatMap(u -> roleRepository.findByName(roleName)
                        .map(r -> {
                            u.getRoles().add(r);
                            return userRepository.save(u);
                        }));
    }

    @Override
    public Optional<User> takeRole(String login, String roleName) {
        return userRepository.findByLogin(login)
                .flatMap(u -> roleRepository.findByName(roleName)
                        .map(r -> {
                            u.getRoles().remove(r);
                            return userRepository.save(u);
                        }));
    }
}
