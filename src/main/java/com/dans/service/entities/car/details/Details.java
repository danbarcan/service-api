package com.dans.service.entities.car.details;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "details")
public class Details implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fuel;
    private String type;
    private String fromDate;
    private String toDate;
    private String body;
    private String capacity;
    private String power;
    private String engineCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_year_id")
    private TypeYearLite typeYear;

    @Override
    public String toString() {
        return "Details{" +
                "id=" + id +
                ", fuel='" + fuel + '\'' +
                ", type='" + type + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", body='" + body + '\'' +
                ", capacity='" + capacity + '\'' +
                ", power='" + power + '\'' +
                ", engineCode='" + engineCode + '\'' +
                '}';
    }
}
