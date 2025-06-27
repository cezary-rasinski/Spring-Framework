package com.example.car_rent.controller;

import com.example.car_rent.dto.RentalRequest;
import com.example.car_rent.model.Rental;
import com.example.car_rent.model.User;
import com.example.car_rent.repository.UserRepository;
import com.example.car_rent.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;
    private final UserRepository userRepository;

    @Autowired
    public RentalController(RentalService rentalService, UserRepository userRepository) {
        this.rentalService = rentalService;
        this.userRepository = userRepository;
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
    public ResponseEntity<Rental> rentVehicle(@RequestBody RentalRequest rentalRequest, @AuthenticationPrincipal UserDetails userDetails ) {
        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login) //Bez CustomUserDetails potrzebne repozytorium lub serwis od naszego Usera
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownik nie znaleziony: " + login));
        Rental rental = rentalService.rent(rentalRequest.getVehicleId(), user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }
    @PostMapping("/return")
    public ResponseEntity<Rental> returnVehicle(@RequestBody RentalRequest rentalRequest, @AuthenticationPrincipal UserDetails userDetails) {
        // 1. Identify user from JWT
        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + login)
                );
        // 2. Delegate to service: find the active rental for this vehicle & user, then close it
        Optional<Rental> rental = rentalService.returnRental(rentalRequest.getVehicleId(), user.getId());
        return rental.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
