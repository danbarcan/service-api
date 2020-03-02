package com.dans.service.entities.car.details;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Type_year")
public class TypeYearLite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id")
    private ModelLite model;

    @Override
    public String toString() {
        return "TypeYear{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
