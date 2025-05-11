package org.example.repositories.jsonIMPL;

import com.google.gson.reflect.TypeToken;
import org.example.model.User;
import org.example.repositories.UserRepository;
import org.example.utils.JsonFileStorage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserJsonRepository implements UserRepository {

    private final JsonFileStorage<User> storage;
    private final List<User> users;

    public UserJsonRepository() {
        Type userListType = new TypeToken<List<User>>() {}.getType();
        this.storage = new JsonFileStorage<>("users.json", userListType);
        this.users = storage.load();
    }
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public Optional<User> findById(String id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.stream().filter(user -> user.getLogin().equals(login)).findFirst();
    }

    @Override
    public User save(User user) {
        if (user != null) {
            if (user.getId() == null) {
                user.setId(UUID.randomUUID().toString());
            } else {
                deleteById(user.getId());
            }
            users.add(user);
            storage.save(users);
            return user;
        }
        return null;
    }
    @Override
    public void deleteById(String id) {
        users.removeIf(r -> r.getId().equals(id));
        storage.save(users);
    }
}
