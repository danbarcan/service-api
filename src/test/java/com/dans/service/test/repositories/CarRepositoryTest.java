package com.dans.service.test.repositories;

import com.dans.service.entities.Car;
import com.dans.service.entities.User;
import com.dans.service.repositories.CarRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    private User user = User.builder().email("test@test.com")
            .password("password")
            .name("test")
            .username("test")
            .phoneNumber("07test")
            .build();

    private Car car = Car.builder()
            .brand("Mercedes")
            .km(1000L)
            .user(user)
            .build();

    @Test
    public void findAllShouldReturnCars() {
        this.entityManager.persist(user);
        this.entityManager.persist(car);
        List<Car> cars = carRepository.findAll();
        Assertions.assertThat(cars != null && cars.size() == 1).isTrue();
    }

    @Test
    public void findAllShouldReturnNull() {
        List<Car> cars = carRepository.findAll();
        Assertions.assertThat(cars != null && cars.size() == 1).isFalse();
    }

    @Test
    public void findByIdShouldReturnCar() {
        this.entityManager.persist(user);
        this.entityManager.persist(car);
        List<Car> cars = this.carRepository.findAll();
        Car car1 = cars.get(0);
        Optional<Car> car = carRepository.findById(car1.getId());
        Assertions.assertThat(car.isPresent()).isTrue();
        Assertions.assertThat(car.get().equals(car1)).isTrue();
    }

    @Test
    public void findByIdShouldReturnNull() {
        Optional<Car> car = carRepository.findById(-1L);
        Assertions.assertThat(car.isPresent()).isFalse();
    }
}
