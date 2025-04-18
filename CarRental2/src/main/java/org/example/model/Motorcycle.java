package org.example.model;

import java.util.Objects;

public class Motorcycle extends Vehicle {
    private String category;

    public Motorcycle(String id, String brand, String model, int year, double price, String category) {
        super(id, brand, model, year, price);
        this.category = category;
    }

    public Motorcycle (Motorcycle copy){
        super(copy);
        this.category = copy.category;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) return false;

        Motorcycle obj = (Motorcycle) object;
        return super.equals(object) && Objects.equals(this.category, obj.category);
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), category);
    }

    public String toCSV() {
        return super.toCSV() + ";" + category;
    }
    public String toString() {
         return super.toString() + " - Category: " + category;
     }
}
