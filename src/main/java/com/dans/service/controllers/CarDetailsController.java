package com.dans.service.controllers;

import com.dans.service.entities.car.details.*;
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

    @GetMapping("/public/liteManufacturers")
    public ResponseEntity<List<ManufacturerLite>> getAllManufacturersLite() {
        return carDetailsService.getAllManufacturersLite();
    }

    @GetMapping("/public/liteModelsByManufacturerId")
    public ResponseEntity<List<ModelLite>> getAllModelsByManufacturerLiteId(@RequestParam Long manufacturerId) {
        return carDetailsService.getAllCarModelsByManufacturerLite(manufacturerId);
    }

    @GetMapping("/public/liteTypeYearsByModelId")
    public ResponseEntity<List<TypeYearLite>> getAllTypeYearsByModelLiteId(@RequestParam Long modelId) {
        return carDetailsService.getAllTypeYearsByModelLite(modelId);
    }

    @GetMapping("/public/allDetailsByTypeYearId")
    public ResponseEntity<List<Details>> getAllDetailsByTypeYearId(@RequestParam Long typeYearId) {
        return carDetailsService.getAllCarDetailsByTypeYear(typeYearId);
    }
}
