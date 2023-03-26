package com.couriercompany.courier_company_api.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequestPojo{

    @NotNull(message = "Email is a required field")
    private String email;

    @NotNull(message = "Password is a required field")
    private String password;
}
