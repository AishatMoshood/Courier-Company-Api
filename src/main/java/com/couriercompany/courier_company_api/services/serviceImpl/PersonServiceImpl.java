package com.couriercompany.courier_company_api.services.serviceImpl;

import com.couriercompany.courier_company_api.exceptions.UnauthorizedException;
import com.couriercompany.courier_company_api.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PersonServiceImpl implements PersonService {

    @Override
    public boolean confirmIfUserLoggedIn(){
        //Get authentication details of user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken))
            throw new UnauthorizedException("Please Login");

        return true;
    }
}
