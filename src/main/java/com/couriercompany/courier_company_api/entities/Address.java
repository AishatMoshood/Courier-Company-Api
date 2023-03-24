package com.couriercompany.courier_company_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "address_tbl")
public class Address extends BaseEntity{

    @NotNull(message = "Street name cannot be missing or empty")
    private String street;

    @NotNull(message = "City name cannot be missing or empty")
    private String city;

    @NotNull(message = "State name cannot be missing or empty")
    private String state;

    @NotNull(message = "Country name cannot be missing or empty")
    private String country;

    @JsonIgnore
    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private Location location;
}
