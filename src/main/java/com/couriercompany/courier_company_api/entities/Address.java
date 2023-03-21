package com.couriercompany.courier_company_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "address_tbl")
public class Address extends BaseEntity {
    private String street;
    private String city;
    private String state;
    private String country;
}
