package com.example.car_rent.service;

import com.example.car_rent.model.Rental;

public interface PaymentService {
    String createCheckoutSession(String rentalId);
    void handleWebhook(String payload, String signature);
    public double calculateRate(Rental rental);
}
