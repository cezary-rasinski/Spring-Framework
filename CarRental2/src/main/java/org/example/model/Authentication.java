package org.example.model;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Authentication {
    private List<User> users;

    private static final String FILE_NAME = "password.csv";

    public Authentication() {
        this.users = new ArrayList<>();
        load();
    }

    private void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                User u = new User(data[0], data[1], data[2]);
                users.add(u);
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    public boolean login(String login, String password) {
        String hashedPassword = DigestUtils.sha256Hex(password);
        for (User u : users){
            if (u.getLogin().equals(login) && u.getPassword().equals(hashedPassword)) {
                return true;
            }
        }
        return false;
    }

}
