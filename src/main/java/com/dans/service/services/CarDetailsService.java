package com.dans.service.services;

import com.dans.service.entities.car.details.Details;
import com.dans.service.entities.car.details.Manufacturer;
import com.dans.service.entities.car.details.Model;
import com.dans.service.entities.car.details.TypeYear;
import com.dans.service.repositories.car.details.DetailsRepository;
import com.dans.service.repositories.car.details.ManufacturerRepository;
import com.dans.service.repositories.car.details.ModelRepository;
import com.dans.service.repositories.car.details.TypeYearRepository;
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

    @Autowired
    public CarDetailsService(final ManufacturerRepository manufacturerRepository, final ModelRepository modelRepository, TypeYearRepository typeYearRepository, DetailsRepository detailsRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.modelRepository = modelRepository;
        this.typeYearRepository = typeYearRepository;
        this.detailsRepository = detailsRepository;
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
        return ResponseEntity.ok(detailsRepository.findAllByTypeYear_Id(typeYearId));
    }
}
