package com.couriercompany.courier_company_api.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(name = "delivery_tbl")
public class Delivery extends BaseEntity{
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parcel_id")
    private Parcel parcel;

    private Double deliveryCost;
}
