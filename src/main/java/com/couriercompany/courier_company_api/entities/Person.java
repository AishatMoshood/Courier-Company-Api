package com.couriercompany.courier_company_api.entities;

import com.couriercompany.courier_company_api.enums.Gender;
import com.couriercompany.courier_company_api.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "person_tbl")
public class Person extends BaseEntity{

    @NotNull(message = "First name cannot be missing or empty")
    @Column(nullable = false, length = 50)
    private String firstName;

    @NotNull(message = "Last name cannot be missing or empty")
    @Column(nullable = false, length = 50)
    private String lastName;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private Boolean verificationStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @JsonIgnore
    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private Staff staff;
}

