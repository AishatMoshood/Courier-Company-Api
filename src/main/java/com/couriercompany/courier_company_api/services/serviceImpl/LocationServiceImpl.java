package com.couriercompany.courier_company_api.services.serviceImpl;

import com.couriercompany.courier_company_api.dtos.GetCoordinatesResponseDto;
import com.couriercompany.courier_company_api.entities.Address;
import com.couriercompany.courier_company_api.entities.Location;
import com.couriercompany.courier_company_api.exceptions.EmptyListException;
import com.couriercompany.courier_company_api.exceptions.ResourceNotFoundException;
import com.couriercompany.courier_company_api.pojos.LocationPojo;
import com.couriercompany.courier_company_api.pojos.OptimalRoutePojo;
import com.couriercompany.courier_company_api.repositories.AddressRepository;
import com.couriercompany.courier_company_api.repositories.LocationRepository;
import com.couriercompany.courier_company_api.services.LocationService;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final AddressRepository addressRepository;

    //@Value("${api_key}")
    private final String apiKey = "";

    //@Value("${cost_per_package_per_kilometer}")
    private final double costPerKm = 1.0;

    @Override
    public String addLocation(LocationPojo addLocationPojo) throws IOException, InterruptedException, ApiException {
        GetCoordinatesResponseDto getCoordinatesResponseDto = getLocationCoordinates(addLocationPojo);

        Address address = Address.builder()
                .street(addLocationPojo.getStreet())
                .city(addLocationPojo.getCity())
                .state(addLocationPojo.getState())
                .country(addLocationPojo.getCountry())
                .build();
        addressRepository.save(address);

        Location newLocation = Location.builder()
                .name(addLocationPojo.getName())
                .latitude(getCoordinatesResponseDto.getLatitude())
                .longitude(getCoordinatesResponseDto.getLongitude())
                .locationType(addLocationPojo.getLocationType())
                .address(address)
                .build();
        locationRepository.save(newLocation);

        return "New location saved successfully";
    }

    @Override
    public String removeLocation(Long locationId){
      Location location = locationRepository.findById(locationId).orElseThrow(() ->
               new ResourceNotFoundException("Location does not exist"));
       locationRepository.delete(location);
       return "Location deleted successfully";
    }

    @Override
    public Location updateLocation(LocationPojo updateLocationPojo, Long locationId){
        Location location = locationRepository.findById(locationId).orElseThrow(() ->
                new ResourceNotFoundException("Location does not exist"));

        Address address = Address.builder()
                .street(updateLocationPojo.getStreet())
                .city(updateLocationPojo.getCity())
                .state(updateLocationPojo.getState())
                .country(updateLocationPojo.getCountry())
                .build();
        addressRepository.save(address);

        location.setName(updateLocationPojo.getName());
        location.setAddress(address);
        location.setLocationType(updateLocationPojo.getLocationType());
        locationRepository.save(location);

        return location;
    }

    @Override
    public GetCoordinatesResponseDto getLocationCoordinates(LocationPojo locationPojo) throws IOException,
            InterruptedException, ApiException {
        // initialize GeoApiContext
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();

        String locationName = locationPojo.getStreet() + ", " + locationPojo.getCity() + ", " +
                locationPojo.getState() + ", " + locationPojo.getCountry();

        // execute Geocoding API request
        GeocodingResult[] results = GeocodingApi.geocode(context, locationName).await();

        // check if results array is empty
        if (results.length == 0) {
            throw new EmptyListException("No geocoding results found for location: " + locationName);
        }

        // get latitude and longitude of first result
        LatLng location = results[0].geometry.location;
        double latitude = location.lat;
        double longitude = location.lng;

        return GetCoordinatesResponseDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    @Override
    public OptimalRoutePojo getOptimalRoute(String origin, String destination) throws Exception {

        Location locationOrigin = locationRepository.findLocationByName(origin.trim()).orElseThrow(()-> new Exception("Location origin not found."));

        Location locationDestination = locationRepository.findLocationByName(destination.trim()).orElseThrow(()-> new Exception("Location destination not found."));

        // initialize GeoApiContext
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();
//
//        // execute Geocoding API request for origin
//        GeocodingResult[] originResults = GeocodingApi.geocode(context, origin.getName()).await();
//        com.google.maps.model.LatLng originLatLng = originResults[0].geometry.location;
//
//        // execute Geocoding API request for destination
//        GeocodingResult[] destinationResults = GeocodingApi.geocode(context, destination.getName()).await();

//        LatLng lngDestination = new LatLng(6.4999, 3.3609);
//        LatLng lngOrigin = new LatLng(6.4358, 3.4447);
        LatLng lngOrigin = new LatLng(locationOrigin.getLatitude(), locationOrigin.getLatitude());
        LatLng lngDestination = new LatLng(locationDestination.getLatitude(), locationDestination.getLatitude());

        // execute Directions API request for intermediate locations
        DirectionsResult directionsResult = DirectionsApi.newRequest(context)
                .origin(lngOrigin)
                .destination(lngDestination)
                .optimizeWaypoints(Boolean.TRUE)
                .alternatives(Boolean.TRUE)
                .mode(TravelMode.DRIVING)
                .await();
//        Arrays.stream(directionsResult.routes).forEach(route -> {
//            OptimalRoutePojo optimalRoutePojo = new OptimalRoutePojo();
////            if(isBestRoute){
//            optimalRoutePojo.setDistanceInMeters(showShortestRoute(route.legs));
//
////            }else{
//            optimalRoutePojo.setRouteName(route.summary);
//
//                Arrays.stream(route.legs).forEach(leg -> {
//                    optimalRoutePojo.setDistanceInMeters(leg.distance.inMeters);
//                    optimalRoutePojo.setDistanceInKm(leg.distance.humanReadable);
//                });
//
////            }
//            optimalRoutePojoList.add(optimalRoutePojo);
//        });
        return showShortestRoute(directionsResult);
    }

    private OptimalRoutePojo showShortestRoute(DirectionsResult directionsResult){
        Map<String, Long> allRoutesBetweenOriginAndDestination = new HashMap<>();

        Arrays.stream(directionsResult.routes).forEach(route -> {
            allRoutesBetweenOriginAndDestination.put(route.summary, 0L);

            Arrays.stream(route.legs).forEach(leg -> {
                for (Map.Entry<String, Long> entry : allRoutesBetweenOriginAndDestination.entrySet()) {
                    entry.setValue(entry.getValue() + leg.distance.inMeters);
                }
            });
        });

        String minDistSummary = null;
        Long minDist = Long.MAX_VALUE;

        for (Map.Entry<String, Long> entry :allRoutesBetweenOriginAndDestination.entrySet()) {
            if (entry.getValue() < minDist) {
                minDist = entry.getValue();
                minDistSummary = entry.getKey();
            }
        }

        OptimalRoutePojo optimalRoutePojo = new OptimalRoutePojo();
        optimalRoutePojo.setDistanceInMeters(minDist);
        optimalRoutePojo.setRouteSummary(minDistSummary);
        optimalRoutePojo.setDistanceInKm((double) (minDist / 1000));
        optimalRoutePojo.setDeliveryCost(costPerKm * optimalRoutePojo.getDistanceInKm());

        return optimalRoutePojo;
    }

//    @Override
//    public Route getOptimalRoute(String originName, String destinationName) throws ApiException, InterruptedException, IOException {
//        Location origin = getLocation(originName);
//        Location destination = getLocation(destinationName);
//
//        List<Location> intermediateLocations = null;
//
//        double distanceInKm = calculateDistance(intermediateLocations);
//        double cost = costPerKm * distanceInKm;
//
//        Route route = new Route();
//        route.setOrigin(origin);
//        route.setDestination(destination);
//        route.setIntermediateLocations(intermediateLocations);
//        route.setDistanceInKm(distanceInKm);
//        route.setCost(cost);
//
//        return route;
//    }

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
