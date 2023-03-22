package com.couriercompany.courier_company_api.config.userDetails;


import com.couriercompany.courier_company_api.entities.Person;
import com.couriercompany.courier_company_api.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person dbUser = personRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Not Found"));
        return new AppUserDetails(dbUser);
    }
}
