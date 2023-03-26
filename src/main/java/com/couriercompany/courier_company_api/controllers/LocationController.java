package com.couriercompany.courier_company_api.controllers;

import com.couriercompany.courier_company_api.dtos.LocationResponseDto;
import com.couriercompany.courier_company_api.pojos.LocationPojo;
import com.couriercompany.courier_company_api.pojos.OptimalRoutePojo;
import com.couriercompany.courier_company_api.services.LocationService;
import com.google.maps.errors.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/add")
    public ResponseEntity<String> addLocation(@RequestBody LocationPojo locationPojo) throws IOException, InterruptedException, ApiException {
      return new ResponseEntity<>(locationService.addLocation(locationPojo), HttpStatus.CREATED);
    }

    @PutMapping("/update/{locationId}")
    public ResponseEntity<String> updateLocation(@RequestBody LocationPojo updateLocationPojo,
                                                   @PathVariable Long locationId) throws IOException, InterruptedException, ApiException {
        return new ResponseEntity<>(locationService.updateLocation(updateLocationPojo, locationId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{locationId}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long locationId){
        return new ResponseEntity<>(locationService.removeLocation(locationId), HttpStatus.OK);
    }

    @GetMapping("/paginated-all")
    public ResponseEntity<Page<LocationResponseDto>> getAllLocations(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                    @RequestParam(defaultValue = "16") Integer pageSize,
                                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                                    @RequestParam(defaultValue = "false") boolean isAscending) {
        return new ResponseEntity<>(locationService.getAllLocations(pageNo, pageSize, sortBy, isAscending), HttpStatus.OK);
    }

    @GetMapping("/optimal-route")
    public ResponseEntity<OptimalRoutePojo> calculateRoute(
            @RequestParam String originName,
            @RequestParam String destinationName
    ) throws Exception {
        OptimalRoutePojo optimalRoute = locationService.getOptimalRoute(originName, destinationName);
        return new ResponseEntity<>(optimalRoute, HttpStatus.OK);
    }

}
