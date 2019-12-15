package com.dans.service.entities.car.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "details")
public class Details {
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_year_id")
    private TypeYear typeYear;

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
