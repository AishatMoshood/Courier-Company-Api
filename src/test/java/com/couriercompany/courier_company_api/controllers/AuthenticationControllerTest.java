package com.couriercompany.courier_company_api.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.couriercompany.courier_company_api.pojos.LoginRequestPojo;
import com.couriercompany.courier_company_api.services.StaffService;
import com.couriercompany.courier_company_api.utils.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthenticationController.class})
@ExtendWith(SpringExtension.class)
class AuthenticationControllerTest {
    @Autowired
    private AuthenticationController authenticationController;

    @MockBean
    private StaffService staffService;

    @Test
    void loginToReturnString() throws Exception {
        when(staffService.login(any())).thenReturn(new ApiResponse<>());

        LoginRequestPojo loginRequestPojo = new LoginRequestPojo();
        loginRequestPojo.setEmail("aishatmoshood1@gmail.com");
        loginRequestPojo.setPassword("123456789");
        String content = (new ObjectMapper()).writeValueAsString(loginRequestPojo);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(authenticationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"message\":null,\"status\":null,\"data\":null,\"statusCode\":null}"));
    }
}

