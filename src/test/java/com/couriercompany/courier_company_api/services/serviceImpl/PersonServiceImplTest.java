package com.couriercompany.courier_company_api.services.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PersonServiceImpl.class})
@ExtendWith(SpringExtension.class)
class PersonServiceImplTest {
    @Autowired
    private PersonServiceImpl personServiceImpl;

    @Test
    void confirmIfUserLoggedIn_toReturnBoolean() {
        assertTrue(personServiceImpl.confirmIfUserLoggedIn());
    }
}

