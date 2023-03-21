package com.couriercompany.courier_company_api.entities;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Route {
    private Location origin;
    private Location destination;
    private List<Location> intermediateLocations;
    private double distanceInKm;
    private double cost;
}
