package com.couriercompany.courier_company_api.entities;

import com.couriercompany.courier_company_api.enums.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(name = "parcel_tbl")
public class Parcel extends BaseEntity {
    private String content;

    @JsonIgnore
    @OneToOne(mappedBy = "parcel", cascade = CascadeType.ALL)
    private Location origin;

    @JsonIgnore
    @OneToOne(mappedBy = "parcel", cascade = CascadeType.ALL)
    private Location destination;

    private DeliveryStatus deliveryStatus;
}
