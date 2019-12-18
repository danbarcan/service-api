package com.dans.service.payloads;

import com.dans.service.entities.Car;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CarResponsePayload {
    private Long id;
    private String manufacturer;
    private String model;
    private String typeYear;
    private String engineSize;
    private String engineCode;
    private String power;

    public static CarResponsePayload createJobResponsePayloadFromJob(Car car) {
        return CarResponsePayload.builder()
                .id(car.getId())
                .manufacturer(car.getDetails().getTypeYear().getModel().getManufacturer().getName())
                .model(car.getDetails().getTypeYear().getModel().getName())
                .typeYear(car.getDetails().getTypeYear().getName())
                .engineSize(car.getDetails().getType())
                .engineCode(car.getDetails().getEngineCode())
                .power(car.getDetails().getPower())
                .build();
    }
}
