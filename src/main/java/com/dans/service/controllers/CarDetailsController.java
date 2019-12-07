package com.dans.service.controllers;

import com.dans.service.entities.car.details.Details;
import com.dans.service.entities.car.details.Manufacturer;
import com.dans.service.entities.car.details.Model;
import com.dans.service.entities.car.details.TypeYear;
import com.dans.service.services.CarDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CarDetailsController {

    private CarDetailsService carDetailsService;

    @Autowired
    public CarDetailsController(final CarDetailsService carDetailsService) {
        this.carDetailsService = carDetailsService;
    }

    @GetMapping("/public/allCars")
    public ResponseEntity<List<Manufacturer>> getAllCars() {
        return carDetailsService.getAllCarDetails();
    }

    @GetMapping("/public/allModelsByManufacturerId")
    public ResponseEntity<List<Model>> getAllModelsByManufacturerId(@RequestParam Long manufacturerId) {
        return carDetailsService.getAllCarModelsByManufacturer(manufacturerId);
    }

    @GetMapping("/public/allTypeYearsByModelId")
    public ResponseEntity<List<TypeYear>> getAllTypeYearsByModelId(@RequestParam Long modelId) {
        return carDetailsService.getAllTypeYearsByModel(modelId);
    }

    @GetMapping("/public/allDetailsByTypeYearId")
    public ResponseEntity<List<Details>> getAllDetailsByTypeYearId(@RequestParam Long typeYearId) {
        return carDetailsService.getAllCarDetailsByTypeYear(typeYearId);
    }
}
