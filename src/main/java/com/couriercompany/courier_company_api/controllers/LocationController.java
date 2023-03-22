package com.couriercompany.courier_company_api.controllers;

import com.couriercompany.courier_company_api.entities.Route;
import com.couriercompany.courier_company_api.services.LocationService;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/optimal-route")
    public ResponseEntity<Route> calculateRoute(
            @RequestParam String originName,
            @RequestParam String destinationName
    ) throws ApiException, InterruptedException, IOException {
        Route route = locationService.getOptimalRoute(originName, destinationName);

        return new ResponseEntity<>(route, HttpStatus.OK);
    }

    @GetMapping("/intermediate-locations")
    public ResponseEntity<DirectionsResult> getIntermediateLocations(@RequestParam String originName,
                                                                              @RequestParam String destinationName
                                                                              ) throws Exception {
        DirectionsResult intermediateLocations = locationService.getIntermediateLocations(originName, destinationName);
        return new ResponseEntity<>(intermediateLocations, HttpStatus.OK);
    }
}
