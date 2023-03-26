package com.couriercompany.courier_company_api.services.serviceImpl;

import com.couriercompany.courier_company_api.entities.Person;
import com.couriercompany.courier_company_api.exceptions.UserNotFoundException;
import com.couriercompany.courier_company_api.repositories.PersonRepository;
import com.couriercompany.courier_company_api.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


@RequiredArgsConstructor
@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    @Override
    public boolean confirmAuthority(){
        Boolean isUserLoggedIn = false;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();
        Person loggedInUser = personRepository.findByEmail(loggedInUserEmail).
                orElseThrow(() -> new UserNotFoundException("No user with this email"));

        if(!ObjectUtils.isEmpty(loggedInUser) || isUserLoggedIn != null) isUserLoggedIn = true;

        return isUserLoggedIn;
    }
}
