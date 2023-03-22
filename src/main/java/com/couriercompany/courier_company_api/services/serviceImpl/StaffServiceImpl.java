package com.couriercompany.courier_company_api.services.serviceImpl;

import com.couriercompany.courier_company_api.dtos.SignupResponseDto;
import com.couriercompany.courier_company_api.entities.Person;
import com.couriercompany.courier_company_api.entities.Staff;
import com.couriercompany.courier_company_api.exceptions.AlreadyExistsException;
import com.couriercompany.courier_company_api.exceptions.EmailNotFoundException;
import com.couriercompany.courier_company_api.pojos.SignupRequestPojo;
import com.couriercompany.courier_company_api.repositories.PersonRepository;
import com.couriercompany.courier_company_api.services.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final PersonRepository personRepository;
    @Override
    @Transactional
    public SignupResponseDto signup(SignupRequestPojo signupRequestPojo) throws AlreadyExistsException, IOException {
        boolean emailExist = personRepository.existsByEmail(signupRequestPojo.getEmail());
        if (emailExist)
            throw new AlreadyExistsException("This Email address already exists");

        Staff staff = new Staff();

        Person person = new Person();
        BeanUtils.copyProperties(signupRequestDto, person);
        person.setCustomer(customer);
        person.setRole(Role.CUSTOMER);
        person.setVerificationStatus(false);
        person.setGender(Gender.valueOf(signupRequestDto.getGender().toUpperCase()));
        person.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));

        customer.setPerson(person);

        personRepository.save(person);
        customerRepository.save(customer);

        String validToken = tokenService.generateVerificationToken(signupRequestDto.getEmail());
        Token token = new Token();
        token.setToken(validToken);
        token.setTokenStatus(ACTIVE);
        token.setPerson(person);
        tokenRepository.save(token);

        String subject = "Verify email address";

        String message =

                "<html> " +
                        "<body>" +
                        "<h5>Hi " + person.getFirstName() + " " + person.getLastName() +",<h5> <br>" +
                        "<p>Thank you for your interest in joining Oakland." +
                        "To complete your registration, we need you to verify your email address \n" +
                        "<br><a href=[[TOKEN_URL]]>CLICK TO VERIFY</a><p>" +
                        "</body> " +
                        "</html>";


        String url = "http://" + request.getServerName() + ":3000" + "/verifyRegistration?token=" + validToken;

        message = message.replace("[[TOKEN_URL]]", url);

        javaMailService.sendMailAlt(signupRequestDto.getEmail(), subject, message);

        // use the user object to create UserResponseDto Object
        SignupResponseDto signupResponseDto = new SignupResponseDto();
        BeanUtils.copyProperties(person, signupResponseDto);
        return signupResponseDto;
    }

    @Override
    public String resendVerificationToken(String email) throws EmailNotFoundException, IOException {
        return null;
    }

    @Override
    public String verifyRegistration(String token) {
        return null;
    }
}
