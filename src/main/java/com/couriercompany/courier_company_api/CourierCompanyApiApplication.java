package com.couriercompany.courier_company_api;

import com.couriercompany.courier_company_api.config.jwt.RSAKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyProperties.class)
public class CourierCompanyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourierCompanyApiApplication.class, args);
    }

}
