package com.example.car_rent.controller;

import com.example.car_rent.model.User;
import com.example.car_rent.service.UserService;
import com.example.car_rent.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final VehicleService vehicleService;
    private final UserService userService;

    @GetMapping("/seeAll")
    public List<User> all() {
        return userService.getUsers();
    }

    @PostMapping("/{login}/{role}")
    public ResponseEntity<User> giveRole(@PathVariable String login, @PathVariable String role) {
        userService.giveRole(login, role);
        return userService.giveRole(login, role)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/roles/{login}/{role}")
    public ResponseEntity<User> takeRole(@PathVariable String login, @PathVariable String role) {
        userService.takeRole(login, role);
        return userService.takeRole(login, role)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/admin/vehicles/{id}
    @DeleteMapping("/{login}")
    public ResponseEntity<Void> softDelete(@PathVariable String login) {
        userService.deleteUser(login);
        return ResponseEntity.noContent().build();
    }
}
