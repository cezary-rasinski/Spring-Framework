package org.example.repositories;

import org.example.model.User;

import java.util.List;

public interface IUserRepository {
    void save(User user);
    User getUser(String login);
    List<User> getUsers();

}
