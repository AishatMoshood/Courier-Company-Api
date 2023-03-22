package com.couriercompany.courier_company_api.config.userDetails;

import com.couriercompany.courier_company_api.entities.Person;
import com.couriercompany.courier_company_api.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class PasswordService implements UserDetailsPasswordService {
    private final PersonRepository personRepository;

    @Override
    public UserDetails updatePassword(UserDetails user, String password) {
        Person person = personRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist!"));
        person.setPassword(password);

        return new AppUserDetails(person);
    }
}
