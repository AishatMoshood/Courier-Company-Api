package com.couriercompany.courier_company_api.pojos;

import com.couriercompany.courier_company_api.entities.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptimalRoutePojo {
    private String originName;
    private String destinationName;
    private String routeSummary;
    private Double distanceInMeters;
    private Double distanceInKm;
    private String duration;
    private Double deliveryCost;
}
