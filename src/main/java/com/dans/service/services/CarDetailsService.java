package com.dans.service.services;

import com.dans.service.entities.car.details.*;
import com.dans.service.repositories.car.details.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarDetailsService {

    private ManufacturerRepository manufacturerRepository;
    private ModelRepository modelRepository;
    private TypeYearRepository typeYearRepository;
    private DetailsRepository detailsRepository;

    private ManufacturerLiteRepository manufacturerLiteRepository;
    private ModelLiteRepository modelLiteRepository;
    private TypeYearLiteRepository typeYearLiteRepository;

    @Autowired
    public CarDetailsService(ManufacturerRepository manufacturerRepository, ModelRepository modelRepository, TypeYearRepository typeYearRepository, DetailsRepository detailsRepository, ManufacturerLiteRepository manufacturerLiteRepository, ModelLiteRepository modelLiteRepository, TypeYearLiteRepository typeYearLiteRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.modelRepository = modelRepository;
        this.typeYearRepository = typeYearRepository;
        this.detailsRepository = detailsRepository;
        this.manufacturerLiteRepository = manufacturerLiteRepository;
        this.modelLiteRepository = modelLiteRepository;
        this.typeYearLiteRepository = typeYearLiteRepository;
    }

    public ResponseEntity<List<Manufacturer>> getAllCarDetails() {
        return ResponseEntity.ok(manufacturerRepository.findAll());
    }

    public ResponseEntity<List<Model>> getAllCarModelsByManufacturer(long manufacturerId) {
        return ResponseEntity.ok(modelRepository.findAllByManufacturer_Id(manufacturerId));
    }

    public ResponseEntity<List<TypeYear>> getAllTypeYearsByModel(long modelId) {
        return ResponseEntity.ok(typeYearRepository.findAllByModel_Id(modelId));
    }

    public ResponseEntity<List<Details>> getAllCarDetailsByTypeYear(long typeYearId) {
        return ResponseEntity.ok(detailsRepository.findAllByTypeYear_IdOrderByType(typeYearId));
    }

    public ResponseEntity<List<ManufacturerLite>> getAllManufacturersLite() {
        return ResponseEntity.ok(manufacturerLiteRepository.findAllByOrderByName());
    }

    public ResponseEntity<List<ModelLite>> getAllCarModelsByManufacturerLite(long manufacturerId) {
        return ResponseEntity.ok(modelLiteRepository.findAllByManufacturer_IdOrderByName(manufacturerId));
    }

    public ResponseEntity<List<TypeYearLite>> getAllTypeYearsByModelLite(long modelId) {
        return ResponseEntity.ok(typeYearLiteRepository.findAllByModel_IdOrderByName(modelId));
    }
}
