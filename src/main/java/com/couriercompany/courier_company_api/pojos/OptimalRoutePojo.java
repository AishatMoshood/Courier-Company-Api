package com.couriercompany.courier_company_api.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptimalRoutePojo {
    private String routeName;
    private Long distanceInMeters;
    private Long distanceInKm;
    private String duration;
    private Double deliveryCost;
}
