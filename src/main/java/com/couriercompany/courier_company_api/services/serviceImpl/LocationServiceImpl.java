package com.couriercompany.courier_company_api.services.serviceImpl;

import com.couriercompany.courier_company_api.entities.Location;
import com.couriercompany.courier_company_api.entities.Route;
import com.couriercompany.courier_company_api.enums.LocationType;
import com.couriercompany.courier_company_api.pojos.OptimalDistancePojo;
import com.couriercompany.courier_company_api.repositories.LocationRepository;
import com.couriercompany.courier_company_api.services.LocationService;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    //@Value("${api_key}")
    private final String apiKey = "";

    //@Value("${cost_per_package_per_kilometer}")
    private final double costPerKm = 1.0;


    @Override
    public Route getOptimalRoute(String originName, String destinationName) throws ApiException, InterruptedException, IOException {
        Location origin = getLocation(originName);
        Location destination = getLocation(destinationName);

//        List<Location> intermediateLocations = getIntermediateLocations(origin, destination);
        List<Location> intermediateLocations = null;

        double distanceInKm = calculateDistance(intermediateLocations);
        double cost = costPerKm * distanceInKm;

        Route route = new Route();
        route.setOrigin(origin);
        route.setDestination(destination);
        route.setIntermediateLocations(intermediateLocations);
        route.setDistanceInKm(distanceInKm);
        route.setCost(cost);

        return route;
    }


    @Override
    public Location getLocation(String locationName) throws ApiException, InterruptedException, IOException {
        // initialize GeoApiContext
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();

        // execute Geocoding API request
        GeocodingResult[] results = GeocodingApi.geocode(context, locationName).await();

        // check if results array is empty
        if (results.length == 0) {
            throw new IllegalArgumentException("No geocoding results found for location: " + locationName);
        }

        // get latitude and longitude of first result
        LatLng location = results[0].geometry.location;
        double latitude = location.lat;
        double longitude = location.lng;

        return new Location(locationName, latitude, longitude);
    }

    @Override
    public  List<OptimalDistancePojo> getIntermediateLocations(String origin, String destination, Boolean isBestRoute) throws
            Exception {

//        Location locationOrigin = locationRepository.findLocationByName(origin).orElseThrow(()-> new Exception("Location origin not found."));
//
//        Location locationDestination = locationRepository.findLocationByName(destination).orElseThrow(()-> new Exception("Location destination not found."));

        // initialize GeoApiContext
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();

//        // execute Geocoding API request for origin
//        GeocodingResult[] originResults = GeocodingApi.geocode(context, origin.getName()).await();
//        com.google.maps.model.LatLng originLatLng = originResults[0].geometry.location;
//
//        // execute Geocoding API request for destination
//        GeocodingResult[] destinationResults = GeocodingApi.geocode(context, destination.getName()).await();

        LatLng lngDestination = new LatLng(6.4999, 3.3609);
        LatLng lngOrigin = new LatLng(6.4358, 3.4447);

//        com.google.maps.model.LatLng destinationLatLng = destinationResults[0].geometry.location;

        // execute Directions API request for intermediate locations
        DirectionsResult directionsResult = DirectionsApi.newRequest(context)
                .origin(lngOrigin)
                .destination(lngDestination)
                .optimizeWaypoints(Boolean.TRUE)
                .alternatives(Boolean.TRUE)
                .mode(TravelMode.DRIVING)
                .await();

        List<OptimalDistancePojo> optimalDistancePojoList = new ArrayList<>();

        Arrays.stream(directionsResult.routes).forEach(v -> {
            OptimalDistancePojo optimalDistancePojo = new OptimalDistancePojo();
            if(isBestRoute){
                optimalDistancePojo.setDistanceInMeters(showShortestRoute(v.legs));

            }else{
                optimalDistancePojo.setSummary(v.summary);

                Arrays.stream(v.legs).forEach(y -> {
                    optimalDistancePojo.setDistanceInMeters(y.distance.inMeters);
                    optimalDistancePojo.setDistanceInKm(y.distance.humanReadable);
                });

            }
            optimalDistancePojoList.add(optimalDistancePojo);
        });

        return optimalDistancePojoList;
    }

    private Long showShortestRoute(DirectionsLeg[] directionsLegs){
        Long directionsLeg = directionsLegs[0].distance.inMeters;

        for(int i = 1; i < directionsLegs.length; i++){
            if(directionsLegs[i].distance.inMeters < directionsLeg)
                directionsLeg = directionsLegs[i].distance.inMeters;
        }
        return directionsLeg;
    }

    @Override
    public double calculateDistance(List<Location> locations) {
        double totalDistance = 0.0;

        for (int i = 0; i < locations.size() - 1; i++) {
            Location location1 = locations.get(i);
            Location location2 = locations.get(i + 1);
            double distance = getDistanceInKm(location1, location2);
            totalDistance += distance;
        }

        return totalDistance;
    }

    @Override
    public double getDistanceInKm(Location location1, Location location2) {
        double lat1 = location1.getLatitude();
        double lon1 = location1.getLongitude();
        double lat2 = location2.getLatitude();
        double lon2 = location2.getLongitude();

        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515 * 1.609344;
        return dist;
    }
}
