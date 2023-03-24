package com.couriercompany.courier_company_api.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptimalRoutePojo {
    private String routeSummary;
    private Long distanceInMeters;
    private Double distanceInKm;
    private String duration;
    private Double deliveryCost;
}
