package com.dans.service.payloads;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class ServiceProfilePayload {

    private Long id;

    private String serviceName;

    private String serviceAddress;

    private String cui;

    private String companyDescription;

    private BigDecimal lat;

    private BigDecimal lng;

    private Long[] categories;

    private String image;
}
