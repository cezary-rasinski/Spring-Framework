package com.example.car_rent.controller;

import com.example.car_rent.dto.RentalRequest;
import com.example.car_rent.model.Rental;
import com.example.car_rent.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;
    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public List<Rental> getRentals(){
        return rentalService.findAll();
    }
    @GetMapping("/active/{id}")
    public  ResponseEntity<Rental> getActiveRentalDetails(@PathVariable String id){
        return rentalService.findActiveRentalByVehicleId(id)
                .map(r -> ResponseEntity.ok(r))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/rent")
    public ResponseEntity<Rental> rentVehicle(@RequestBody RentalRequest rentalRequest) {
        if (rentalRequest.vehicleId == null || rentalRequest.userId == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Rental rental = rentalService.rent(rentalRequest.vehicleId, rentalRequest.userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(rental);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/return")
    public ResponseEntity<Void> returnVehicle(@RequestBody RentalRequest rentalRequest) {
        if (rentalRequest.getVehicleId() == null || rentalRequest.getUserId() == null) {
            return ResponseEntity.badRequest().build();
        }
        boolean returnedRental = rentalService.returnRental(rentalRequest.getVehicleId(), rentalRequest.getUserId());
        return returnedRental ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
