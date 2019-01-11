package com.dans.service.test.services;

import com.dans.service.entities.Car;
import com.dans.service.entities.Role;
import com.dans.service.entities.RoleName;
import com.dans.service.entities.User;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.CarPayload;
import com.dans.service.repositories.CarRepository;
import com.dans.service.repositories.UserRepository;
import com.dans.service.services.CarService;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarServiceTest {

    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    private User user = User.builder().email("test@test.com")
            .password("password")
            .name("test")
            .username("test")
            .phoneNumber("07test")
            .role(Role.builder().name(RoleName.ROLE_USER).build())
            .build();

    private CarPayload carPayload = new CarPayload(-1L, "test", "test", "2000", 1L);

    private Car car = Car.createCarFromPayload(carPayload, user);

    @Before
    public void setUp() {
        carService = new CarService(carRepository, userRepository);
    }

    @Test
    public void getAllReturnsListWithOneElement() {
        BDDMockito.given(this.carRepository.findAllByUserId(user.getId())).willReturn(Arrays.asList(this.car));
        Assert.assertNotNull(carService.getAllCarsForCurrentUser(user.getId()).getBody());
        Assert.assertEquals(1, carService.getAllCarsForCurrentUser(user.getId()).getBody().size());
        Assert.assertTrue(carService.getAllCarsForCurrentUser(user.getId()).getBody().contains(this.car));
    }

    @Test
    public void getAllReturnsEmptyList() {
        Assert.assertTrue(carService.getAllCarsForCurrentUser(user.getId()).getBody().isEmpty());
    }

    @Test
    public void saveCarFail() {
        Assert.assertThat(carService.saveCar(carPayload), Is.is(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "UNAUTHORIZED"))));
    }

    @Test
    public void saveCarSuccess() {
        BDDMockito.given(this.userRepository.findById(carPayload.getUserId())).willReturn(Optional.of(this.user));

        Assert.assertThat(carService.saveCar(carPayload), Is.is(ResponseEntity.ok(new ApiResponse(true, "Car saved"))));
    }

    @Test
    public void updateCarFailNotFound() {
        Assert.assertThat(carService.updateCar(carPayload), Is.is(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Car not found"))));
    }

    @Test
    public void updateCarSuccess() {
        BDDMockito.given(this.carRepository.findById(BDDMockito.anyLong())).willReturn(Optional.of(this.car));

        Assert.assertThat(carService.updateCar(carPayload), Is.is(ResponseEntity.ok(new ApiResponse(true, "Car updated"))));
    }
}
