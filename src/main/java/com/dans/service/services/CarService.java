package com.dans.service.services;

import com.dans.service.entities.Car;
import com.dans.service.entities.User;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.CarPayload;
import com.dans.service.repositories.CarRepository;
import com.dans.service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<List<Car>> getAllCarsForCurrentUser(Long userId) {
        return ResponseEntity.ok(carRepository.findAllByUserId(userId));
    }

    public ResponseEntity<ApiResponse> saveCar(CarPayload carPayload) {
        Optional<User> user = userRepository.findById(carPayload.getUserId());

        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "UNAUTHORIZED"));
        }
        Car car = Car.createCarFromPayload(carPayload, user.get());

        carRepository.save(car);

        return ResponseEntity.ok(new ApiResponse(true, "Car saved"));
    }

    public ResponseEntity<ApiResponse> updateCar(CarPayload carPayload) {
        Optional<Car> carOptional = carRepository.findById(carPayload.getId());

        if (!carOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Car not found"));
        }

        Car car = carOptional.get();

        car.updateFieldsWithPayloadData(carPayload);

        carRepository.save(car);

        return ResponseEntity.ok(new ApiResponse(true, "Car updated"));
    }
}
