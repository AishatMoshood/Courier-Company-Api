package com.couriercompany.courier_company_api.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.couriercompany.courier_company_api.dtos.SignupResponseDto;
import com.couriercompany.courier_company_api.pojos.SignupRequestPojo;
import com.couriercompany.courier_company_api.services.StaffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {StaffController.class})
@ExtendWith(SpringExtension.class)
class StaffControllerTest {
    @Autowired
    private StaffController staffController;

    @MockBean
    private StaffService staffService;

    @Test
    void staffControllerToSignupAndReturnString() throws Exception {
        when(staffService.signup(any())).thenReturn(new SignupResponseDto());

        SignupRequestPojo signupRequestPojo = new SignupRequestPojo();
        signupRequestPojo.setEmail("aishatmoshood.gmail.com");
        signupRequestPojo.setFirstName("Aishat");
        signupRequestPojo.setLastName("Moshood");
        signupRequestPojo.setPassword("123456789");
        String content = (new ObjectMapper()).writeValueAsString(signupRequestPojo);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/staff/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(staffController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void staffControllerToResendVerificationTokenReturnString() throws Exception {
        when(staffService.resendVerificationToken(any())).thenReturn("Verification Token sent successfully");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/staff/resend-verification-token")
                .param("email", "aishatmoshood@gmail.com");
        MockMvcBuilders.standaloneSetup(staffController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Verification Token sent successfully"));
    }

    @Test
    void staffControllerToVerifyAccountAndReturnString() throws Exception {
        when(staffService.verifyRegistration(any())).thenReturn("Verification Successful");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/staff/verify-registration")
                .param("token", "sdefdjxkcdshfgccznfv");
        MockMvcBuilders.standaloneSetup(staffController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Verification Successful"));
    }
}

