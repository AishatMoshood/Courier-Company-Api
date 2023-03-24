package com.couriercompany.courier_company_api.controllers;

import com.couriercompany.courier_company_api.entities.Location;
import com.couriercompany.courier_company_api.pojos.LocationPojo;
import com.couriercompany.courier_company_api.pojos.OptimalRoutePojo;
import com.couriercompany.courier_company_api.services.LocationService;
import com.google.maps.errors.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/add-location")
    public ResponseEntity<String> addLocation(@RequestBody LocationPojo locationPojo) throws IOException, InterruptedException, ApiException {
      return new ResponseEntity<>(locationService.addLocation(locationPojo), HttpStatus.CREATED);
    }

    @PutMapping("/update-location/{locationId}")
    public ResponseEntity<Location> updateLocation(@RequestBody LocationPojo updateLocationPojo, @PathVariable Long locationId) {
        return new ResponseEntity<>(locationService.updateLocation(updateLocationPojo, locationId), HttpStatus.OK);
    }

    @DeleteMapping("/delete-location/{locationId}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long locationId){
        return new ResponseEntity<>(locationService.removeLocation(locationId), HttpStatus.OK);
    }

    @GetMapping("/optimal-route")
    public ResponseEntity<OptimalRoutePojo> calculateRoute(
            @RequestParam String originName,
            @RequestParam String destinationName
    ) throws Exception {
        OptimalRoutePojo optimalRoute = locationService.getOptimalRoute(originName, destinationName);
        return new ResponseEntity<>(optimalRoute, HttpStatus.OK);
    }

//    @GetMapping("/intermediate-locations")
//    public ResponseEntity<DirectionsResult> getIntermediateLocations(@RequestParam String originName,
//                                                                              @RequestParam String destinationName
//                                                                              ) throws Exception {
//        DirectionsResult intermediateLocations = locationService.getIntermediateLocations(originName, destinationName);
//        return new ResponseEntity<>(intermediateLocations, HttpStatus.OK);
//    }
}
