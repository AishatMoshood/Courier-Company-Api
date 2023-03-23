package com.couriercompany.courier_company_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "address_tbl")
public class Address extends BaseEntity{

    private String street;
    private String city;
    private String state;
    private String country;

//    @ManyToOne
//    @JoinColumn(name="customer_id")
//    private Customer customer;

    @JsonIgnore
    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private Location location;
}
