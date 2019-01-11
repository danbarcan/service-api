package com.dans.service.entities;

import com.dans.service.payloads.CarPayload;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String make;

    @NotBlank
    private String model;

    private Integer year;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Car createCarFromPayload(CarPayload carPayload, User user) {
        return Car.builder()
                .make(carPayload.getMake())
                .model(carPayload.getModel())
                .year(Integer.parseInt(carPayload.getYear()))
                .user(user)
                .build();
    }

    public Car updateFieldsWithPayloadData(CarPayload carPayload) {
        this.make = carPayload.getMake();
        this.model = carPayload.getModel();
        this.year = Integer.parseInt(carPayload.getYear());

        return this;
    }
}
