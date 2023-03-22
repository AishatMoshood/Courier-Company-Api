package com.couriercompany.courier_company_api.config.tokens;

import org.springframework.security.core.Authentication;


public interface TokenService {
    String generateToken(Authentication authentication);
    String generateVerificationToken(String email);
}
