package com.couriercompany.courier_company_api.services;

import com.couriercompany.courier_company_api.dtos.SignupResponseDto;
import com.couriercompany.courier_company_api.exceptions.AlreadyExistsException;
import com.couriercompany.courier_company_api.exceptions.EmailNotFoundException;
import com.couriercompany.courier_company_api.pojos.SignupRequestPojo;

import java.io.IOException;

public interface StaffService {
    SignupResponseDto signup(SignupRequestPojo signupRequestPojo) throws AlreadyExistsException, IOException;

    String resendVerificationToken(String email) throws EmailNotFoundException, IOException;

    String verifyRegistration(String token);
}
