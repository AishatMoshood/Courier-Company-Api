package com.couriercompany.courier_company_api.controllers;

import com.couriercompany.courier_company_api.exceptions.AlreadyExistsException;
import com.couriercompany.courier_company_api.pojos.SignupRequestPojo;
import com.couriercompany.courier_company_api.services.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService staffService;

    @PostMapping("/staff/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestPojo signupRequestPojo) throws AlreadyExistsException, IOException {
        staffService.signup(signupRequestPojo);
        return new ResponseEntity<>("Registration Successful! Check your mail for activation link",HttpStatus.CREATED);
    }

    @GetMapping("/staff/verifyRegistration")
    public ResponseEntity<ApiResponse> verifyAccount(@RequestParam("token") String token){
        return customerService.verifyRegistration(token);
    }
}