package com.couriercompany.courier_company_api.entities;

import com.couriercompany.courier_company_api.enums.LocationType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(name = "location_tbl")
public class Location extends BaseEntity {

    @NotNull(message = "Location name cannot be missing or empty")
    @Column(name = "name", nullable = false)
    private String name;

    private Double latitude;

    private Double longitude;

    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    public Location(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
