package com.couriercompany.courier_company_api.pojos;

import com.couriercompany.courier_company_api.enums.LocationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LocationPojo {
    private String name;

    @NotNull(message = "Street name cannot be missing or empty")
    private String street;

    @NotNull(message = "City name cannot be missing or empty")
    private String city;

    @NotNull(message = "State name cannot be missing or empty")
    private String state;

    @NotNull(message = "Country name cannot be missing or empty")
    private String country;

    @NotNull(message = "Location type cannot be missing or empty")
    private LocationType locationType;
}
