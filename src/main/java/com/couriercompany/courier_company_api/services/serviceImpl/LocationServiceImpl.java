package com.couriercompany.courier_company_api.services.serviceImpl;

import com.couriercompany.courier_company_api.dtos.GetCoordinatesResponseDto;
import com.couriercompany.courier_company_api.entities.Address;
import com.couriercompany.courier_company_api.entities.Location;
import com.couriercompany.courier_company_api.entities.Route;
import com.couriercompany.courier_company_api.exceptions.EmptyListException;
import com.couriercompany.courier_company_api.exceptions.InvalidOperationException;
import com.couriercompany.courier_company_api.pojos.GetCoordinatesPojo;
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
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final AddressRepository addressRepository;

    //@Value("${api_key}")
    private final String apiKey = " ";

    //@Value("${cost_per_package_per_kilometer}")
    private final double costPerKm = 1.0;

    @Override
    public String addLocation(GetCoordinatesPojo getCoordinatesPojo) throws IOException, InterruptedException, ApiException {
        if(getCoordinatesPojo.getAddress().equals("") || getCoordinatesPojo.getAddress().equals("") || getCoordinatesPojo.getAddress().equals("") || getCoordinatesPojo.getAddress().equals(""))
            throw new InvalidOperationException("Please fill in all address fields to add a new location");


        GetCoordinatesResponseDto getCoordinatesResponseDto = getLocationCoordinates(getCoordinatesPojo);

        Address address = Address.builder()
                .street(getCoordinatesPojo.getAddress().getStreet())
                .city(getCoordinatesPojo.getAddress().getCity())
                .state(getCoordinatesPojo.getAddress().getState())
                .country(getCoordinatesPojo.getAddress().getCountry())
                .build();
        addressRepository.save(address);

        Location newLocation = Location.builder()
                .latitude(getCoordinatesResponseDto.getLatitude())
                .longitude(getCoordinatesResponseDto.getLongitude())
                .locationType(getCoordinatesPojo.getLocationType())
                .address(address)
                .build();
        locationRepository.save(newLocation);

        return "New location saved successfully";
    }



    @Override
    public GetCoordinatesResponseDto getLocationCoordinates(GetCoordinatesPojo getCoordinatesPojo) throws IOException, InterruptedException, ApiException {
        // initialize GeoApiContext
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();

        String locationName = getCoordinatesPojo.getAddress().getStreet() + ", " + getCoordinatesPojo.getAddress().getCity() + ", " + getCoordinatesPojo.getAddress().getState() + ", " + getCoordinatesPojo.getAddress().getCountry();

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

        GetCoordinatesResponseDto getCoordinatesResponseDto = GetCoordinatesResponseDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();

        return getCoordinatesResponseDto;
    }


    @Override
    public Route getOptimalRoute(String originName, String destinationName) throws ApiException, InterruptedException, IOException {
        Location origin = getLocation(originName);
        Location destination = getLocation(destinationName);

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
    public  DirectionsResult getIntermediateLocations(String origin, String destination) throws
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

        // execute Directions API request for intermediate locations
        DirectionsResult directionsResult = DirectionsApi.newRequest(context)
                .origin(lngOrigin)
                .destination(lngDestination)
                .optimizeWaypoints(Boolean.TRUE)
                .alternatives(Boolean.TRUE)
                .mode(TravelMode.DRIVING)
                .await();

        List<OptimalRoutePojo> optimalRoutePojoList = new ArrayList<>();

//        Arrays.stream(directionsResult.routes).forEach(route -> {
//            OptimalDistancePojo optimalDistancePojo = new OptimalDistancePojo();
////            if(isBestRoute){
//                optimalDistancePojo.setDistanceInMeters(showShortestRoute(route.legs));
//
////            }else{
//                optimalDistancePojo.setRouteName(route.summary);
//
//                Arrays.stream(route.legs).forEach(leg -> {
//                    optimalDistancePojo.setDistanceInMeters(leg.distance.inMeters);
//                    optimalDistancePojo.setDistanceInKm(leg.distance.humanReadable);
//                });
//
////            }
//            optimalDistancePojoList.add(optimalDistancePojo);
//        });

        return directionsResult;
    }

//    private Long showShortestRoute(DirectionsResult directionsResult, DirectionsLeg[] directionsLegs){
//        OptimalDistancePojo optimalDistancePojo = new OptimalDistancePojo();
//        Long distanceInMeters = directionsLegs[0].distance.inMeters;
//
//        for(int i = 1; i < directionsLegs.length; i++){
//            if(directionsLegs[i].distance.inMeters < distanceInMeters)
//                distanceInMeters = directionsLegs[i].distance.inMeters;
//
////                optimalDistancePojo.setSummary(directionsLegs[i].);
//                optimalDistancePojo.setDistanceInKm(distanceInMeters / 1000);
//                optimalDistancePojo.setDuration(String.valueOf(directionsLegs[i].durationInTraffic));
//        }
//
//
//        optimalDistancePojo.setDistanceInMeters();
//        return directionsLeg;
//    }

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
