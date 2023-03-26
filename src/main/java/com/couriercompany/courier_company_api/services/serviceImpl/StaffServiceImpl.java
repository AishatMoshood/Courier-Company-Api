package com.couriercompany.courier_company_api.services.serviceImpl;

import com.couriercompany.courier_company_api.config.tokens.TokenService;
import com.couriercompany.courier_company_api.config.userDetails.AppUserDetailsService;
import com.couriercompany.courier_company_api.dtos.SignupResponseDto;
import com.couriercompany.courier_company_api.entities.Person;
import com.couriercompany.courier_company_api.entities.Staff;
import com.couriercompany.courier_company_api.entities.Token;
import com.couriercompany.courier_company_api.enums.Role;
import com.couriercompany.courier_company_api.exceptions.AlreadyExistsException;
import com.couriercompany.courier_company_api.exceptions.EmailNotFoundException;
import com.couriercompany.courier_company_api.exceptions.InvalidTokenException;
import com.couriercompany.courier_company_api.pojos.LoginRequestPojo;
import com.couriercompany.courier_company_api.pojos.SignupRequestPojo;
import com.couriercompany.courier_company_api.repositories.PersonRepository;
import com.couriercompany.courier_company_api.repositories.StaffRepository;
import com.couriercompany.courier_company_api.repositories.TokenRepository;
import com.couriercompany.courier_company_api.services.JavaMailService;
import com.couriercompany.courier_company_api.services.StaffService;
import com.couriercompany.courier_company_api.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService userDetailsService;

    @Override
    @Transactional
    public SignupResponseDto signup(SignupRequestPojo signupRequestPojo) throws AlreadyExistsException, IOException {
        //Check if inputted email in db
        boolean emailExist = personRepository.existsByEmail(signupRequestPojo.getEmail());
        if (emailExist)
            throw new AlreadyExistsException("This Email address already exists");

        //Initialize staff object
        Staff staff = new Staff();

        //Save provided details in a person object
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

        //Generate Token
        String validToken = tokenService.generateVerificationToken(signupRequestPojo.getEmail());
        Token token = new Token();
        token.setToken(validToken);
        token.setTokenStatus(ACTIVE);
        token.setPerson(person);
        tokenRepository.save(token);

        //Create Email Verification message
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

        //Build context path(URL) for email verification
        String url = "http://" + request.getServerName() + ":8080/api/v1/staff" + "/verify-registration?token=" + validToken;
        message = message.replace("[[TOKEN_URL]]", url);

        //Send email to user's email address
        javaMailService.sendMailAlt(signupRequestPojo.getEmail(), subject, message);

        //Build response Object
        SignupResponseDto signupResponseDto = SignupResponseDto.builder()
                        .firstName(person.getFirstName())
                        .lastName(person.getLastName())
                        .email(person.getEmail())
                        .verificationStatus(person.getVerificationStatus())
                        .build();

        return signupResponseDto;
    }

    @Override
    public String verifyRegistration(String token) {
        //Check if token in db
        Token verificationToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new InvalidTokenException("Token Not Found"));

        //if token expired throw exception
        if (verificationToken.getTokenStatus().equals(EXPIRED))
            throw new InvalidTokenException("Token already used");

        //Save token
        verificationToken.getPerson().setVerificationStatus(true);
        verificationToken.getPerson().setActive(true);
        verificationToken.setTokenStatus(EXPIRED);
        tokenRepository.save(verificationToken);
        return "Account verification successful";
    }

    @Override
    public ApiResponse login(LoginRequestPojo loginRequest){
        try {
            //Get user details from email
            UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            //Check if user has been verified from their email
            if(!user.isEnabled())
                return new ApiResponse<>("You have not been verified. Check your email to be verified!",false, null, HttpStatus.UNAUTHORIZED);
            //Check if user's account is active
            if (!user.isAccountNonLocked()){
                return new ApiResponse<>("This account has been deactivated", false, null, HttpStatus.OK);
            }
            //Check if user exists in db
            if (user == null){
                return new ApiResponse<>("Email not found", false, null, HttpStatus.NOT_FOUND);
            }
            //Authenticate user details
            if(authentication == null)
                throw new InvalidCredentialsException("Invalid Email or Password");
            return new ApiResponse<>("Login Successful",
                    true, tokenService.generateToken(authentication), HttpStatus.OK);
        } catch (InvalidCredentialsException e) {
            return new ApiResponse<>("Invalid Credentials", false, null, HttpStatus.UNAUTHORIZED);
        } catch (UsernameNotFoundException e) {
            return new ApiResponse<>("Email not found", false, null, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public String resendVerificationToken(String email) throws EmailNotFoundException, IOException {
        //Check if email exists in db
        boolean emailExists = personRepository.existsByEmail(email);
        if (!emailExists) {
            throw new EmailNotFoundException("Email not found");
        }
        //Find user in db
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with this email"));

        //if user hasn't been verified resend verification email
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


            String url = "http://" + request.getServerName() + ":8080/api/v1/staff" + "/verify-registration?token=" + validToken;

            message = message.replace("[[TOKEN_URL]]", url);

            javaMailService.sendMailAlt(email, subject, message);
            return "Verification token resent. Check your email";

        }else
            //if user is verified throw exception
            throw new AlreadyExistsException("User is already verified");
    }
}
