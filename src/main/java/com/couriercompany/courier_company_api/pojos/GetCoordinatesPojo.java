package com.couriercompany.courier_company_api.pojos;

import com.couriercompany.courier_company_api.entities.Address;
import com.couriercompany.courier_company_api.enums.LocationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetCoordinatesPojo {
    private Address address;
    private LocationType locationType;
}
