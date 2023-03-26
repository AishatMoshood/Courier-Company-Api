package com.couriercompany.courier_company_api.services;

import com.couriercompany.courier_company_api.dtos.LocationResponseDto;
import com.couriercompany.courier_company_api.pojos.LocationPojo;
import com.couriercompany.courier_company_api.pojos.OptimalRoutePojo;
import com.google.maps.errors.ApiException;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface LocationService {
    String removeLocation(Long locationId);

    String updateLocation(LocationPojo updateLocationPojo, Long locationId) throws IOException, InterruptedException, ApiException;

    String addLocation(LocationPojo locationPojo) throws IOException, InterruptedException, ApiException;

    Page<LocationResponseDto> getAllLocations(Integer pageNo, Integer pageSize, String sortingField, boolean isAscending);

    OptimalRoutePojo getOptimalRoute(String originName, String destinationName) throws Exception;

}
