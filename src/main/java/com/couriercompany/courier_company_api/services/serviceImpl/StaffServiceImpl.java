package com.couriercompany.courier_company_api.services.serviceImpl;

import com.couriercompany.courier_company_api.config.tokens.TokenService;
import com.couriercompany.courier_company_api.dtos.SignupResponseDto;
import com.couriercompany.courier_company_api.entities.Person;
import com.couriercompany.courier_company_api.entities.Staff;
import com.couriercompany.courier_company_api.entities.Token;
import com.couriercompany.courier_company_api.enums.Role;
import com.couriercompany.courier_company_api.exceptions.AlreadyExistsException;
import com.couriercompany.courier_company_api.exceptions.EmailNotFoundException;
import com.couriercompany.courier_company_api.exceptions.InvalidTokenException;
import com.couriercompany.courier_company_api.pojos.SignupRequestPojo;
import com.couriercompany.courier_company_api.repositories.PersonRepository;
import com.couriercompany.courier_company_api.repositories.StaffRepository;
import com.couriercompany.courier_company_api.repositories.TokenRepository;
import com.couriercompany.courier_company_api.services.JavaMailService;
import com.couriercompany.courier_company_api.services.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;

import static com.couriercompany.courier_company_api.enums.TokenStatus.ACTIVE;
import static com.couriercompany.courier_company_api.enums.TokenStatus.EXPIRED;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final PersonRepository personRepository;
    private final StaffRepository staffRepository;
    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final JavaMailService javaMailService;
    private final TokenService tokenService;
    private final HttpServletRequest request;

    @Override
    @Transactional
    public SignupResponseDto signup(SignupRequestPojo signupRequestPojo) throws AlreadyExistsException, IOException {
        boolean emailExist = personRepository.existsByEmail(signupRequestPojo.getEmail());
        if (emailExist)
            throw new AlreadyExistsException("This Email address already exists");

        Staff staff = new Staff();

        Person person =  Person.builder()
                .firstName(signupRequestPojo.getFirstName())
                .lastName(signupRequestPojo.getLastName())
                .email(signupRequestPojo.getEmail())
                .password(passwordEncoder.encode(signupRequestPojo.getPassword()))
                .staff(staff)
                .role(Role.STAFF)
                .verificationStatus(false)
                .build();

        staff.setPerson(person);

        personRepository.save(person);
        staffRepository.save(staff);

        String validToken = tokenService.generateVerificationToken(signupRequestPojo.getEmail());
        Token token = new Token();
        token.setToken(validToken);
        token.setTokenStatus(ACTIVE);
        token.setPerson(person);
        tokenRepository.save(token);

        String subject = "Verify email address";

        String message =
                "<html>" +
                        "<body>" +
                        "<h2>Hi " + person.getFirstName() + " " + person.getLastName() +",</h2> </br>" +
                        "<h3>Welcome to the delivery platform for courier company." +
                        "To complete your registration, please verify your email address \n" +
                        "<br><a href=[[TOKEN_URL]]>CLICK TO VERIFY</a></h3>" +
                        "</body> " +
                        "</html>";

        String url = "http://" + request.getServerName() + ":3000" + "/verifyRegistration?token=" + validToken;

        message = message.replace("[[TOKEN_URL]]", url);

        javaMailService.sendMailAlt(signupRequestPojo.getEmail(), subject, message);

        SignupResponseDto signupResponseDto = SignupResponseDto.builder()
                        .firstName(person.getFirstName())
                        .lastName(person.getLastName())
                        .email(person.getEmail())
                        .verificationStatus(person.getVerificationStatus())
                        .build();

        return signupResponseDto;
    }

    @Override
    public String resendVerificationToken(String email) throws EmailNotFoundException, IOException {
        boolean emailExists = personRepository.existsByEmail(email);
        if (!emailExists) {
            throw new EmailNotFoundException("Email not found");
        }
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with this email"));

        if (!person.getVerificationStatus()) {
            String validToken = tokenService.generateVerificationToken(email);
            Token token = new Token();
            token.setToken(validToken);
            token.setTokenStatus(ACTIVE);
            token.setPerson(person);
            tokenRepository.save(token);

            String subject = "Verify email address";

            String message =

                    "<html> " +
                            "<body>" +
                            "<h2>Hi " + person.getFirstName() + " " + person.getLastName() +",</h2> <br>" +
                            "<h4>Welcome to the delivery platform for courier company." +
                            "To complete your registration, please verify your email address \n" +
                            "<br><a href=[[TOKEN_URL]]>CLICK TO VERIFY AGAIN</a></h4>" +
                            "</body> " +
                            "</html>";


            String url = "http://" + request.getServerName() + ":3000" + "/verifyRegistration?token=" + validToken;

            message = message.replace("[[TOKEN_URL]]", url);

            javaMailService.sendMailAlt(email, subject, message);
            return "Verification token resent. Check your email";

        }else
            throw new AlreadyExistsException("User is already verified");
    }

    @Override
    public String verifyRegistration(String token) {
        Token verificationToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new InvalidTokenException("Token Not Found"));

        if (verificationToken.getTokenStatus().equals(EXPIRED))
            throw new InvalidTokenException("Token already used");

        verificationToken.getPerson().setVerificationStatus(true);
        verificationToken.setTokenStatus(EXPIRED);
        tokenRepository.save(verificationToken);
        return "Account verification successful";
    }
}
