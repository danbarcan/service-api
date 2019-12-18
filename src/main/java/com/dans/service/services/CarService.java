package com.dans.service.services;

import com.dans.service.entities.Car;
import com.dans.service.entities.User;
import com.dans.service.entities.car.details.Details;
import com.dans.service.payloads.CarPayload;
import com.dans.service.repositories.CarRepository;
import com.dans.service.repositories.JobRepository;
import com.dans.service.repositories.UserRepository;
import com.dans.service.repositories.car.details.DetailsRepository;
import com.dans.service.security.UserPrincipal;
import com.dans.service.utils.AppUtils;
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
    private DetailsRepository detailsRepository;
    private JobRepository jobRepository;

    @Autowired
    public CarService(final CarRepository carRepository, final UserRepository userRepository, final DetailsRepository detailsRepository, final JobRepository jobRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.detailsRepository = detailsRepository;
        this.jobRepository = jobRepository;
    }

    public ResponseEntity<List<Car>> getAllCarsForCurrentUser(Long userId) {
        return ResponseEntity.ok(carRepository.findAllByUserId(userId));
    }

    public ResponseEntity<List<Car>> saveCar(CarPayload carPayload) {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<Details> details = detailsRepository.findById(carPayload.getDetailsId());
        if (!details.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Car car = Car.builder()
                .user(user.get())
                .details(details.get())
                .build();

        carRepository.save(car);

        return ResponseEntity.ok(carRepository.findAllByUserId(user.get().getId()));
    }

    public ResponseEntity<List<Car>> updateCar(CarPayload carPayload) {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<Car> carOptional = carRepository.findById(carPayload.getId());
        if (!carOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Car car = carOptional.get();

        if (!car.getUser().getId().equals(user.get().getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (!car.getDetails().getId().equals(carPayload.getDetailsId())){
            Optional<Details> details = detailsRepository.findById(carPayload.getDetailsId());

            if (!details.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            car.setDetails(details.get());

            carRepository.save(car);
        }

        return ResponseEntity.ok(carRepository.findAllByUserId(car.getUser().getId()));
    }

    public ResponseEntity<List<Car>> deleteCar(Long carId) {
        Optional<Car> carOptional = carRepository.findById(carId);

        if (!carOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Car car = carOptional.get();
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        if (!userPrincipal.getId().equals(car.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        car.getJobs().forEach(job -> jobRepository.delete(job));
        carRepository.deleteById(carId);

        return ResponseEntity.ok(carRepository.findAllByUserId(car.getUser().getId()));
    }
}
