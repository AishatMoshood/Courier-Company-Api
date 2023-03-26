package com.couriercompany.courier_company_api.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.when;

import com.couriercompany.courier_company_api.enums.LocationType;
import com.couriercompany.courier_company_api.pojos.LocationPojo;
import com.couriercompany.courier_company_api.pojos.OptimalRoutePojo;
import com.couriercompany.courier_company_api.services.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {LocationController.class})
@ExtendWith(SpringExtension.class)
class LocationControllerTest {
    @Autowired
    private LocationController locationController;

    @MockBean
    private LocationService locationService;

    @Test
    void locationController_toAddLocation_andReturnString() throws Exception {
        when(locationService.addLocation(any())).thenReturn("Location added successfully");

        LocationPojo locationPojo = new LocationPojo();
        locationPojo.setCity("Ilorin");
        locationPojo.setCountry("Nigeria");
        locationPojo.setLocationType(LocationType.ORIGIN);
        locationPojo.setName("Ilorin Location");
        locationPojo.setState("Kwara");
        locationPojo.setStreet("13, Taiwo Road");
        String content = (new ObjectMapper()).writeValueAsString(locationPojo);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/location/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(locationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Location added successfully"));
    }

    @Test
    void locationController_updateLocation_andReturnString() throws Exception {
        when(locationService.updateLocation(any(), any())).thenReturn("Location updated");

        LocationPojo locationPojo = new LocationPojo();
        locationPojo.setCity("Ilorin");
        locationPojo.setCountry("Nigeria");
        locationPojo.setLocationType(LocationType.ORIGIN);
        locationPojo.setName("Ilorin Location");
        locationPojo.setState("Kwara");
        locationPojo.setStreet("13, Taiwo Road");

        String content = (new ObjectMapper()).writeValueAsString(locationPojo);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/location/update/{locationId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(locationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Location updated"));
    }

    @Test
    void locationController_toGetAllLocations_andReturnJson() throws Exception {
        when(locationService.getAllLocations(any(), any(), any(), anyBoolean()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v1/location/paginated-all");
        MockHttpServletRequestBuilder paramResult = getResult.param("isAscending", String.valueOf(true));
        MockHttpServletRequestBuilder paramResult1 = paramResult.param("pageNo", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult1.param("pageSize", String.valueOf(1))
                .param("sortBy", "foo");
        MockMvcBuilders.standaloneSetup(locationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

    @Test
    void locationController_toGetOptimalRoute_andReturnJson() throws Exception {
        OptimalRoutePojo optimalRoutePojo = new OptimalRoutePojo();
        optimalRoutePojo.setDeliveryCostInDollars(1.0d);
        optimalRoutePojo.setDestinationName("Lagos Location");
        optimalRoutePojo.setDistanceInKm(10.0d);
        optimalRoutePojo.setDistanceInMeters(10.0d);
        optimalRoutePojo.setDuration("1hour 30mins");
        optimalRoutePojo.setOriginName("New york location");
        optimalRoutePojo.setRouteSummary("Route Summary");
        when(locationService.getOptimalRoute(any(), any())).thenReturn(optimalRoutePojo);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/location/optimal-route")
                .param("destinationName", "Lagos Location")
                .param("originName", "New york location");
        MockMvcBuilders.standaloneSetup(locationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"originName\":\"New york location\",\"destinationName\":\"Lagos Location\",\"routeSummary\":\"Route Summary\","
                                        + "\"distanceInMeters\":10.0,\"distanceInKm\":10.0,\"duration\":\"1hour 30mins\",\"deliveryCostInDollars\":1.0}"));
    }

    @Test
    void locationControllerTo_deleteLocation_andReturnString() throws Exception {
        when(locationService.removeLocation(any())).thenReturn("Location Deleted");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/location/delete/{locationId}", 1L);
        MockMvcBuilders.standaloneSetup(locationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Location Deleted"));
    }
}

