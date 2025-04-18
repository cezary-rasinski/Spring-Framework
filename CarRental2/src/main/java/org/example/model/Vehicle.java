package org.example.model;

import java.util.Objects;

public abstract class Vehicle {

    protected String brand, model;
    protected int year;
    protected double price;
    protected boolean rented;
    protected String id;

    public Vehicle (String id, String brand, String model, int year, double price) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = false;
    }

    public Vehicle (Vehicle copy) {
        this.id = copy.id;
        this.brand = copy.brand;
        this.model = copy.model;
        this.year = copy.year;
        this.price = copy.price;
        this.rented = copy.rented;
    }

    @Override
    public boolean equals(Object object){
        if (this == object) {return true;}
        if (object == null || getClass() != object.getClass()) return false;

        Vehicle obj = (Vehicle) object;
        return Objects.equals(this.id, obj.id) && Objects.equals(this.brand, obj.brand)
                && Objects.equals(this.model, obj.model) && this.year == obj.year && Double.compare(this.price, obj.price) == 0
                && this.rented == obj.rented;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, year, price, rented);
    }

    public String toCSV() {
        return String.join(";", id, brand, model, String.valueOf(year), String.valueOf(price), String.valueOf(rented));
    }

    public String toString() {
        return "ID:" + id + " " + brand + " " + model + " (" + year + ") - " + (rented ? "Rented out." : "Available.") + " Price: " + price;
    }

    public void rent() { rented = true; }
    public void returnVehicle() { rented = false; }
    public String getId() { return id; }
    public boolean isRented() { return rented; }

}
