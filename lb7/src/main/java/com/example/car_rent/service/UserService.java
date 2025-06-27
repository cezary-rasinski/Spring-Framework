package com.example.car_rent.service;

import com.example.car_rent.dto.UserRequest;
import com.example.car_rent.model.Role;
import com.example.car_rent.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void register(UserRequest req);
    Optional<User> findByLogin(String login);

    void deleteUser(String login);
    List<User> getUsers();
    Optional<User> giveRole(String login, String role);
    Optional<User> takeRole(String login, String role);
}
