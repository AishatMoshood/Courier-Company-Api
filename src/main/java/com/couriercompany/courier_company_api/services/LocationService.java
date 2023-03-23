package com.couriercompany.courier_company_api.services;

import com.couriercompany.courier_company_api.dtos.GetCoordinatesResponseDto;
import com.couriercompany.courier_company_api.entities.Location;
import com.couriercompany.courier_company_api.entities.Route;
import com.couriercompany.courier_company_api.pojos.GetCoordinatesPojo;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

import java.io.IOException;
import java.util.List;

public interface LocationService {
    GetCoordinatesResponseDto getLocationCoordinates(GetCoordinatesPojo getCoordinatesPojo) throws IOException, InterruptedException, ApiException;

    String addLocation(GetCoordinatesPojo getCoordinatesPojo) throws IOException, InterruptedException, ApiException;

    Route getOptimalRoute(String originName, String destinationName) throws ApiException, InterruptedException, IOException;

    Location getLocation(String locationName) throws ApiException, InterruptedException, IOException;

    DirectionsResult getIntermediateLocations(String origin, String destination2) throws Exception;

    double calculateDistance(List<Location> locations);

    double getDistanceInKm(Location location1, Location location2);
}
