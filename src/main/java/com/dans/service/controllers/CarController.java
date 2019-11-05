package com.dans.service.controllers;

import com.dans.service.entities.Car;
import com.dans.service.payloads.CarPayload;
import com.dans.service.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(final CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/users/cars")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Car>> getAllCars(@RequestParam Long userId) {
        return carService.getAllCarsForCurrentUser(userId);
    }

    @GetMapping("/users/deleteCar")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Car>> deleteCar(@RequestParam Long carId) {
        return carService.deleteCar(carId);
    }

    @PostMapping("/users/car")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Car>> saveCar(@Valid @RequestBody CarPayload carPayload) {
        return carService.saveCar(carPayload);
    }

    @PostMapping("/users/updateCar")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Car>> updateCar(@Valid @RequestBody CarPayload carPayload) {
        return carService.updateCar(carPayload);
    }
}
