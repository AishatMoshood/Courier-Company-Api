package com.couriercompany.courier_company_api.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptimalDistancePojo {
    private String summary;
    private Long distanceInMeters;
    private String distanceInKm;
}
