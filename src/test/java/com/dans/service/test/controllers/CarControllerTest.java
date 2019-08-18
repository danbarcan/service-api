package com.dans.service.test.controllers;

import com.dans.service.controllers.CarController;
import com.dans.service.entities.Car;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.CarPayload;
import com.dans.service.services.CarService;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTest {

    private CarController carController;

    @Mock
    private CarService carService;

    @Mock
    private CarPayload carPayload;

    @Before
    public void setUp() {
        this.carController = new CarController(carService);
    }

    @Test
    public void saveCar() {
        BDDMockito.given(this.carService.saveCar(this.carPayload)).willReturn(ResponseEntity.ok(new ApiResponse(true, "Car saved")));

        Assert.assertThat(this.carController.saveCar(this.carPayload), Is.is(ResponseEntity.ok(new ApiResponse(true, "Car saved"))));
    }

    @Test
    public void updateCar() {
        BDDMockito.given(this.carService.updateCar(this.carPayload)).willReturn(ResponseEntity.ok(new ApiResponse(true, "Car updated")));

        Assert.assertThat(this.carController.updateCar(this.carPayload), Is.is(ResponseEntity.ok(new ApiResponse(true, "Car updated"))));
    }

    @Test
    public void getAllCars() {
        BDDMockito.given(this.carService.getAllCarsForCurrentUser(BDDMockito.anyLong())).willReturn(ResponseEntity.ok(Arrays.asList(new Car())));

        Assert.assertThat(this.carController.getAllCars(1L), Is.is(ResponseEntity.ok(Arrays.asList(new Car()))));
    }
}
