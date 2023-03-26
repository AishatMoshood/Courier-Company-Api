package com.couriercompany.courier_company_api.services.serviceImpl;

import com.couriercompany.courier_company_api.dtos.GetCoordinatesResponseDto;
import com.couriercompany.courier_company_api.dtos.LocationResponseDto;
import com.couriercompany.courier_company_api.entities.Address;
import com.couriercompany.courier_company_api.entities.Location;
import com.couriercompany.courier_company_api.enums.LocationType;
import com.couriercompany.courier_company_api.exceptions.*;
import com.couriercompany.courier_company_api.exceptions.InputMismatchException;
import com.couriercompany.courier_company_api.pojos.LocationPojo;
import com.couriercompany.courier_company_api.pojos.OptimalRoutePojo;
import com.couriercompany.courier_company_api.repositories.AddressRepository;
import com.couriercompany.courier_company_api.repositories.LocationRepository;
import com.couriercompany.courier_company_api.services.LocationService;
import com.couriercompany.courier_company_api.services.PersonService;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final AddressRepository addressRepository;
    private final PersonService personService;
    private static final String apiKey = "";
    private static final double costPerKm = 1.0;

    @Override
    public String addLocation(LocationPojo addLocationPojo) throws IOException, InterruptedException, ApiException {
        if(!personService.confirmAuthority()) throw new UnauthorizedException("You are NOT AUTHORIZED to perform this operation");

        if(locationRepository.existsByName(addLocationPojo.getName().trim().toUpperCase())) {throw new AlreadyExistsException("This location name already exists");}

        Address address = Address.builder()
                .street(addLocationPojo.getStreet().trim().toUpperCase())
                .city(addLocationPojo.getCity().trim().toUpperCase())
                .state(addLocationPojo.getState().trim().toUpperCase())
                .country(addLocationPojo.getCountry().trim().toUpperCase())
                .build();

        addressRepository.save(address);
        if(locationRepository.existsByAddress(address)) {throw new AlreadyExistsException("This location address already exists");}

        GetCoordinatesResponseDto getCoordinatesResponseDto = getLocationCoordinates(addLocationPojo);

        Location newLocation = Location.builder()
                .name(addLocationPojo.getName().trim().toUpperCase())
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
        if(!personService.confirmAuthority()) throw new UnauthorizedException("You are NOT AUTHORIZED to perform this operation");

      Location location = locationRepository.findById(locationId).orElseThrow(() ->
               new ResourceNotFoundException("Location does not exist"));
       locationRepository.delete(location);

       return "Location deleted successfully";
    }

    @Override
    public String updateLocation(LocationPojo updateLocationPojo, Long locationId) throws IOException, InterruptedException, ApiException {
        if(!personService.confirmAuthority()) throw new UnauthorizedException("You are NOT AUTHORIZED to perform this operation");

        Location location = locationRepository.findById(locationId).orElseThrow(() ->
                new ResourceNotFoundException("Location does not exist"));
        Address address = addressRepository.findByLocationId(locationId).orElseThrow(() ->
                new ResourceNotFoundException("Location does not exist"));

        GetCoordinatesResponseDto getCoordinatesResponseDto = getLocationCoordinates(updateLocationPojo);

        address.setStreet(updateLocationPojo.getStreet().trim().toUpperCase());
        address.setCity(updateLocationPojo.getCity().trim().toUpperCase());
        address.setState(updateLocationPojo.getState().trim().toUpperCase());
        address.setCountry(updateLocationPojo.getCountry().trim().toUpperCase());
        addressRepository.save(address);

        location.setName(updateLocationPojo.getName().trim().toUpperCase());
        location.setAddress(address);
        location.setLatitude(getCoordinatesResponseDto.getLatitude());
        location.setLongitude(getCoordinatesResponseDto.getLongitude());
        location.setLocationType(updateLocationPojo.getLocationType());
        locationRepository.save(location);

        return "Location updated successfully";
    }

    @Override
    public Page<LocationResponseDto> getAllLocations(Integer pageNo, Integer pageSize, String sortingField, boolean isAscending){
        if(!personService.confirmAuthority()) throw new UnauthorizedException("You are NOT AUTHORIZED to perform this operation");

        if(locationRepository.findAll().isEmpty()) throw new EmptyListException("No locations found");

        return locationRepository.findAll(PageRequest.of(pageNo, pageSize,
                isAscending ? Sort.Direction.ASC : Sort.Direction.DESC, sortingField)).map(this::responseMapper);
    }

    @Override
    public OptimalRoutePojo getOptimalRoute(String origin, String destination) throws Exception {
        if(!personService.confirmAuthority()) throw new UnauthorizedException("You are NOT AUTHORIZED to perform this operation");

        if(locationRepository.findAll().isEmpty()) throw new EmptyListException("No locations found, add at least 3 locations to generate an optimal route, so as to start delivery");

        if(locationRepository.findAll().size() < 3) throw new EmptyListException("Add at least 3 locations to generate an optimal route, so as to start delivery");

        if(origin.equals("") || destination.equals("")) throw new CannotBeEmptyException("Origin or Location name cannot be empty");

        Location locationOrigin = locationRepository.findLocationByName(origin.trim().toUpperCase()).orElseThrow(()-> new NotAvailableException("Location origin not found"));

        if(locationOrigin.getLocationType().equals(LocationType.DESTINATION)) throw new InputMismatchException("This location is not an origin");

        if(origin.equalsIgnoreCase(destination)) throw new InputMismatchException("Origin and destination cannot be the same");

        Location locationDestination = locationRepository.findLocationByName(destination.trim().toUpperCase()).orElseThrow(()-> new NotAvailableException("Location destination not found."));

        if(locationDestination.getLocationType().equals(LocationType.ORIGIN)) throw new InputMismatchException("This location cannot be a destination, as it is an origin");

        // initialize GeoApiContext
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();

        LatLng lngOrigin = new LatLng(locationOrigin.getLatitude(), locationOrigin.getLongitude());
        LatLng lngDestination = new LatLng(locationDestination.getLatitude(), locationDestination.getLongitude());

        // execute Directions API request for intermediate locations
        DirectionsResult directionsResult = DirectionsApi.newRequest(context)
                .origin(lngOrigin)
                .destination(lngDestination)
                .optimizeWaypoints(Boolean.TRUE)
                .alternatives(Boolean.TRUE)
                .await();

        OptimalRoutePojo optimalRoutePojo = showShortestRoute(directionsResult);
        optimalRoutePojo.setOriginName(origin);
        optimalRoutePojo.setDestinationName(destination);

        return optimalRoutePojo;
    }

    private GetCoordinatesResponseDto getLocationCoordinates(LocationPojo locationPojo) throws IOException,
            InterruptedException, ApiException {
        // initialize GeoApiContext
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();

        String locationName = locationPojo.getStreet() + " " + locationPojo.getCity() + " " +
                locationPojo.getState() +  " " + locationPojo.getCountry();

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

    private OptimalRoutePojo showShortestRoute(DirectionsResult directionsResult){
        OptimalRoutePojo optimalRoutePojo = new OptimalRoutePojo();
        Map<String, Long> allRoutesBetweenOriginAndDestination = new HashMap<>();

        Arrays.stream(directionsResult.routes).forEach(route -> {
            allRoutesBetweenOriginAndDestination.put(route.summary, 0L);

            Arrays.stream(route.legs).forEach(leg -> {
                optimalRoutePojo.setDuration(String.valueOf(leg.duration));
                for (Map.Entry<String, Long> entry : allRoutesBetweenOriginAndDestination.entrySet()) {
                    entry.setValue(entry.getValue() + leg.distance.inMeters);
                }
            });
        });

        String minDistSummary = null;
        Long minDist = Long.MAX_VALUE;

        for (Map.Entry<String, Long> entry : allRoutesBetweenOriginAndDestination.entrySet()) {
            if (entry.getValue() < minDist) {
                minDist = entry.getValue();
                minDistSummary = entry.getKey();
            }
        }

        optimalRoutePojo.setDistanceInMeters((double) minDist);
        optimalRoutePojo.setRouteSummary(minDistSummary);
        optimalRoutePojo.setDistanceInKm(minDist / 1000.0);
        optimalRoutePojo.setDeliveryCost(costPerKm * optimalRoutePojo.getDistanceInKm());

        return optimalRoutePojo;
    }

    private LocationResponseDto responseMapper(Location location) {
        return LocationResponseDto.builder()
                .name(location.getName())
                .address(location.getAddress())
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .locationType(location.getLocationType())
                .build();
    }
}
