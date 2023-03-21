package com.couriercompany.courier_company_api.services;

import com.couriercompany.courier_company_api.entities.Location;
import com.couriercompany.courier_company_api.entities.Route;
import com.couriercompany.courier_company_api.pojos.OptimalDistancePojo;
import com.google.maps.errors.ApiException;

import java.io.IOException;
import java.util.List;

public interface LocationService {
    Route getOptimalRoute(String originName, String destinationName) throws ApiException, InterruptedException, IOException;

    Location getLocation(String locationName) throws ApiException, InterruptedException, IOException;

    List<OptimalDistancePojo> getIntermediateLocations(String origin, String destination, Boolean isBestRoute) throws Exception;

    double calculateDistance(List<Location> locations);

    double getDistanceInKm(Location location1, Location location2);
}
