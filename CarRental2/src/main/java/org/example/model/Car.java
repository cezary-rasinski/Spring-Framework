package org.example.model;

public class Car extends Vehicle {
    public Car(String id, String brand, String model, int year, double price) {
        super(id, brand, model, year, price);
    }

    public Car(Car copy){
        super(copy);
    }
}