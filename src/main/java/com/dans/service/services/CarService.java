package com.dans.service.services;

import com.dans.service.entities.Car;
import com.dans.service.entities.User;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.CarPayload;
import com.dans.service.repositories.CarRepository;
import com.dans.service.repositories.UserRepository;
import com.dans.service.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private CarRepository carRepository;
    private UserRepository userRepository;

    @Autowired
    public CarService(final CarRepository carRepository, final UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<List<Car>> getAllCarsForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(carRepository.findAllByUserId(((UserPrincipal) auth.getPrincipal()).getId()));
    }

    public ResponseEntity<ApiResponse> saveCar(CarPayload carPayload) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findById(((UserPrincipal) auth.getPrincipal()).getId());

        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "UNAUTHORIZED"));
        }
        Car car = Car.createCarFromPayload(carPayload, user.get());

        carRepository.save(car);

        return ResponseEntity.ok(new ApiResponse(true, "Car saved"));
    }
}
