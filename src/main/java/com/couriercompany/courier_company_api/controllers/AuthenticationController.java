package com.couriercompany.courier_company_api.controllers;

import com.couriercompany.courier_company_api.pojos.LoginRequestPojo;
import com.couriercompany.courier_company_api.services.StaffService;
import com.couriercompany.courier_company_api.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
   private final StaffService staffService;

    @PostMapping("/login")
    public ApiResponse authenticate(@Valid @RequestBody LoginRequestPojo loginRequest) {
        return staffService.login(loginRequest);
    }
}
