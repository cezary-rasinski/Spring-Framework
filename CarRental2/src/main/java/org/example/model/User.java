package org.example.model;

import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;

public class User {
    protected String login;
    protected String password;
    protected String role;

    protected Vehicle userVehicle;

    public User(String login, String password, String role) {
        this.login = login;
        this.password = DigestUtils.sha256Hex(password);
        this.role = role;
        this.userVehicle = null;
    }
    public String getRole() {return role;}
    public String getLogin() {return login;}
    public String getPassword() {return password;}
    public Vehicle getUserVehicle() {return userVehicle;}

    public void setUserVehicle(Vehicle vehicle){
        this.userVehicle = vehicle;
    }
    public boolean returnVehicle(){
        if (userVehicle != null){
            userVehicle.returnVehicle();
            userVehicle = null;
            return true;
        }
        return false;
    }
    public void authenticate(Authentication auth, String password) {
        if (auth.login(this.login, password)) {
            System.out.println("Successful Login");
        } else {
            System.out.println("Login Failed");
        }
    }

    @Override
    public String toString() {
        return "User: " + login + ", Role: " + role + ", Rented Vehicle: " +
                (userVehicle != null ? userVehicle : "None");
    }

    public String toCSV() {
        return String.join(";", login, password, role, (userVehicle != null ? userVehicle.getId() : "null"));
    }
}
