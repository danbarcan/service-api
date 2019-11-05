package com.dans.service.services;

import com.dans.service.entities.Car;
import com.dans.service.entities.User;
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

    public ResponseEntity<List<Car>> saveCar(CarPayload carPayload) {
        Optional<User> user = userRepository.findById(carPayload.getUserId());

        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Car car = Car.createCarFromPayload(carPayload, user.get());

        carRepository.save(car);

        return ResponseEntity.ok(carRepository.findAllByUserId(user.get().getId()));
    }

    public ResponseEntity<List<Car>> updateCar(CarPayload carPayload) {
        Optional<Car> carOptional = carRepository.findById(carPayload.getId());

        if (!carOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Car car = carOptional.get();

        car.updateFieldsWithPayloadData(carPayload);

        carRepository.save(car);

        return ResponseEntity.ok(carRepository.findAllByUserId(car.getUser().getId()));
    }

    public ResponseEntity<List<Car>> deleteCar(Long carId) {
        Optional<Car> carOptional = carRepository.findById(carId);

        if (!carOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Car car = carOptional.get();
        carRepository.delete(car);

        return ResponseEntity.ok(carRepository.findAllByUserId(car.getUser().getId()));
    }
}
