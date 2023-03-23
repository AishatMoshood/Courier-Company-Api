package com.couriercompany.courier_company_api.controllers;

import com.couriercompany.courier_company_api.config.tokens.TokenService;
import com.couriercompany.courier_company_api.config.userDetails.AppUserDetailsService;
import com.couriercompany.courier_company_api.pojos.LoginRequestPojo;
import com.couriercompany.courier_company_api.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService userDetailsService;
    

    @PostMapping("/login")
        public ApiResponse authenticate(@Valid @RequestBody LoginRequestPojo loginRequest) {
            try {
                UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getEmail());
                if(!user.isEnabled())
                    throw new UsernameNotFoundException("You have not been verified. Check your email to be verified!");
                if (!user.isAccountNonLocked()){
                    return new ApiResponse<>("This account has been deactivated", false, null,HttpStatus.OK);
                }
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
                if(authentication == null)
                    throw new InvalidCredentialsException("Invalid Email or Password");
                return new ApiResponse<>("Login Successful",
                        true, tokenService.generateToken(authentication), HttpStatus.OK);
            } catch (InvalidCredentialsException e) {
                return new ApiResponse<>("Invalid Credentials", false, null, HttpStatus.UNAUTHORIZED);
            } catch (BadCredentialsException | UsernameNotFoundException e) {
                return new ApiResponse<>("Password or Email not correct", false, null, HttpStatus.UNAUTHORIZED);
            }
    }
}
