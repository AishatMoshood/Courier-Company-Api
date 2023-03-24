package com.couriercompany.courier_company_api.services;

import com.couriercompany.courier_company_api.dtos.GetCoordinatesResponseDto;
import com.couriercompany.courier_company_api.entities.Location;
import com.couriercompany.courier_company_api.entities.Route;
import com.couriercompany.courier_company_api.pojos.LocationPojo;
import com.couriercompany.courier_company_api.pojos.OptimalRoutePojo;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

import java.io.IOException;
import java.util.List;

public interface LocationService {
    String removeLocation(Long locationId);

    Location updateLocation(LocationPojo updateLocationPojo, Long locationId);

    GetCoordinatesResponseDto getLocationCoordinates(LocationPojo locationPojo) throws IOException, InterruptedException, ApiException;

    String addLocation(LocationPojo locationPojo) throws IOException, InterruptedException, ApiException;

    OptimalRoutePojo getOptimalRoute(String originName, String destinationName) throws Exception;

    Location getLocation(String locationName) throws ApiException, InterruptedException, IOException;


    double calculateDistance(List<Location> locations);

    double getDistanceInKm(Location location1, Location location2);
}
