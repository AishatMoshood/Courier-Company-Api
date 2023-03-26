package com.couriercompany.courier_company_api.services.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.couriercompany.courier_company_api.config.tokens.TokenService;
import com.couriercompany.courier_company_api.config.userDetails.AppUserDetailsService;
import com.couriercompany.courier_company_api.entities.Person;
import com.couriercompany.courier_company_api.entities.Staff;
import com.couriercompany.courier_company_api.entities.Token;
import com.couriercompany.courier_company_api.enums.Role;
import com.couriercompany.courier_company_api.enums.TokenStatus;
import com.couriercompany.courier_company_api.exceptions.AlreadyExistsException;
import com.couriercompany.courier_company_api.exceptions.EmailNotFoundException;
import com.couriercompany.courier_company_api.pojos.LoginRequestPojo;
import com.couriercompany.courier_company_api.pojos.SignupRequestPojo;
import com.couriercompany.courier_company_api.repositories.PersonRepository;
import com.couriercompany.courier_company_api.repositories.StaffRepository;
import com.couriercompany.courier_company_api.repositories.TokenRepository;
import com.couriercompany.courier_company_api.services.JavaMailService;
import com.couriercompany.courier_company_api.utils.ApiResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {StaffServiceImpl.class})
@ExtendWith(SpringExtension.class)
class StaffServiceImplTest {
    @MockBean
    private AppUserDetailsService appUserDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private HttpServletRequest httpServletRequest;

    @MockBean
    private JavaMailService javaMailService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private StaffRepository staffRepository;

    @Autowired
    private StaffServiceImpl staffServiceImpl;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private TokenService tokenService;

    @Test
    void signup_toReturnString() throws AlreadyExistsException, IOException {
        Person person = new Person();
        person.setActive(true);
        person.setCreatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        person.setEmail("am@gmail.com");
        person.setFirstName("Aishat");
        person.setId(1L);
        person.setLastName("Moshood");
        person.setPassword("123456789");
        person.setRole(Role.STAFF);
        person.setStaff(new Staff());
        person.setUpdatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        person.setVerificationStatus(true);
        person.updatedAt();

        Staff staff = new Staff();
        staff.setCreatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        staff.setId(1L);
        staff.setPerson(person);
        staff.setUpdatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        staff.updatedAt();

        when(personRepository.existsByEmail(any())).thenReturn(true);
        when(personRepository.save(any())).thenReturn(person);
        assertThrows(AlreadyExistsException.class,
                () -> staffServiceImpl.signup(new SignupRequestPojo("Jane", "Doe", "am@gmail.com", "123456789")));
        verify(personRepository).existsByEmail(any());
    }

    @Test
    void verifyRegistration_toReturnString() {
        Person person = new Person();
        person.setActive(true);
        person.setCreatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        person.setEmail("am@gmail.com");
        person.setFirstName("Aishat");
        person.setId(1L);
        person.setLastName("Moshood");
        person.setPassword("123456789");
        person.setRole(Role.STAFF);
        person.setStaff(new Staff());
        person.setUpdatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        person.setVerificationStatus(true);
        person.updatedAt();

        Staff staff = new Staff();
        staff.setCreatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        staff.setId(1L);
        staff.setPerson(person);
        staff.setUpdatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        staff.updatedAt();

        Token token = new Token();
        token.setCreatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        token.setId(1L);
        token.setPerson(person);
        token.setToken("1234678");
        token.setTokenStatus(TokenStatus.ACTIVE);
        token.setUpdatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        token.updatedAt();
        Optional<Token> ofResult = Optional.of(token);

        when(tokenRepository.save(any())).thenReturn(token);
        when(tokenRepository.findByToken(any())).thenReturn(ofResult);
        assertEquals("Account verification successful", staffServiceImpl.verifyRegistration("1234678"));
        verify(tokenRepository).save(any());
        verify(tokenRepository).findByToken(any());
    }

    @Test
    void login_toReturnJSON() throws AuthenticationException {
        when(tokenService.generateToken(any())).thenReturn("123");
        when(appUserDetailsService.loadUserByUsername(any()))
                .thenReturn(new User("Aishat", "123456789", new ArrayList<>()));
        when(authenticationManager.authenticate(any()))
                .thenReturn(new BearerTokenAuthenticationToken("ABC123"));
        ApiResponse actualLoginResult = staffServiceImpl.login(new LoginRequestPojo("am@gmail.com", "123456789"));
        assertEquals("123", actualLoginResult.getData());
        assertEquals(HttpStatus.OK, actualLoginResult.getStatusCode());
        assertTrue(actualLoginResult.getStatus());
        assertEquals("Login Successful", actualLoginResult.getMessage());
        verify(tokenService).generateToken(any());
        verify(appUserDetailsService).loadUserByUsername(any());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void resendVerificationToken_toReturnString() throws EmailNotFoundException, IOException {
        Person person = new Person();
        person.setActive(true);
        person.setCreatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        person.setEmail("am@gmail.com");
        person.setFirstName("Aishat");
        person.setId(1L);
        person.setLastName("Moshood");
        person.setPassword("123456789");
        person.setRole(Role.STAFF);
        person.setStaff(new Staff());
        person.setUpdatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        person.setVerificationStatus(true);
        person.updatedAt();

        Staff staff = new Staff();
        staff.setCreatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        staff.setId(1L);
        staff.setPerson(person);
        staff.setUpdatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        staff.updatedAt();

        Token token = new Token();
        token.setCreatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        token.setId(1L);
        token.setPerson(person);
        token.setToken("1234678");
        token.setTokenStatus(TokenStatus.ACTIVE);
        token.setUpdatedAt(Date.from(LocalDate.of(2023,1, 2).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        token.updatedAt();

        Optional<Person> ofResult = Optional.of(person);
        when(personRepository.findByEmail(any())).thenReturn(ofResult);
        when(personRepository.existsByEmail(any())).thenReturn(true);
        assertThrows(AlreadyExistsException.class,
                () -> staffServiceImpl.resendVerificationToken("am@gmail.com"));
        verify(personRepository).existsByEmail(any());
        verify(personRepository).findByEmail(any());
    }
}

